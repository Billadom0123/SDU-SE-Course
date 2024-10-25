package com.db_ride_hailing_sys.constant;

public class RedisConstant {
    //验证码 + 手机号
    public static final String Check_CODE_KEY = "CheckCode:";
    //验证码过期时间
    public static final Long Check_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;
    public static final String IDLE_DRIVER = "idle driver";
    public static final String BUSY_IDLE_FLOW = "busy idle flow";
    public static final String STREAM_ORDER = "customer.request";
    public static final String ORDER_CONSUMER_GROUP = "g1";
    public static final String ORDER_CONSUMER = "c1";
}
