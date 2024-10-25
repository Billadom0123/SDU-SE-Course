package com.db_ride_hailing_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginFormDTO {
    private String phone;
    private String code;
    private String password;
    private Long role_id;
}
