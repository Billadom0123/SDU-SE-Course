package com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringWSDriverMessageForCustomer {
    private String reqId;
    private String licensePlateNumber;
    private String carType;
    private String carColor;
    private String carBrand;
    private String driverUserId;
    private String driverName;
    private String driverPhone;
    private String driverCredit;
    private String cancelInfo;//如果当前司机取消订单，通知乘客当前司机已经取消订单，为您更换司机
}
