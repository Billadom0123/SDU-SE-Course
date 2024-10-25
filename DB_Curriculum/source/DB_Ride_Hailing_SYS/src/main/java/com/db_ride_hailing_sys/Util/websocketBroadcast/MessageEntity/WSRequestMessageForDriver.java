package com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity;

import com.db_ride_hailing_sys.entity.Request;
import lombok.Data;

//由于前端JS精度问题，该类已经弃用，替代的实体类在同包名下的StringWSRequestMessageForDriver.java文件中
@Data
public class WSRequestMessageForDriver {
    Request request;
    String phone;
}
