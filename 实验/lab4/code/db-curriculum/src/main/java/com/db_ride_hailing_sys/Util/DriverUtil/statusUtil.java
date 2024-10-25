package com.db_ride_hailing_sys.Util.DriverUtil;

import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverOrder;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverRunning;

public class statusUtil {
    private static Logger logger = LoggerFactory.getLogger(statusUtil.class);
    public static DriverInfo changeStatus(Long id,Boolean idle){
        DriverInfo driverInfo = DriverStatus.get(id);
        if(driverInfo==null){
            return null;
        }
        driverInfo.setIdle(idle);
//        DriverStatus.put(id,driverInfo);
        return driverInfo;
    }

    public static long getIdleCount(){
        if(DriverStatus.size() == 0){
            return 0;
        }
        int count = 0;
        for (DriverInfo driverInfo : DriverStatus.values()) {
            if(driverInfo.getIdle()){
                count++;
            }
        }
        return count;
    }

    //更新司机接受订单的列表后用来判断司机当前应该处于何种状态
    //true:空闲 false:忙碌
    public static boolean judgeStatus(Long driverId){
        //如果这个司机当前已经接到乘客了，无论删哪一单都是忙碌(乘客在被接到之后也不能取消订单了)
        if(driverRunning.get(driverId)!=null){
            return false;
        }
        List<RequestInfo> reqList = driverOrder.get(driverId);
        if(reqList==null||reqList.isEmpty()){
            return true;
        }
//        logger.debug(reqList.toString());
        //统计是否有实时单
        ArrayList<RequestInfo> current = new ArrayList<RequestInfo>();
        for (RequestInfo requestInfo : reqList) {
            if(!requestInfo.getIs_reservation()){
                current.add(requestInfo);
                break;
            }
        }
        //如果没有实时单，那么处于空闲,有实时单,忙碌
        return current.size() == 0;
    }
}
