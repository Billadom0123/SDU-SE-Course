package com.db_ride_hailing_sys.Util.websocketBroadcast.BroadCast;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;

public class websocketBroadcast {
    private static final Logger log = LoggerFactory.getLogger(websocketBroadcast.class);
    public static void sendDispatchResult(Object obj, Session des){
        sendObject(obj,des);
        if(des!=null){
            log.info("向客户端{}发送订单结果{}",des.getId(),obj.toString());
        }
    }

    public static void sendObject(Object obj,Session des){
        if(des==null){
            log.debug("用户客户端session为空");
            return;
        }
        try{
            JSONObject jsonObject = JSONUtil.parseObj(obj);
            des.getBasicRemote().sendText(jsonObject.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendCancelRequestResult(String message,Session des){
        log.info("向客户端{}发送用户取消请求的结果",des.getId());
        sendMessage(message,des);
    }

    public static void sendMessage(String message,Session des){
        if(des==null){
            log.debug("乘客客户端session为空");
            return;
        }
        try {
            des.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
