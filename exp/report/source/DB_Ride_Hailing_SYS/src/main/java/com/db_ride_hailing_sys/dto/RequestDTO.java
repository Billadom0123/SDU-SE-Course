package com.db_ride_hailing_sys.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDTO {
    private Long id;
    private Integer priority;
//    private Boolean carpool;
    private LocalDateTime request_time;
    private Double startX;
    private Double startY;
    private String start_name;
    private Double desX;
    private Double desY;
    private String des_name;
    private Boolean is_reservation;
    private LocalDateTime appointment_time;
    private Boolean is_instead;
    private String customer_phone;
}
