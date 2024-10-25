package com.db_ride_hailing_sys.EntityInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Location {
    private Long id;//用户的id
    private Double curX;
    private Double curY;
}
