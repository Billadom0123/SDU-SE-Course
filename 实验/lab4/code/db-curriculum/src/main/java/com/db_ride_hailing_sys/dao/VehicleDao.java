package com.db_ride_hailing_sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.db_ride_hailing_sys.entity.Vehicle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface VehicleDao extends BaseMapper<Vehicle> {
    @Select("select count(*) from vehicle where " +
            "number = #{number} and deleted=0")
    Integer queryHasExistVehicle(@Param("number")String number);

    @Select("select id from vehicle where number = #{number} and deleted=0")
    Long queryVidByNumber(@Param("number")String number);

    @Select("select * from vehicle where id=#{vehicle_id} and deleted=0")
    Vehicle queryVehicleById(@Param("vehicle_id")Long vehicle_id);

}
