package com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity;

import lombok.Data;

@Data
public class StringWSRequestMessageForDriver {
    private String id;
    private String priority;
    private String startX;
    private String startY;
    private String startName;
    private String desX;
    private String desY;
    private String desName;
    private String phone;
    private String appointmentTime;
}
