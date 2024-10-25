package com.db_ride_hailing_sys.dto;

import lombok.Data;

@Data
public class DriverDTO {
    private Long id;//司机用户id
    private Double curX;
    private Double curY;
}
