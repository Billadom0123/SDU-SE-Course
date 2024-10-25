package com.db_ride_hailing_sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.Util.LogUtil.logUtil;
import com.db_ride_hailing_sys.Util.RegexUtil.RegexUtil;
import com.db_ride_hailing_sys.Util.UserUtil.UserHolder;
import com.db_ride_hailing_sys.service.IUserService;
import com.db_ride_hailing_sys.dao.CustomerDao;
import com.db_ride_hailing_sys.dao.DriverDao;
import com.db_ride_hailing_sys.dao.UserDao;
import com.db_ride_hailing_sys.dto.*;
import com.db_ride_hailing_sys.entity.Customer;
import com.db_ride_hailing_sys.entity.Driver;
import com.db_ride_hailing_sys.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.db_ride_hailing_sys.Util.LogUtil.logUtil.recordLog;
import static com.db_ride_hailing_sys.constant.BusinessConstant.Customer_Role;
import static com.db_ride_hailing_sys.constant.LogConstant.CustomerTokenFilePath;
import static com.db_ride_hailing_sys.constant.LogConstant.DriverTokenFilePath;
import static com.db_ride_hailing_sys.constant.RedisConstant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private DriverDao driverDao;

    //生成验证码
    @Override
    public Result generateCheckCode(String phone) {
        //校验手机号
        if(RegexUtil.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误!");
        }
        //满足正则表达式，生成验证码
        String code = RandomUtil.randomNumbers(6);
        //保存验证码到redis
        stringRedisTemplate.opsForValue().set(Check_CODE_KEY + phone,code,Check_CODE_TTL, TimeUnit.MINUTES);

        //发送验证码,打印日志模拟
        log.debug("发送短信验证码:"+code);
        return Result.ok();
    }

    //用户注册
    @Override
    public Result register(RegisterFormDTO regForm) {
        //因为获取验证码的时候确保手机号合法，不用再校验
        //即便伪造请求更改手机号，也会因为在redis中查不到对应的验证码而报告错误
        String name = regForm.getName();
        String code = regForm.getCode();
        String password = regForm.getPassword();
        String phone = regForm.getPhone();
        Long roleId = regForm.getRole_id();
        //1.查询数据库 查询是否已经注册过同用户类型的账号
        //先保证phone不为空并且roleId不为空
        if(null==phone || null==roleId || null == name || null == code || null == password){
            return Result.fail("表单项不完整");
        }
        Integer count = userDao.queryExistReg(roleId, phone);
        if(count>0){
            return Result.fail("已经存在该类型的账号!");
        }
        //2.检查密码是否符合格式 仅允许大小写字母以及数字出现
        String regex = "^[a-z0-9A-Z]+$";
        if(!password.matches(regex)){
            return Result.fail("密码格式错误,仅允许大小写字母以及数字");
        }
        if(password.length()>20||password.length()<6){
            return Result.fail("密码长度错误,仅允许6到20位的密码");
        }
        //3.查询redis是否存在当前手机号下的验证码
        String tCode = queryRedisCheckCode(phone);
        if(!code.equals(tCode)){
            return Result.fail("验证码错误!");
        }
        regCreateUser(name,phone,SecureUtil.md5(password),roleId);//SecureUtil.md5(password)
        return Result.ok();
    }

    //用户登陆
    @Override
    public Result login(LoginFormDTO loginForm) {
        String code = loginForm.getCode();
        String password = loginForm.getPassword();
        String phone = loginForm.getPhone();
        Long roleId = loginForm.getRole_id();
        //校验手机号
        if(RegexUtil.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误!");
        }
        //检查密码是否非空，密码非空走密码验证
        if(null!=password&&!password.equals("")){
            Integer count = userDao.queryLogin(roleId, phone, SecureUtil.md5(password));
            if(roleId==null){
                return Result.fail("请选择用户类型");
            }
            if(count<1){
                return Result.fail("密码错误");
            }
        }
        else{
            //密码空，走验证码验证
            String tCode = queryRedisCheckCode(phone);
            if(!code.equals(tCode)){
                return Result.fail("验证码错误");
            }
        }
        //两项中一项成功，保存token到redis，返回token到客户端
        User user = userDao.queryUserByRoleAndPhone(roleId, phone);
        Long userId = user.getId();
        String token = generateTokenInRedis(phone, userId);
        //记录token
        if(roleId==Customer_Role){
            recordLog(token+'\n',CustomerTokenFilePath,true);
        }
        else {
            recordLog(token + '\n', DriverTokenFilePath, true);
        }
        UserHolder.saveUser(BeanUtil.copyProperties(user, UserDTO.class));
        return Result.ok(createAuthentication(token,userId.toString(),user.getName()));
    }

    //redis查询验证码
    private String queryRedisCheckCode(String phone){
        return stringRedisTemplate.opsForValue().get(Check_CODE_KEY + phone);
    }

    //用户注册保存数据库
    private void regCreateUser(String name,String phone,String password,Long roleId){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole_id(roleId);
        save(user);
        if(roleId == 1L){
            regCreateCustomer(phone,roleId);
        }
        if(roleId == 2L){
            regCreateDriver(phone,roleId);
        }
    }

    private void regCreateCustomer(String phone,Long roleId){
        Long uid = userDao.queryIdByRoleAndPhone(roleId, phone);
        Customer customer = new Customer();
        customer.setUser_id(uid);
        customerDao.insert(customer);
    }

    private void regCreateDriver(String phone,Long roleId){
        Long uid = userDao.queryIdByRoleAndPhone(roleId, phone);
        Driver driver = new Driver();
        driver.setUser_id(uid);
        driverDao.insert(driver);
    }

    //为登陆的用户在redis生成token
    private String generateTokenInRedis(String phone,Long userId){
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY+token,
                String.valueOf(userId),
                LOGIN_USER_TTL,
                TimeUnit.SECONDS
                );
        return token;
    }

    //为登陆用户创建凭证信息
    private UserAuthenticationDTO createAuthentication(String token,String userId,String userName){
        UserAuthenticationDTO uRes = new UserAuthenticationDTO();
        uRes.setToken(token);
        uRes.setName(userName);
        uRes.setId(userId);
        return uRes;
    }
}
