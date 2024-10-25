package com.db_ride_hailing_sys.dto;

import lombok.Data;

@Data
public class CommentOrderDTO {
    private Long id;//乘客用户id
    private Long reqId;//评价订单对应的请求id
    private Integer comment;//x星好评
    private String content;//评价内容
}
