package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.Request;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */

public interface RequestDao extends BaseMapper<Request> {

    @Select("select count(*) from request where id = #{reqId} and customer_id = #{id} and deleted =0")
    Integer queryCountByRequestIdAndCustomerId(@Param("reqId")Long reqId,@Param("id")Long id);

    @Select("select * from request where id = #{reqId} and deleted=0")
    Request queryRequestIdByReqId(@Param("reqId")Long reqId);

    @Select("select * from request where id = #{reqId} and " +
            "(select count(*) from indent where request_id=#{reqId} and deleted=0)>0")
    Request queryExistingOrderRequestByReqId(@Param("reqId")Long reqId);

    @Select("select customer_id from request where id = #{reqId} and deleted=0")
    Long queryCustomerIdByReqId(@Param("reqId")Long reqId);

    @Select("select count(*) from request where id = #{reqId} and deleted=0")
    Integer queryRequestCountByReqId(@Param("reqId")Long reqId);

    @Update("update request set accepted=#{accepted},response_time=#{response_time} where id = #{reqId} and deleted=0")
    void updateRequestByReqId(@Param("accepted")Boolean accepted, @Param("response_time")LocalDateTime response_time,@Param("reqId")Long reqId);

    //逻辑删除请求
    @Update("update request set accepted=#{accepted},deleted=1 where id=#{reqId}")
    void logicDeleteRequestByReqId(@Param("accepted")Boolean accepted,@Param("reqId")Long reqId);

    @Select("select * from request where accepted=true and customer_id=#{id} and deleted=0")
    List<Request> queryAcceptedRequestByCustomerId(@Param("id")Long id);
}
