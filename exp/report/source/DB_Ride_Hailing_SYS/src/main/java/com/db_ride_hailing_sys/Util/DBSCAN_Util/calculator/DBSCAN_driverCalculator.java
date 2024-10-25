package com.db_ride_hailing_sys.Util.DBSCAN_Util.calculator;

import com.db_ride_hailing_sys.Util.DBSCAN_Util.Entity.DBSCAN_driverPoint;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Args.DBSCAN_ARGS;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Util.DBSCAN_Calculator;

import static com.db_ride_hailing_sys.constant.BusinessConstant.WRONG_DATASET;


public class DBSCAN_driverCalculator extends DBSCAN_Calculator {
    public DBSCAN_driverCalculator(DistanceStrategy distanceStrategy, DBSCAN_ARGS dbscan_args) {
        super(distanceStrategy, dbscan_args);
    }

    @Override
    public void setDataSet(Object[][] dataSet) throws DBSCAN_POINT_EXCEPTION {
        if(dataSet[0].length!=3){
            throw new DBSCAN_POINT_EXCEPTION(WRONG_DATASET);
        }
        for (Object[] data : dataSet) {
            points.add(
                    new DBSCAN_driverPoint(
                            (double)data[0],(double)data[1],(long)data[2]
                    )
            );
        }
        unvisited.addAll(points);
    }
}
