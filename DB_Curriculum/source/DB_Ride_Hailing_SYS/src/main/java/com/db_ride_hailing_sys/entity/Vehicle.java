package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vehicle")
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String number;

    private String color;

    private String type;

    private String brand;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;
}
