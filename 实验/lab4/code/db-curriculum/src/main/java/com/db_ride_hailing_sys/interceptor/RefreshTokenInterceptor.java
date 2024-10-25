package com.db_ride_hailing_sys.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.db_ride_hailing_sys.Util.UserUtil.UserHolder;
import com.db_ride_hailing_sys.dao.UserDao;
import com.db_ride_hailing_sys.dto.UserDTO;
import com.db_ride_hailing_sys.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.db_ride_hailing_sys.constant.RedisConstant.LOGIN_USER_KEY;
import static com.db_ride_hailing_sys.constant.RedisConstant.LOGIN_USER_TTL;

//这个拦截器纯做token刷新的,一律放行，随后再来个登陆的拦截器做登陆拦截
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;


    private UserDao userDao = SpringUtil.getBean(UserDao.class);

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
//        System.out.println(token);
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 2.基于TOKEN获取redis中的用户
        String key  = LOGIN_USER_KEY + token;
        String id = stringRedisTemplate.opsForValue().get(key);
        // 3.判断用户是否存在
        if(null==id){
            return true;
        }
        Long uid = Long.valueOf(id);
        //根据id查询数据库得到user信息
        User user = userDao.queryById(uid);
        // 5.将查询到的hash数据转为UserDTO
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 6.存在，保存用户信息到 ThreadLocal
        UserHolder.saveUser(userDTO);
        // 7.刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 8.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
