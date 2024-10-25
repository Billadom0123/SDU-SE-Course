package com.db_ride_hailing_sys.controller;


import com.db_ride_hailing_sys.dto.LoginFormDTO;
import com.db_ride_hailing_sys.dto.PhoneDTO;
import com.db_ride_hailing_sys.dto.RegisterFormDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    //登陆或注册时的验证码
    //注册必须要接一次验证码，登陆可以通过手机号+密码或者手机号+验证码登陆
    @PostMapping("checkCode")
    public Result checkCode(@RequestBody PhoneDTO phoneDTO){
        return userService.generateCheckCode(phoneDTO.getPhone());
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterFormDTO regForm){
        return userService.register(regForm);
    }

    @PostMapping("login")
    public Result login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    @GetMapping("getTest")
    public Result getTest(){
        System.out.println("ping pong test");
        return Result.ok();
    }
}

