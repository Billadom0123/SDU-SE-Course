package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.Comment;
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

public interface CommentDao extends BaseMapper<Comment> {

    @Select("select * from comment where order_id=#{orderId} and deleted=0")
    Comment queryCommentByOrderId(@Param("orderId")Long orderId);

}
