package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.Indent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

public interface IndentDao extends BaseMapper<Indent> {

    @Select("select request_id from indent where id = #{orderId} and deleted = 0")
    Long queryRequestIdByOrderId(@Param("id") Long orderId);

    @Select("select * from indent where request_id=#{reqId} and deleted=0")
    Indent queryOrderByReqId(@Param("reqId")Long reqId);



    @Update("update indent set deleted=1 where request_id = #{reqId}")
    void logicDeleteOrderByReqId(@Param("reqId")Long reqId);

    @Select("select count(*) from indent where id=#{order_id} and deleted=0")
    Integer queryCountByOrderId(@Param("order_id")Long orderId);

    @Select("select count(*) from indent where request_id=#{reqId} and driver_id = #{driverId} and deleted = 0")
    Integer queryCountByRequestIdAndDriverId(@Param("reqId")Long reqId,@Param("driverId")Long driverId);

    @Update("update indent set price = #{price},distance = #{distance},end_x = #{endX},end_y = #{endY},end_time = #{endTime} " +
            "where deleted=0 and request_id = #{reqId} and driver_id =#{driverId}")
    void updateOrderByRequestIdAndDriverId(@Param("price")Double price, @Param("distance")Double distance,
                                           @Param("endX")Double endX, @Param("endY")Double endY,
                                           @Param("endTime")LocalDateTime endTime,@Param("reqId")Long reqId,
                                           @Param("driverId")Long driverId);

    @Update("update indent set start_time=#{startTime} where deleted=0 and request_id = #{reqId}")
    void updateOrderStartTimeByReqId(@Param("startTime")LocalDateTime startTime,@Param("reqId")Long reqId);

    //当前订单没有被司机删除 同时已经完成
    @Select("select * from indent where driver_id=#{id} and deleted=0 and driver_deleted=0 and price IS NOT NULL")
    List<Indent> queryIndentByDriverId(@Param("id")Long id);

    //乘客删除订单定制  同时已经完成
    @Select("select * from indent where request_id=#{reqId} and deleted=0 and customer_deleted=0 and price IS NOT NULL")
    Indent queryOrderByReqIdandCustomer(@Param("reqId")Long reqId);

    @Update("update indent set customer_deleted = 1 where id=#{id}")
    void updateOrderCustomerDeleteById(@Param("id")Long id);

    @Update("update indent set driver_deleted=1 where id=#{id}")
    void updateOrderDriverDeleteById(@Param("id")Long id);

    @Select("select * from indent where " +
            "(select count(*) from request where request.id=#{reqId} and request.customer_id=#{id} and request.deleted=0)>0 " +
            "and indent.request_id=#{reqId} " +
            "and indent.deleted=0")
    Indent queryExistingIndentByCustomerIdAndRequestId(@Param("reqId")Long reqId,@Param("id")Long id);
}
