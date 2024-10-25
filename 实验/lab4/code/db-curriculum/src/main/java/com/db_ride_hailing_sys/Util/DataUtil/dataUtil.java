package com.db_ride_hailing_sys.Util.DataUtil;

import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.entity.Request;
import com.db_ride_hailing_sys.service.impl.DriverServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.db_ride_hailing_sys.service.impl.DriverServiceImpl.DriverStatus;

public class dataUtil {
    private static Logger logger = LoggerFactory.getLogger(dataUtil.class);
    public static Object[][] driverDataTranslate(){
        ArrayList<DriverInfo> idleDriver = new ArrayList<DriverInfo>();
        logger.debug(DriverStatus.toString());
        for (DriverInfo driverInfo : DriverStatus.values()) {
            if(driverInfo.getIdle()){
                idleDriver.add(driverInfo);
            }
        }
        Object[][] res = new Object[idleDriver.size()][3];
        DriverInfo driverInfo;Long id;Double curX,curY;
        for (int i = 0; i < idleDriver.size(); i++) {
            driverInfo = idleDriver.get(i);
            id = driverInfo.getId();
            curX = driverInfo.getCurX();
            curY = driverInfo.getCurY();
            res[i][0] = curX;
            res[i][1] = curY;
            res[i][2] = id; //这里原先res[i][2] = (double)id是有long转double的精度损失的
        }
        return res;
    }

    public static Point pointTransfer(Request request){
        Double startX = request.getStartX();
        Double startY = request.getStartY();
        return new Point(startX,startY);
    }
}
