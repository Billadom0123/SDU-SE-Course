package com.db_ride_hailing_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationDTO {
    private String token;
    private String id; //前端js长整型有精度丢失，只能改string
    private String name;
}
