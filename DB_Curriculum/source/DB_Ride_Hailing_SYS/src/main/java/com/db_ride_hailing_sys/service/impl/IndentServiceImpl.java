package com.db_ride_hailing_sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.Entity.DBSCAN_driverPoint;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.calculator.LeastDisCalculator;
import com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity.StringWSRequestMessageForDriver;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.dao.*;
import com.db_ride_hailing_sys.dto.DeleteCompletedOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.dto.ShowOrderDTO;
import com.db_ride_hailing_sys.entity.Indent;
import com.db_ride_hailing_sys.entity.Request;
import com.db_ride_hailing_sys.entity.Reservation;
import com.db_ride_hailing_sys.entity.User;
import com.db_ride_hailing_sys.service.IIndentService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.db_ride_hailing_sys.Util.DBSCAN_Util.clusterUtil.getClustersSize;
import static com.db_ride_hailing_sys.Util.DBSCAN_Util.clusterUtil.updateClusters;
import static com.db_ride_hailing_sys.Util.DataUtil.dataUtil.pointTransfer;
import static com.db_ride_hailing_sys.Util.DriverUtil.statusUtil.getIdleCount;
import static com.db_ride_hailing_sys.Util.LogUtil.logUtil.*;
import static com.db_ride_hailing_sys.Util.RequestUtil.requestUtil.is_Instead;
import static com.db_ride_hailing_sys.Util.RequestUtil.requestUtil.is_Reservation;
import static com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast.websocketBroadcast.sendDispatchResult;
import static com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast.websocketBroadcast.sendMessage;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_ARGS_CONSTANT.defaultEllipsoid;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.clusters;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.queueFlow;
import static com.db_ride_hailing_sys.component.DispatchOrderServer.sessionMap;
import static com.db_ride_hailing_sys.constant.BusinessConstant.Customer_Role;
import static com.db_ride_hailing_sys.constant.LogConstant.logFilePath;
import static com.db_ride_hailing_sys.constant.RedisConstant.*;
import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class IndentServiceImpl extends ServiceImpl<IndentDao, Indent> implements IIndentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RequestDao requestDao;

    @Resource
    private UserDao userDao;

    @Resource
    private InsteadDao insteadDao;

    @Resource
    private ReservationDao reservationDao;

    @Resource
    private IndentDao indentDao;

    public static final Map<Long,List<RequestInfo>> driverOrder = new HashMap<>();

    public static final Map<Long,RequestInfo> driverRunning = new HashMap<>();

    public static final Map<Long,List<Long>> driverReject = new HashMap<>();

    public static final Map<Long,Long> driverDispatch = new HashMap<>();

    private static final DefaultRedisScript<Long> CREATEREQ_SCRIPT;

    static{
        CREATEREQ_SCRIPT = new DefaultRedisScript<>();
        CREATEREQ_SCRIPT.setLocation(new ClassPathResource("LuaScript/createCustomerRequest.lua"));
        CREATEREQ_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init(){
        ORDER_EXECUTOR.submit(new ORDER_HANDLER());
    }

    private class ORDER_HANDLER implements Runnable{

        @Override
        public void run() {
            while(true){
                if(getIdleCount()==0){
//                    log.debug("当前没有空闲司机,聚簇中司机数量:"+getClustersSize());
                    //当前没有空闲司机，没法分配订单
                    continue;
                }
                try {
                    //获取消息队列中的订单信息
                    List<MapRecord<String, Object, Object>> req = stringRedisTemplate.opsForStream().read(
                            Consumer.from(ORDER_CONSUMER_GROUP, ORDER_CONSUMER),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(STREAM_ORDER, ReadOffset.lastConsumed())
                    );
                    if(req == null || req.isEmpty()){
                        continue;
                    }
                    MapRecord<String, Object, Object> record = req.get(0);
                    //确认消息
                    stringRedisTemplate.opsForStream().acknowledge(STREAM_ORDER,ORDER_CONSUMER_GROUP,record.getId());
                    Map<Object, Object> value = record.getValue();
                    RequestInfo requestInfo = BeanUtil.fillBeanWithMap(value, new RequestInfo(), true);
                    Long reqId = requestInfo.getReqId();
//                    log.debug("请求id:"+reqId);
                    synchronized (reqId){
                        //查询数据库，判断请求是否还存在
                        Integer count = requestDao.queryRequestCountByReqId(reqId);
                        if(count==null||count==0){
                            continue;//订单已经取消
                        }
                        //分配订单
                        Request request = requestDao.queryRequestIdByReqId(reqId);
                        log.debug(ORDER_DISPATCHING_Info(reqId));
                        dispatchOrders(request,requestInfo.getIs_instead(),requestInfo.getIs_reservation());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        handlePendingList();
                    } catch (DBSCAN_ARGS_EXCEPTION | DBSCAN_POINT_EXCEPTION ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        private void handlePendingList() throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
            while(true){
                try {
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from(ORDER_CONSUMER_GROUP, ORDER_CONSUMER),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(STREAM_ORDER, ReadOffset.from("0"))
                    );
                    if(list==null||list.isEmpty()){
                        break;
                    }
                    MapRecord<String, Object, Object> record = list.get(0);
                    //确认消息
                    stringRedisTemplate.opsForStream().acknowledge(STREAM_ORDER,ORDER_CONSUMER_GROUP,record.getId());
                    Map<Object, Object> value = record.getValue();
                    RequestInfo requestInfo = BeanUtil.fillBeanWithMap(value, new RequestInfo(), true);
                    Long reqId = requestInfo.getReqId();
                    synchronized (reqId){
                        //查询数据库，判断请求是否还存在
                        Integer count = requestDao.queryRequestCountByReqId(reqId);
                        if(count==null||count==0){
                            continue;//订单已经取消
                        }
                        //分配订单
                        Request req = requestDao.queryRequestIdByReqId(reqId);
                        log.debug(ORDER_DISPATCHING_Info(reqId));
                        dispatchOrders(req,requestInfo.getIs_instead(),requestInfo.getIs_reservation());
                    }
                } catch (DBSCAN_ARGS_EXCEPTION | DBSCAN_POINT_EXCEPTION e) {
                    log.debug( ORDER_DISPATCH_FAIL_Info());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //上同步锁
    private synchronized void dispatchOrders(Request request,Boolean is_instead,Boolean is_reservation) throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
        //查询聚簇是否存在，不存在更新聚簇
        if(null == clusters || null == queueFlow){
            updateClusters();
        }
        log.debug(CLUSTER_SIZE_Info());
//        System.out.println(CLUSTER_SIZE_Info());
        //轮询所有聚簇直到发现一个没有拒绝过该订单的司机
        boolean flag = false;//是否存在没有拒绝过的司机
        Long driverId = null;
        //先把当前位置转换为Point实例
        Point point = pointTransfer(request);
        int length = clusters.size();
        LeastDisCalculator leastDisCalculator = new LeastDisCalculator(defaultEllipsoid);
        Long reqId = request.getId();
        outer:
        for(int i=1;i<=length;i++){
            Cluster kthLeastDistanceCluster = leastDisCalculator.getKthLeastDistanceCluster(point, clusters, i);
            for (Point p : kthLeastDistanceCluster.getCq()) {
                DBSCAN_driverPoint driverPoint = (DBSCAN_driverPoint) p;
                long dId = driverPoint.getUId();
                if(driverDispatch.get(dId)!=null){
                    //已经为他分配了订单，且该司机还没确认订单
                    continue ;
                }
                List<Long> rejectList = driverReject.get(dId);
                //该司机拒绝的列表中不包含该请求
                if(rejectList==null||!rejectList.contains(reqId)){
                    flag = true;
                    driverId = dId;
                    break outer;
                }
            }
        }
        driverDispatch.put(driverId,reqId);
        log.debug(DRIVER_REJECT_LIST_Info(driverId));
        //分配不成功,没法写回消息队列了，不然只有一条消息的时候会疯狂反复处理然后服务器崩掉
        if(!flag){
            sendMessage("没有司机愿意接单",sessionMap.get(request.getCustomerId()));
//            rewriteRequest(reqId);
            return;
        }
        log.debug(DISPATCH_DRIVERID_Info(driverId));
        //将订单消息通知给司机客户端
        StringWSRequestMessageForDriver message = createMessage(request, is_instead, is_reservation);
        String dispatchResult = DISPATCH_RESULT(reqId, driverId);
        log.debug(dispatchResult);
        //日志以追加的方式写入到指定文件当中
        recordLog(logTimePrefixSuffix(dispatchResult),logFilePath,true);

        sendDispatchResult(message,sessionMap.get(driverId));
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

    private StringWSRequestMessageForDriver createMessage(Request request,Boolean is_instead,Boolean is_reservation){
        //Bug:Long型导致精度丢失
        StringWSRequestMessageForDriver message = new StringWSRequestMessageForDriver();
        message.setId(request.getId().toString());
        message.setPriority(request.getPriority().toString());
        message.setStartX(request.getStartX().toString());
        message.setStartY(request.getStartY().toString());
        message.setStartName(request.getStartName());
        message.setDesX(request.getDesX().toString());
        message.setDesY(request.getDesY().toString());
        message.setDesName(request.getDesName());
        message.setAppointmentTime("");
        Long reqId = request.getId();
        //根据是否代叫设置手机号
        if(is_instead){
            //如果是代叫，手机号应该是被代叫人的手机号
            message.setPhone(insteadDao.queryCustomerPhoneByReqId(reqId));
        }
        else{
            //否则无论是预约还是实时单，都是自己手机号
            message.setPhone(userDao.queryPhoneByUID(request.getCustomerId()));
        }
        //根据是否预约设置预约时间
        if(is_reservation){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //时间转指定格式注入返回对象
            Reservation reservation = reservationDao.queryReservationByRequestId(reqId);
            log.debug(reservation.toString());
            LocalDateTime reserveTime = reservation.getReserveTime();
            message.setAppointmentTime(reserveTime.format(dateTimeFormatter));
        }
        return message;
    }

    @Override
    public Result deleterOrder(DeleteCompletedOrderDTO deleteCompletedOrderDTO){
        Long role = deleteCompletedOrderDTO.getRole();
        Long id=deleteCompletedOrderDTO.getIndentId();
//        System.out.println(deleteCompletedOrderDTO);
        log.debug(deleteCompletedOrderDTO.toString());
        if(role==Customer_Role){
            indentDao.updateOrderCustomerDeleteById(id);
        }
        else{
            indentDao.updateOrderDriverDeleteById(id);
        }
        return Result.ok();
    }

    @Override
    public Result showOrder(Long id,Long role) {
        return Result.ok(createOrderList(id,role));
    }

    //这个方法查到的是已经完成的订单吗？ 如果不是的话 null会报错吧
    //通过改price确保查到的一定是完成的订单
    private List<ShowOrderDTO> createOrderList(Long id,Long role){
        //新建列表，存放所有待返回的DTO
        ArrayList<ShowOrderDTO> res = new ArrayList<>();
        if(role==Customer_Role){
            //如果是乘客，根据id查询所有被接受的请求
            List<Request> reqs = requestDao.queryAcceptedRequestByCustomerId(id);
            for (Request req : reqs) {
                //再根据每个请求的id查询出所有订单
                Long reqId = req.getId();
                Indent indent;

                indent = indentDao.queryOrderByReqIdandCustomer(reqId);
                if(indent == null){
                    continue;
                }

                ShowOrderDTO showOrderDTO = createShowOrder(indent,req);
                res.add(showOrderDTO);
            }
        }
        else{
            //如果是司机，根据id查询所有订单
            List<Indent> indents = indentDao.queryIndentByDriverId(id);//
            for (Indent indent : indents) {
                //再根据每个订单的request_id查询所有对应请求
                Long requestId = indent.getRequestId();
                Request request = requestDao.queryRequestIdByReqId(requestId);
                ShowOrderDTO showOrder = createShowOrder(indent, request);
                //将订单与请求中的信息拼起来，新建对象置入列表
                res.add(showOrder);
            }
        }
//        System.out.println(res);
        log.debug(res.toString());
        return res;
    }

    private ShowOrderDTO createShowOrder(Indent indent,Request req){
        ShowOrderDTO showOrderDTO;
        showOrderDTO = new ShowOrderDTO();
        showOrderDTO.setIndentId(indent.getId().toString());
        Long reqId = req.getId();
        showOrderDTO.setReqId(reqId.toString());
        showOrderDTO.setResponseTime(req.getResponseTime());
        showOrderDTO.setStartTime(indent.getStartTime());
        Reservation reservation = reservationDao.queryReservationByRequestId(reqId);
        if(reservation!=null){
            showOrderDTO.setAppointTime(reservation.getReserveTime());
        }
        showOrderDTO.setStartX(req.getStartX());
        showOrderDTO.setStartY(req.getStartY());
        showOrderDTO.setEndX(indent.getEndX());
        showOrderDTO.setEndY(indent.getEndY());
        showOrderDTO.setStartName(req.getStartName());
        showOrderDTO.setEndName(req.getDesName());
        showOrderDTO.setPrice(indent.getPrice());

        //乘客端显示司机name 手机号
        User userDriver=userDao.queryById(indent.getDriverId());
        showOrderDTO.setDriverName(userDriver.getName());
        showOrderDTO.setDriverPhone(userDriver.getPhone());
        //车牌号
        showOrderDTO.setNumber(indent.getNumber());


        //司机端显示乘客name
        User userCustomer=userDao.queryById(req.getCustomerId());
        showOrderDTO.setCustomerName(userCustomer.getName());
        showOrderDTO.setCustomerPhone(userCustomer.getPhone());

        //司机接单时间
        showOrderDTO.setReceiveTime(indent.getReceiveTime());
        //订单结束时间
        showOrderDTO.setEndTime(indent.getEndTime());

        return showOrderDTO;
    }
}