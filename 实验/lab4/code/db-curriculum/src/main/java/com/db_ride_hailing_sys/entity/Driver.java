package com.db_ride_hailing_sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
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
@TableName("driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Double comment_rate;

    private Double income;

    private Long user_id;

    private String license;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
