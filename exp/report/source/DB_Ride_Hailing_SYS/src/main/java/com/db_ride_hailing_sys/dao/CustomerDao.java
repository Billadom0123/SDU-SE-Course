package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */

public interface CustomerDao extends BaseMapper<Customer> {

    @Select("select id from customer where user_id = #{user_id} and deleted=0")
    Long queryCustomerIdByUID(@Param("user_id")Long user_ID);


}
