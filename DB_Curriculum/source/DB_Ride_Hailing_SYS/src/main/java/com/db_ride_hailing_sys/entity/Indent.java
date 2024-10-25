package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("indent")
public class Indent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long requestId;

    private Long driverId;

    //司机接单时间
    private LocalDateTime receiveTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double endX;

    private Double endY;

    private Double distance;

    private Double price;

    private Integer priority;

    //当前车牌号
    private String number;

    @TableLogic
    private Integer deleted;

    private Integer driverDeleted;

    private Integer customerDeleted;

    @Version
    private Integer version;


}
