package com.db_ride_hailing_sys.Util.LogUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.db_ride_hailing_sys.Util.DBSCAN_Util.clusterUtil.getClustersSize;
import static com.db_ride_hailing_sys.constant.LogConstant.*;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverReject;

public class logUtil {

    public static String logTimePrefixSuffix(String log){
        return LocalDateTime.now().toString()+" "+log+'\n';
    }

    /**
     * 记录日志
     * @param log 日志内容
     * @param path 日志文件路径
     * @param append 是否追加(不追加默认覆盖)
     */
    public static void recordLog(String log,String path,Boolean append){
        try {
            FileWriter fileWriter = new FileWriter(path,append);
            fileWriter.write(log);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String ORDER_DISPATCHING_Info(Long reqId){
        return ORDER_DISPATCHING+reqId;
    }

    public static String ORDER_DISPATCH_FAIL_Info(){
        return ORDER_DISPATCH_FAIL;
    }

    public static String CLUSTER_SIZE_Info(){
        return CLUSTER_SIZE+getClustersSize();
    }

    public static String DRIVER_REJECT_LIST_Info(Long driverId){
        return "司机"+driverId+DRIVER_REJECT_LIST+((driverReject.get(driverId)==null)?"":driverReject.get(driverId).toString());
    }

    public static String DISPATCH_DRIVERID_Info(Long driverId){
        return DISPATCH_DRIVERID+driverId;
    }

    public static String DISPATCH_RESULT(Long reqId,Long driverId){
        return DISPATCH_REQUEST+":"+reqId+DISPATCH_DRIVER+":"+driverId;
    }
}
