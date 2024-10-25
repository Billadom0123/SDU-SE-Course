package com.db_ride_hailing_sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.db_ride_hailing_sys.entity.Belong;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BelongDao extends BaseMapper<Belong> {

    @Select("select count(*) from belong where driver_id = #{driver_id} and deleted=0")
    Integer queryCountByDriver(@Param("driver_id") Long driver_id);

    @Select("select count(*) from belong,vehicle where " +
            "belong.vehicle_id = vehicle.id and " +
            "vehicle.number = #{number} " +
            "and belong.deleted=0 and vehicle.deleted=0" )
    Integer queryVehicleBelongCount(@Param("number")String number);

    @Select("select vehicle_id from belong where driver_id = #{id} and deleted=0")
    Long queryVehicleIdByDriverId(@Param("id")Long id);

}
