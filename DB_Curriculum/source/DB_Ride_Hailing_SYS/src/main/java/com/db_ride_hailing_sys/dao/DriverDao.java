package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.Driver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */

public interface DriverDao extends BaseMapper<Driver> {

    @Select("select license from driver where user_id = #{driver_id} and deleted=0")
    String queryLicenseByUid(@Param("driver_id") Long driver_id);

    @Select("select user_id from driver where id=#{driver_id} and deleted=0")
    Long queryUserIdByDriverId(@Param("driver_id")Long driver_id);

    @Update("update driver set license = #{license} where user_id = #{user_id} and deleted=0")
    void updateLicenseByUserId(@Param("license")String license,@Param("user_id")Long user_id);

}
