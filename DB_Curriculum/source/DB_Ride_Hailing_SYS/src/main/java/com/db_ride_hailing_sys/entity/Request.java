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
@TableName("request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long customerId;

    private LocalDateTime requestTime;

    private LocalDateTime responseTime;

    private Integer priority;

    private Boolean carpool;

    private Boolean accepted;

    private Double startX;

    private Double startY;

    private String startName;

    private Double desX;

    private Double desY;

    private String desName;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
