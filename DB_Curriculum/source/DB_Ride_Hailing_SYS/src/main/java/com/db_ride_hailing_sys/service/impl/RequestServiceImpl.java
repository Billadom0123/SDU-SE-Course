package com.db_ride_hailing_sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.dao.*;
import com.db_ride_hailing_sys.dto.CancelRequestDTO;
import com.db_ride_hailing_sys.dto.RequestDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.entity.Indent;
import com.db_ride_hailing_sys.entity.Instead;
import com.db_ride_hailing_sys.entity.Request;
import com.db_ride_hailing_sys.entity.Reservation;
import com.db_ride_hailing_sys.service.IRequestService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.db_ride_hailing_sys.Util.DBSCAN_Util.clusterUtil.addDriverToCluster;
import static com.db_ride_hailing_sys.Util.DriverUtil.statusUtil.changeStatus;
import static com.db_ride_hailing_sys.Util.DriverUtil.statusUtil.judgeStatus;
import static com.db_ride_hailing_sys.Util.RequestUtil.requestUtil.*;
import static com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast.websocketBroadcast.sendCancelRequestResult;
import static com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast.websocketBroadcast.sendMessage;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.queueFlow;
import static com.db_ride_hailing_sys.component.DispatchOrderServer.sessionMap;
import static com.db_ride_hailing_sys.constant.BusinessConstant.cancelRequest;
import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverOrder;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverRunning;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class RequestServiceImpl extends ServiceImpl<RequestDao, Request> implements IRequestService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private RequestDao requestDao;

    @Resource
    private IndentDao indentDao;

    @Resource
    private ReservationDao reservationDao;

    @Resource
    private InsteadDao insteadDao;

    private static final DefaultRedisScript<Long> CREATEREQ_SCRIPT;

    static{
        CREATEREQ_SCRIPT = new DefaultRedisScript<>();
        CREATEREQ_SCRIPT.setLocation(new ClassPathResource("LuaScript/createCustomerRequest.lua"));
        CREATEREQ_SCRIPT.setResultType(Long.class);
    }

    @Override
    public Result createCustomerRequest(RequestDTO requestDTO) {
        //判断这个请求是否是伪造的
        Long id = requestDTO.getId();
        Long cId = customerDao.queryCustomerIdByUID(id);
        if(cId==null){
            return Result.fail("非法请求，乘客不存在");
        }
        //生成请求，如果是特殊类型的请求(如代叫，预约)，则向reservation,instead表中添加特殊请求
        Request req = createRequest(requestDTO);
        //执行lua脚本
        Long res = stringRedisTemplate.execute(
                CREATEREQ_SCRIPT,
                Collections.emptyList(),
                req.getId().toString(),
                requestDTO.getIs_reservation().toString(),
                requestDTO.getIs_instead().toString()
        );
        log.debug("lua脚本写入请求"+req.getId());
        //向客户端返回请求id
        return Result.ok(req.getId().toString());
    }

    private Request createRequest(RequestDTO requestDTO){
        Long uid = requestDTO.getId();
        Integer priority = requestDTO.getPriority();
        Double startX = requestDTO.getStartX();
        Double startY = requestDTO.getStartY();
        String startName = requestDTO.getStart_name();
        Double desX = requestDTO.getDesX();
        Double desY = requestDTO.getDesY();
        String desName = requestDTO.getDes_name();
        //创建基础request
        Request req = new Request();
        req.setCustomerId(uid);req.setPriority(priority);req.setRequestTime(LocalDateTime.now());req.setStartX(startX);req.setStartY(startY);req.setStartName(startName);
        req.setDesX(desX);req.setDesY(desY);req.setDesName(desName);
        //存入数据库
        save(req);
        //获取自动生成的订单id
        Long reqId = req.getId();
        //如果是预约
        Boolean isReservation = requestDTO.getIs_reservation();
        if (isReservation) {
            LocalDateTime appointmentTime = requestDTO.getAppointment_time();
            Reservation reservation = new Reservation();
            reservation.setRequestId(reqId);
            reservation.setReserveTime(appointmentTime);
            reservationDao.insert(reservation);
        }
        //如果是代叫
        Boolean isInstead = requestDTO.getIs_instead();
        if(isInstead){
            String customerPhone = requestDTO.getCustomer_phone();
            Instead instead = new Instead();
            instead.setRequest_id(reqId);
            instead.setCustomer_phone(customerPhone);
            insteadDao.insert(instead);
        }
        return req;
    }

    @Override
    public Result cancelRequest(CancelRequestDTO cancelRequestDTO) {
        //伪造请求校验
        Long usr = cancelRequestDTO.getId();
        List<Long> requestList = cancelRequestDTO.getRequestList();
        for (Long reqId : requestList) {
            Integer count = requestDao.queryCountByRequestIdAndCustomerId(reqId, usr);
            if(count==null||count==0){
                //这里不能暴力return，可能影响前面或后面的对数据库的操作
                sendMessage("指定删除的请求不合法:"+reqId,sessionMap.get(usr));
                continue;
            }
            //获取该请求对应的订单
            Indent indent = indentDao.queryOrderByReqId(reqId);
            if(null== indent){
                //没有对应的订单，直接把请求删了
                requestDao.logicDeleteRequestByReqId(false,reqId);
                //仅通知乘客订单取消
                sendCancelRequestResult(cancelRequest,sessionMap.get(usr));
                continue;
            }
            //有对应订单
            Long driverId = indent.getDriverId();
            //检查是否已经接到乘客
            RequestInfo receiveCheck = driverRunning.get(driverId);
            if(receiveCheck!=null&&receiveCheck.getReqId().equals(reqId)){
                sendMessage("司机已经接到您了,不能再取消订单:"+reqId,sessionMap.get(usr));
                continue;
            }
            synchronized (reqId){
                //逻辑删除该请求
                requestDao.logicDeleteRequestByReqId(false,reqId);
                //查询是否是特殊请求,级联删除
                if(is_Reservation(reqId)){
                    reservationDao.logicDeleteReservationByReqId(reqId);
                }
                if(is_Instead(reqId)){
                    insteadDao.logicDeleteInsteadByReqId(reqId);
                }
                //逻辑删除对应订单
                indentDao.logicDeleteOrderByReqId(reqId);
                log.debug("司机"+driverId+"订单列表:"+ driverOrder.get(driverId));
                //同时删除哈希表中订单
                boolean has_Order = deleteOrder(reqId, driverId);
                if(!has_Order){
                    log.debug("该订单不存在");
                }
            }
            //检查司机是否在线
            DriverInfo onlineCheck = DriverStatus.get(driverId);
            if(onlineCheck==null){
                //司机已经取消听单，不需要更新他的状态
                queueFlow++;
                //通知司机订单已经取消  要加上reqId啊，不然前端怎么知道哪个订单被取消了可恶
                sendCancelRequestResult(cancelRequest+reqId,sessionMap.get(driverId));
            }
            //司机在线,判断司机此时应该是什么状态
            boolean status = judgeStatus(driverId);
            if(status!=onlineCheck.getIdle()){
                //不相等只有一种情况，从忙碌到空闲，因为不可能删除订单把司机从空闲删除到忙碌的
                //从忙碌司机列表中取出订单对应的司机
                DriverInfo driverInfo = changeStatus(driverId,true);
                queueFlow++;
                //将该司机加入距离最近的聚簇中
                addDriverToCluster(driverInfo);
            }
            //通知司机订单已经取消 要加上reqId啊，不然前端怎么知道哪个订单被取消了可恶
            sendCancelRequestResult(cancelRequest+reqId,sessionMap.get(driverId));
        }
        return Result.ok();
    }

}