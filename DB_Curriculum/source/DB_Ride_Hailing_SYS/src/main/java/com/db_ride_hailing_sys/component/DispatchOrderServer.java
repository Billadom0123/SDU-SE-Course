package com.db_ride_hailing_sys.component;


import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.EntityInfo.Location;
import com.db_ride_hailing_sys.Util.websocketBroadcast.Encoder.RequestForDriverEncoder;
import com.db_ride_hailing_sys.dao.IndentDao;
import com.db_ride_hailing_sys.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.db_ride_hailing_sys.constant.BusinessConstant.Driver_Role;
import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;

/**
 * 分配订单后向得到分配的乘客客户端发送消息
 */
@ServerEndpoint(value = "/DispatchOrderServer/{userId}",encoders = {RequestForDriverEncoder.class})
@Component
public class DispatchOrderServer {
    private UserDao userDao = SpringUtil.getBean(UserDao.class);

    private IndentDao orderDao = SpringUtil.getBean(IndentDao.class);

    private static final Logger log = LoggerFactory.getLogger(DispatchOrderServer.class);

    /**
     * 记录当前在线的用户
     */
    public static final Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId")Long userId){
//        //查询当前uID是否合法:用户是否存在，用户类型是否为乘客
//        Long role = userDao.queryRoleByUID(userId);
//        //用户不存在或用户类型不为乘客
//        if(null==role||role!=Customer_Role){
//            return;
//        }
//        合法，把该用户客户端加入sessionMap
        sessionMap.put(userId,session);
        log.info("{}用户登入,sessionId:{}",userId,session.getId());
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId")Long userId){
        sessionMap.remove(userId);
        log.info("{}用户退出",userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("客户端发生错误");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("用户发来消息{}",message);
        Location location = messageToLocation(message);
        //更新司机的实时位置,由于乘客的实时位置在服务端程序中根本没有维护(也不需要维护),所以仅做一下是否为司机的身份校验即可
        Long uid = location.getId();
        Long role_id = userDao.queryRoleByUID(uid);
        if(role_id==null||!role_id.equals(Driver_Role)){
            log.info("用户身份不合法:不为司机");
            return;
        }
        DriverInfo driverInfo = DriverStatus.get(uid);
        if(driverInfo==null){
            log.debug("需要更新位置的司机不在在线司机列表中");
            return;
        }
        driverInfo.setCurX(location.getCurX());
        driverInfo.setCurY(location.getCurY());
    }

    private Location messageToLocation(String message){
        JSONObject jsonObject = JSONUtil.parseObj(message);
        Long uid = Long.parseLong(jsonObject.getStr("id"));
        Double curX = Double.parseDouble(jsonObject.getStr("curX"));
        Double curY = Double.parseDouble(jsonObject.getStr("curY"));
        Location location = new Location();
        location.setId(uid);location.setCurX(curX);location.setCurY(curY);
        return location;
    }

}
