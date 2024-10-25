package com.db_ride_hailing_sys.dto;

import lombok.Data;

@Data
public class CommitOrderDTO {
    Long id;//司机用户id
    Long reqId;
    Double endX;
    Double endY;
}
