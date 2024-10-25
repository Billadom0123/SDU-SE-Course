package com.db_ride_hailing_sys.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteOrderDTO {
    private Long id;//司机用户id
    private List<Long> requestList;
}
