package com.db_ride_hailing_sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.db_ride_hailing_sys.entity.Reservation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ReservationDao extends BaseMapper<Reservation> {
    @Select("select count(*) from reservation where request_id = #{reqId} and deleted=0")
    Integer queryCountByReqId(@Param("reqId")Long reqId);

    @Update("update reservation set deleted=1 where request_id = #{reqId}")
    void logicDeleteReservationByReqId(@Param("reqId")Long reqId);

    @Select("select * from reservation where request_id = #{reqId} and deleted=0")
    Reservation queryReservationByRequestId(@Param("reqId")Long reqId);
}
