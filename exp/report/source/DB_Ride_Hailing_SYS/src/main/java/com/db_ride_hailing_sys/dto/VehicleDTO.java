package com.db_ride_hailing_sys.dto;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;//司机用户id
    private String number;
    private String color;
    private String type;
    private String brand;
}
