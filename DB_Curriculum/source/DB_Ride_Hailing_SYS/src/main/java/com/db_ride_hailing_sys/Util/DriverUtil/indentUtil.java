package com.db_ride_hailing_sys.Util.DriverUtil;

import com.db_ride_hailing_sys.EntityInfo.RequestInfo;

import java.util.ArrayList;
import java.util.List;

import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverOrder;
import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverReject;

public class indentUtil {
    public static void deleteDriverOrder(Long driverId,Long reqId){
        List<RequestInfo> requestInfos = driverOrder.get(driverId);
        for (int i = 0; i < requestInfos.size(); i++) {
            RequestInfo requestInfo = requestInfos.get(i);
            if(requestInfo.getReqId().equals(reqId)){
                requestInfos.remove(i);
                break;
            }
        }
    }

    public static void addToRejectList(Long driverId,Long reqId){
        List<Long> reject = driverReject.get(driverId);
        if(reject==null){
            reject = new ArrayList<>();
        }
        reject.add(reqId);
        driverReject.put(driverId,reject);
    }
}
