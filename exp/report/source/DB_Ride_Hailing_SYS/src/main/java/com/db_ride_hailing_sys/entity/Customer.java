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
@TableName("customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Double carpool_rate;

    private Double remain;

    private Long user_id;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
