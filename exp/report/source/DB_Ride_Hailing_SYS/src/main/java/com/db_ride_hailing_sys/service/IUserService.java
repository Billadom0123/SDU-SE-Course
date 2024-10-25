package com.db_ride_hailing_sys.service;

import com.db_ride_hailing_sys.dto.LoginFormDTO;
import com.db_ride_hailing_sys.dto.RegisterFormDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
public interface IUserService extends IService<User> {

    Result register(RegisterFormDTO regForm);

    Result generateCheckCode(String phone);

    Result login(LoginFormDTO loginForm);
}
