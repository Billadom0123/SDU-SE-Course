package com.db_ride_hailing_sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.db_ride_hailing_sys.entity.Instead;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface InsteadDao extends BaseMapper<Instead> {
    @Select("select count(*) from instead where request_id = #{reqId} and deleted=0")
    Integer queryCountByReqId(@Param("reqId") Long reqId);


    @Update("update instead set deleted =1 where request_id = #{reqId}")
    void logicDeleteInsteadByReqId(@Param("reqId")Long reqId);

    @Select("select customer_phone from instead where request_id = #{reqId} and deleted=0")
    String queryCustomerPhoneByReqId(@Param("reqId")Long reqId);
}


