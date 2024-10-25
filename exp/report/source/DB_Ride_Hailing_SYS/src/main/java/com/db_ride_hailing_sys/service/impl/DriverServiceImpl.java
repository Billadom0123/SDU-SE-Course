package com.db_ride_hailing_sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.Entity.DBSCAN_driverPoint;
import com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity.StringWSDriverMessageForCustomer;
import com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity.StringWSOrderMessageForCustomer;
import com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity.WSReceivePassengerForCustomer;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Geo_Distance;
import com.db_ride_hailing_sys.dao.*;
import com.db_ride_hailing_sys.dto.*;
import com.db_ride_hailing_sys.entity.*;
import com.db_ride_hailing_sys.service.IDriverService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.db_ride_hailing_sys.Util.DBSCAN_Util.clusterUtil.*;
import static com.db_ride_hailing_sys.Util.DriverUtil.indentUtil.addToRejectList;
import static com.db_ride_hailing_sys.Util.DriverUtil.indentUtil.deleteDriverOrder;
import static com.db_ride_hailing_sys.Util.DriverUtil.statusUtil.*;
import static com.db_ride_hailing_sys.Util.RequestUtil.requestUtil.*;
import static com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast.websocketBroadcast.*;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_ARGS_CONSTANT.defaultEllipsoid;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.clusters;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.queueFlow;
import static com.db_ride_hailing_sys.component.DispatchOrderServer.sessionMap;
import static com.db_ride_hailing_sys.constant.BusinessConstant.*;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class DriverServiceImpl extends ServiceImpl<DriverDao, Driver> implements IDriverService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserDao userDao;

    @Resource
    private DriverDao driverDao;

    @Resource
    private IndentDao indentDao;

    @Resource
    private RequestDao requestDao;

    @Resource
    private ReservationDao reservationDao;

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private BelongDao belongDao;

    private static final DefaultRedisScript<Long> CREATEREQ_SCRIPT;

    static{
        CREATEREQ_SCRIPT = new DefaultRedisScript<>();
        CREATEREQ_SCRIPT.setLocation(new ClassPathResource("LuaScript/createCustomerRequest.lua"));
        CREATEREQ_SCRIPT.setResultType(Long.class);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final Map<Long,DriverInfo> DriverStatus = new HashMap<>();

    @Override
    //司机请求听单
    public Result startBusiness(DriverDTO driverDTO) throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
        //伪造请求校验:是否是司机
        Long driverId = driverDTO.getId();
        Long role = userDao.queryRoleByUID(driverId);
        if(role!=Driver_Role){
            return Result.fail("当前用户不为司机类型，不能开始听单");
        }
        //判断该司机是否已经注册车辆，没有注册车辆无法开始听单
        Integer count = belongDao.queryCountByDriver(driverId);
        if(count==null||count==0){
            return Result.fail("当前用户未注册车辆，不能开始听单");
        }
        //先查询这个司机是否已经在听单
        if(DriverStatus.get(driverId)!=null){
            return Result.fail("当前司机已经处于接单状态");
        }
        //查询id对应司机的信用分，和当前定位一起封装为DriverInfo
        User user = userDao.queryById(driverId);
        DriverInfo driverInfo = new DriverInfo(driverId, driverDTO.getCurX(), driverDTO.getCurY(), user.getCredit(),judgeStatus(driverId));
//        log.debug(driverInfo.getIdle().toString());
        log.debug(driverOrder.get(driverId)==null?"":driverOrder.get(driverId).toString());
        log.debug(driverRunning.get(driverId)==null?"":driverRunning.get(driverId).toString());
        synchronized(user.getId()) {
            //将司机信息添加到空闲司机列表中
            DriverStatus.put(driverId,driverInfo);
            //如果是第一个加入的司机，那么updateBusyIdleFlow会初始化并更新聚簇，就不需要将当前司机加入最近的聚簇了
            if(!updateBusyIdleFlow()){
                //在队列流水到达阈值之前，寻找一个距离当前节点最近的key，将该司机加入对应的聚簇中
                addDriverToCluster(driverInfo);
            }
        }
        return Result.ok();
    }

    @Override
    public Result registerLicense(LicenseDTO licenseDTO) {
        Long uid = licenseDTO.getId();
        String license = licenseDTO.getLicense();
        if(null==uid||null==license||license.equals("")){
            return Result.fail("司机信息不完整");
        }
        Long role = userDao.queryRoleByUID(uid);
        if(null==role||role!=Driver_Role){
            return Result.fail("用户身份不合法");
        }
        if (!Valid_Type.contains(license)) {
            return Result.fail("提交驾照类型不合法");
        }
        driverDao.updateLicenseByUserId(license,uid);
        return Result.ok();
    }

    @Override
    public Result endBusiness(Long id) {
        //查询是否是伪造的请求
        Long role_id = userDao.queryRoleByUID(id);
        if(role_id!=Driver_Role||sessionMap.get(id)==null){
            return Result.fail("当前司机不存在");
        }
        //从在线司机列表中移除该司机
        DriverInfo driverInfo = DriverStatus.remove(id);
        //从拒绝订单哈希表中移除该司机
        driverReject.remove(id);
        //从聚簇中移除该司机
        removeDriverFromCluster(id);
        if(driverInfo!=null){
            //响应司机他已经取消听单
            return Result.ok();
        }
        return Result.fail("服务端代码逻辑异常:正常请求结束听单的司机不在在线司机列表中");
    }

    @Override
    public Result confirmOrder(Long id, Long requestId, Boolean accept) {
        //伪造请求检验:司机是否在线
        if(DriverStatus.get(id)==null){
            return Result.fail("该司机已经离线");
        }
        //无论是否接受，把他从driverDispatch中放出来
        driverDispatch.remove(id);
        //获取请求消息
        Request req = requestDao.queryRequestIdByReqId(requestId);
        //不接受订单
        if(!accept){
            //将该订单计入司机拒绝的订单列表中
            recordReject(id,requestId);
            //通过lua脚本将其放回消息队列，重新排队
            rewriteRequest(requestId);
            return Result.ok();
        }
        //线程并发问题:如果一个司机没有立即返回服务器他是否愿意接单
        //可能在短时间内由于他是空闲状态而服务器给他分配了两单实时单
        //但一个司机同一时间只能接一单实时单，那么如果他接了前一单，无论后一单他是否接受，都应该被写回消息队列

        /**
         * 线程并发问题:司机确认接受前应该确保请求仍存在
         */
        Integer count = requestDao.queryRequestCountByReqId(req.getId());
        if(count<=0){
            return Result.fail("乘客已经取消了约车请求,无法接受订单");
        }

        //是否是预约单
        Integer reserveCount = reservationDao.queryCountByReqId(requestId);
        //实时单
        if(reserveCount==null||reserveCount==0) {
            DriverInfo driverInfo = DriverStatus.get(id);
            if (!driverInfo.getIdle()) {
                rewriteRequest(requestId);
                return Result.ok("当前司机已经处于忙碌状态,不能再接实时单");
            }
            //司机状态空闲变忙碌
            if (driverInfo.getIdle()) {
                changeStatus(id, false);
                //从聚簇中移除
                removeDriverFromCluster(id);
            }
        }
        //更新队列流水
        queueFlow++;
        //若队列流水达到某一阈值，更新聚簇
        if(queueFlow>=queueFlowMax){
            try {
                updateClusters();
            } catch (DBSCAN_ARGS_EXCEPTION | DBSCAN_POINT_EXCEPTION e) {
                throw new RuntimeException(e);
            }
        }
        Long customerId = req.getCustomerId();
        //更改请求信息，针对是否接单以及响应时间
        LocalDateTime now = LocalDateTime.now();
        log.debug("司机接单时间:"+now.toString());
        requestDao.updateRequestByReqId(true, now,requestId);
        //生成订单,并交由哈希表维护
        createOrder(id,req);
        //创建订单具体信息
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setReqId(requestId);
        requestInfo.setIs_reservation(is_Reservation(requestId));
        requestInfo.setIs_instead(is_Instead(requestId));
        //获取司机对应订单列表
        List<RequestInfo> reqInfoList = driverOrder.get(id);
        if(null== reqInfoList){
            reqInfoList = new ArrayList<>();
        }
        //加入新的订单
        reqInfoList.add(requestInfo);
        //重设司机对应订单列表
        driverOrder.put(id,reqInfoList);
        log.debug("司机"+id+"订单列表:"+reqInfoList.toString());
        log.debug("当前在线用户:"+sessionMap.toString());
        //ws响应乘客司机的信息
        //查询所有相关信息
        Long vehicleId = belongDao.queryVehicleIdByDriverId(id);
        Vehicle vehicle = vehicleDao.queryVehicleById(vehicleId);
        User user = userDao.queryById(id);
        //这个时候cancelInfo为空
        StringWSDriverMessageForCustomer stringWSDriverMessageForCustomer = new StringWSDriverMessageForCustomer(requestId.toString(), vehicle.getNumber(), vehicle.getType(), vehicle.getColor(), vehicle.getBrand(), id.toString(), user.getName(), user.getPhone(), user.getCredit().toString(),null);
        sendObject(stringWSDriverMessageForCustomer,sessionMap.get(customerId));
        return Result.ok();
    }



    @Override
    public Result commitOrder(CommitOrderDTO commitOrderDTO) {
        //校验是否为伪造的请求:数据库中是否有对应订单，这个订单是否逻辑存在
        Integer count = indentDao.queryCountByRequestIdAndDriverId(commitOrderDTO.getReqId(), commitOrderDTO.getId());
        if(count==null||count==0){
            return Result.fail("数据库中没有对应的合法订单");
        }
        //校验当前结束位置是否和请求中要求的结束位置大致一致
        Request request = requestDao.queryRequestIdByReqId(commitOrderDTO.getReqId());
        //计算等待时间差
        LocalDateTime requestTime = request.getRequestTime();
        LocalDateTime responseTime = request.getResponseTime();
        Duration between = Duration.between(requestTime, responseTime);
        long minute = between.toMinutes();
        Double startX = request.getStartX();
        Double startY = request.getStartY();
        Double desX = request.getDesX();
        Double desY = request.getDesY();
        Double endX = commitOrderDTO.getEndX();
        Double endY = commitOrderDTO.getEndY();
        Geo_Distance geoDistance = new Geo_Distance(defaultEllipsoid);
        double tarDiff = geoDistance.calculateDistance(
                new Point(desX, desY),
                new Point(endX, endY)
        );
        if(tarDiff>maxTargetDiff){
            return Result.fail("距离实际目的地太远,无法提交订单");
        }
        //更改数据库中对应订单的对应信息项
        //需要更改 price,distance,endX,endY,endTime
        //测算distance
        double distance;
        distance = geoDistance.calculateDistance(
                new Point(startX, startY),
                new Point(endX, endY)
        )/1000;
        //如果有效，根据提供的信息计算价格
        double price = 0.0;
        //网约车订单计价:起步价+时间费+里程费+超里程费+夜间费
        //一般来说不同类型的车在不同区域这些价格都不一样，但我懒得做了
        //所以统一起步价:14米，里程费3.6米/公里，时间费(等待费)1.2米/分钟，超里程阈值300公里，超里程费2米/公里，夜间域为23:00-05:00，夜间费0.4米/公里
        double mileCost = (Math.min(distance,OverMileAge)*MileAgeCost);
        double waitCost = minute*WaitCost;
        double overMileCost = distance>OverMileAge?(distance-OverMileAge)*OverMileAgeCost:0;
        log.debug("行车距离:"+distance+",里程费:"+mileCost+",等待费:"+waitCost+",超里程费:"+overMileCost);
        price+=14.0+mileCost+waitCost+overMileCost;
        //获取该订单的结束时间
        LocalDateTime endTime = LocalDateTime.now();
        //TODO:计算夜间费
        //更新订单信息
        indentDao.updateOrderByRequestIdAndDriverId(price,distance,endX,endY,endTime,commitOrderDTO.getReqId(),commitOrderDTO.getId());
        //司机可能在取消听单后提交之前的订单，这个时候在线司机列表里是访问不到这个司机的,为空直接跳过了
        //将司机状态修改为空闲，清空他正在跑的单子，同时清空他接收的订单列表中的该单子
        DriverInfo driverInfo = changeStatus(commitOrderDTO.getId(), true);
        if(driverInfo!=null){
            driverRunning.remove(commitOrderDTO.getId());
        }
        deleteDriverOrder(commitOrderDTO.getId(),commitOrderDTO.getReqId());
        //把司机放到聚簇中
        addDriverToCluster(driverInfo);
        //ws通知乘客他的订单已经完成，包括需要支付的价格以及对应的请求id
        log.debug("当前司机状态:"+DriverStatus.get(commitOrderDTO.getId())+",当前聚簇大小:"+getClustersSize()+",当前司机正在跑的单子:"+driverRunning.get(commitOrderDTO.getId()));
        StringWSOrderMessageForCustomer stringWSOrderMessageForCustomer = new StringWSOrderMessageForCustomer();
        stringWSOrderMessageForCustomer.setPrice(price+"");
        stringWSOrderMessageForCustomer.setReqId(request.getId().toString());
        sendObject(stringWSOrderMessageForCustomer,sessionMap.get(request.getCustomerId()));
        //响应司机他已经提交了订单，以及将得到的支付的价格
        return Result.ok(price);
    }

    @Override
    public Result deleteOrder(DeleteOrderDTO deleteOrderDTO) {
        //伪造请求校验
        Long id = deleteOrderDTO.getId();
        List<Long> requestList = deleteOrderDTO.getRequestList();
        for (Long reqId : requestList) {
            Integer count = indentDao.queryCountByRequestIdAndDriverId(reqId, id);
            if(count==null||count==0){
                return Result.fail("非法请求:订单不存在");
            }
        }
        RequestInfo running = driverRunning.get(id);
        //由于该司机删除了这一订单，所以他拒绝的订单中应该添加这一项
        List<Long> reject = driverReject.get(id);
        for (Long reqId : requestList) {
            if(running!=null&&Objects.equals(reqId, running.getReqId())){
                return Result.fail("不能删除已经接到乘客的订单");
            }
            //修改是否被接单以及响应时间(没被接单,没得到响应)
            requestDao.updateRequestByReqId(false,null,reqId);

            //这里应该通知乘客司机取消接单了，更换下一个司机，不然乘客的前端还有原来司机的信息啊
            StringWSDriverMessageForCustomer stringWSDriverMessageForCustomer = new StringWSDriverMessageForCustomer(reqId.toString(), null, null, null, null, null, null,null,null,"订单已取消");
            Request request = requestDao.queryRequestIdByReqId(reqId);
            sendObject(stringWSDriverMessageForCustomer,sessionMap.get(request.getCustomerId()));
            synchronized (reqId){
                //逻辑删除对应订单
                indentDao.logicDeleteOrderByReqId(reqId);
                //lua脚本写回消息队列
                rewriteRequest(reqId);
                //将其从司机订单列表中移除
                deleteDriverOrder(id,reqId);
                //添加到司机拒绝的订单中
                addToRejectList(id,reqId);
            }
        }
        //更新司机状态
        DriverInfo driverInfo = DriverStatus.get(id);
        if(driverInfo!=null){
            //司机在线，更新司机状态
            boolean status = judgeStatus(id);
            log.debug("司机取消订单后的状态应为:"+(status?"空闲":"忙碌"));
            driverInfo.setIdle(status);
            if(status){
                //如果司机处于空闲状态，应该把他加入回聚簇
                addDriverToCluster(driverInfo);
            }
        }
        //响应司机已经删除了请求
        return Result.ok();
    }

    @Override
    public Result receivePassenger(Long id,Long reqId) {
        /**
         * 线程并发问题:司机接到乘客前应确保对应请求仍存在
         */
        Integer count = requestDao.queryRequestCountByReqId(reqId);
        if(count<=0){
            return Result.fail("乘客已经取消对应约车请求,无法接到该乘客");
        }

        //司机可能在取消听单后接到之前确认过的订单的乘客，这个时候changeStatus返回null，直接跳过了
        //无论司机现在是什么状态，把他置为忙碌
        DriverInfo driverInfo = changeStatus(id,false);
        if(driverInfo!=null){
            //更新其正在跑的单子
            driverRunning.put(id,getRequestInfo(reqId));
            //从聚簇中移除
            removeDriverFromCluster(id);
            //更新当前order的开始时间
            indentDao.updateOrderStartTimeByReqId(LocalDateTime.now(),reqId);
            Long customerId = requestDao.queryCustomerIdByReqId(reqId);
            sendObject(new WSReceivePassengerForCustomer(reqId.toString(),true),sessionMap.get(customerId));
        }
        return Result.ok();
    }

    @Override
    public Result arriveStart(Long id, Long reqId,Double curX,Double curY) {
        //伪造请求校验:是否是司机
        Long role = userDao.queryRoleByUID(id);
        if(role!=Driver_Role){
            return Result.fail("当前用户不为司机类型");
        }
        //检查是否存在对应订单
        Integer count = indentDao.queryCountByRequestIdAndDriverId(reqId, id);
        if(count==null||count==0){
            return Result.fail("不存在对应订单");
        }
        //检查是否真正抵达出发点
        Request request = requestDao.queryRequestIdByReqId(reqId);
        Double startX = request.getStartX();
        Double startY = request.getStartY();
        Geo_Distance geoDistance = new Geo_Distance(defaultEllipsoid);
        double distance = geoDistance.calculateDistance(
                new Point(startX, startY),
                new Point(curX, curY)
        );
        if(distance>=maxTargetDiff){
            return Result.fail("距离实际出发点太远,请继续前进");
        }
        //ws通知乘客司机已经抵达出发点
        //获取乘客id
        Long customerId = request.getCustomerId();
        sendMessage("司机已经抵达出发点:"+request.getStartName(),sessionMap.get(customerId));
        //响应司机请求成功
        return Result.ok();
    }

    private void createOrder(Long driverId, Request request){
        //添加司机的车牌号
        long vehicleId=belongDao.queryVehicleIdByDriverId(driverId);
        Vehicle vehicle=vehicleDao.queryVehicleById(vehicleId);

        //根据已有信息创建订单
        Indent indent = new Indent();
        indent.setDriverId(driverId);
        indent.setNumber(vehicle.getNumber());
        indent.setReceiveTime(LocalDateTime.now());
        //indent.setStartTime();
        indent.setRequestId(request.getId());
        indent.setPriority(request.getPriority());
        indentDao.insert(indent);
    }

    private void rewriteRequest(Long requestId){
        //查询是否是特殊订单
        boolean is_reservation=is_Reservation(requestId);
        boolean is_instead=is_Instead(requestId);
        //通过lua脚本将其放回消息队列，重新排队
        stringRedisTemplate.execute(
                CREATEREQ_SCRIPT,
                Collections.emptyList(),
                requestId.toString(),
                Boolean.toString(is_reservation),
                Boolean.toString(is_instead)
        );
    }

    private void recordReject(Long driverId,Long requestId){
        List<Long> rejectList = driverReject.get(driverId);
        if(rejectList==null){
            rejectList = new ArrayList<Long>();
        }
        rejectList.add(requestId);
        driverReject.put(driverId,rejectList);
    }
}