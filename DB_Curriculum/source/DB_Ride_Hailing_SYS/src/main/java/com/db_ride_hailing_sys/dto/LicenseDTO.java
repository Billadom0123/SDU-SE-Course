package com.db_ride_hailing_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDTO {
    private Long id;//司机的用户id
    private String license;

}
