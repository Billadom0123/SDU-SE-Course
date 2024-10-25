package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("belong")
public class Belong  implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long driverId;

    private Long vehicleId;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;
}
