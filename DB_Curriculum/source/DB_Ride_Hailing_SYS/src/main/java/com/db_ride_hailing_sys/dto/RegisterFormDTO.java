package com.db_ride_hailing_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装注册请求
 * 要求必须填写手机号，验证码，密码
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFormDTO {
    private String name;
    private String phone;
    private String code;
    private String password;

    private Long role_id;
}
