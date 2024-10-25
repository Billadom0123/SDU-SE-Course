package com.db_ride_hailing_sys.constant;

public class LogConstant {
    public static String ORDER_DISPATCHING = "正在准备分配订单,订单id:";
    public static String ORDER_DISPATCH_FAIL = "派发订单失败";
    public static String CLUSTER_SIZE = "当前聚簇中司机数量:";
    public static String DRIVER_REJECT_LIST="拒绝的列表为:";
    public static String DISPATCH_DRIVERID = "分配司机id:";
    public static String DISPATCH_REQUEST = "将订单";
    public static String DISPATCH_DRIVER = "分配给司机";

    public static String logFilePath = "C:\\Users\\Administrator\\RideHailingDispatchOrder.txt";

    public static String CustomerTokenFilePath = "C:\\Users\\Administrator\\Desktop\\大二下\\数据库课设\\DB_Curriculum\\jmeter_data\\CustomerToken.txt";

    public static String DriverTokenFilePath = "C:\\Users\\Administrator\\Desktop\\大二下\\数据库课设\\DB_Curriculum\\jmeter_data\\DriverToken.txt";
}
