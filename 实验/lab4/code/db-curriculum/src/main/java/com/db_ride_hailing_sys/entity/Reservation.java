package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("reservation")
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long requestId;

    private LocalDateTime reserveTime;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;
}
