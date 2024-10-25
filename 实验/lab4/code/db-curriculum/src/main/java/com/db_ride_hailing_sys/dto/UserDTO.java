package com.db_ride_hailing_sys.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String phone;
    private Long credit;
    private Long role_id;
}
