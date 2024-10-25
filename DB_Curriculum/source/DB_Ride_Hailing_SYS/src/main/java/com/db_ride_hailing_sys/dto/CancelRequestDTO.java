package com.db_ride_hailing_sys.dto;

import lombok.Data;

import java.util.List;

@Data
public class CancelRequestDTO {
    private Long id;//乘客的用户id
    private List<Long> requestList;
}
