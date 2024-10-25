package com.db_ride_hailing_sys.component;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.EntityInfo.Location;
import com.db_ride_hailing_sys.dao.IndentDao;
import com.db_ride_hailing_sys.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import static com.db_ride_hailing_sys.constant.BusinessConstant.Driver_Role;
import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;

@ServerEndpoint(value="/RealTimeLocationUpdateServer")
@Component
public class RealTimeLocationUpdateServer {
    private UserDao userDao = SpringUtil.getBean(UserDao.class);

    private IndentDao orderDao = SpringUtil.getBean(IndentDao.class);
    private static final Logger log = LoggerFactory.getLogger(RealTimeLocationUpdateServer.class);

    @OnOpen
    public void onOpen(Session session){
        log.info("用户登入 (from LUServer)");
    }

    @OnClose
    public void onClose(Session session){
        log.info("用户退出 (from LUServer)");
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
