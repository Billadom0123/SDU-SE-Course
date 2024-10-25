package com.db_ride_hailing_sys.Util.RequestUtil;

import cn.hutool.extra.spring.SpringUtil;
import com.db_ride_hailing_sys.EntityInfo.RequestInfo;
import com.db_ride_hailing_sys.dao.InsteadDao;
import com.db_ride_hailing_sys.dao.ReservationDao;

import java.util.List;

import static com.db_ride_hailing_sys.service.impl.IndentServiceImpl.driverOrder;

public class requestUtil {

    private static ReservationDao reservationDao = SpringUtil.getBean(ReservationDao.class);

    private static InsteadDao insteadDao = SpringUtil.getBean(InsteadDao.class);

    public static boolean is_Reservation(Long reqId){
        Integer reserveCount = reservationDao.queryCountByReqId(reqId);
        if(reserveCount==null||reserveCount==0){
            return false;
        }
        return true;
    }

    public static boolean is_Instead(Long reqId){
        Integer insteadCount = insteadDao.queryCountByReqId(reqId);
        if(insteadCount==null||insteadCount==0){
            return false;
        }
        return true;
    }

    public static RequestInfo getRequestInfo(Long reqId){
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setReqId(reqId);
        requestInfo.setIs_reservation(is_Reservation(reqId));
        requestInfo.setIs_instead(is_Instead(reqId));
        return requestInfo;
    }

    public static boolean deleteOrder(Long reqId,Long id){
        List<RequestInfo> requestInfos = driverOrder.get(id);
        for (int i = 0; i < requestInfos.size(); i++) {
            RequestInfo requestInfo = requestInfos.get(i);
            if(requestInfo.getReqId().equals(reqId)){
                requestInfos.remove(i);
                return true;
            }
        }
        return false;
    }

}
