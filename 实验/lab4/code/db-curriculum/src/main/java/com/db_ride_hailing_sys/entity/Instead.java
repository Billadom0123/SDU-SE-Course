package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("instead")
public class Instead implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long request_id;

    private String customer_phone;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;
}
