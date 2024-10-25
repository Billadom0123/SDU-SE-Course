package com.db_ride_hailing_sys.EntityInfo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestInfo {
    private Long reqId;
    private Boolean is_reservation;
    private Boolean is_instead;
}
