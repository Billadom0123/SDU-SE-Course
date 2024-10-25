package com.db_ride_hailing_sys.EntityInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverInfo {
    private Long id;//这个是用户id,不是driverId
    private Double curX;
    private Double curY;
    private Long credit;
    private Boolean idle;//是否空闲,是为空闲,否为忙碌
}
