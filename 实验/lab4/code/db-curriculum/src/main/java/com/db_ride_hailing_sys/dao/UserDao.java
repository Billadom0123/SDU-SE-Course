package com.db_ride_hailing_sys.dao;

import com.db_ride_hailing_sys.entity.User;
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

public interface UserDao extends BaseMapper<User> {

    //注册时查询是否已经注册过同用户类型的账号
    @Select("select count(*) from " +
            "user " +
            "where " +
            "role_id = #{role_id} and " +
            "phone = #{phone} " +
            "and deleted=0")
    Integer queryExistReg(@Param("role_id")Long role_id,@Param("phone")String phone);

    //查询手机号，密码，用户类型是否对应存在
    @Select("select count(*) from " +
            "user " +
            "where " +
            "role_id = #{role_id} and " +
            "phone = #{phone} and " +
            "password = #{password} " +
            "and deleted=0")
    Integer queryLogin(@Param("role_id")Long role_id,@Param("phone")String phone,@Param("password") String password);

    //
    @Select("select * from " +
            "user " +
            "where " +
            "role_id = #{role_id} and " +
            "phone = #{phone} " +
            "and deleted=0")
    User queryUserByRoleAndPhone(@Param("role_id")Long role_id,@Param("phone")String phone);

    @Select("select id from user where role_id = #{role_id} and phone = #{phone} and deleted=0")
    Long queryIdByRoleAndPhone(@Param("role_id")Long role_id,@Param("phone")String phone);

    @Select("select * from user where id = #{id} and deleted=0")
    User queryById(@Param("id")Long id);

    @Select("select role_id from user where id = #{userId} and deleted=0")
    Long queryRoleByUID(@Param("userId")Long userId);

    @Select("select phone from user where id = #{userId} and deleted = 0")
    String queryPhoneByUID(@Param("userId")Long userId);


}
