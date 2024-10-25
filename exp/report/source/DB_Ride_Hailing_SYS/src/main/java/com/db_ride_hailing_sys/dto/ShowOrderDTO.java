package com.db_ride_hailing_sys.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowOrderDTO {
    private String indentId;//精度问题
    private String reqId;
    private LocalDateTime responseTime;//乘客发起订单时间
    private LocalDateTime receiveTime;//司机接单时间
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime appointTime;
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
    private String startName;
    private String endName;
    private Double price;
    private String driverName;
    private String customerName;
    private String number;//车牌号
    private String driverPhone;
    private String customerPhone;
}
