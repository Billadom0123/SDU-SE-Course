package com.db_ride_hailing_sys.Util.DBSCAN_Util;

import com.db_ride_hailing_sys.Util.DBSCAN_Util.calculator.DBSCAN_driverCalculator;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Args.DBSCAN_ARGS;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.DBSCAN_Result;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;

public class DBSCAN_driverUtil {
    public static DBSCAN_Result DBSCAN_driver(Object[][] dataset, DistanceStrategy distanceStrategy, DBSCAN_ARGS dbscan_args) throws DBSCAN_POINT_EXCEPTION {
        DBSCAN_driverCalculator dbscanDriverCalculator = new DBSCAN_driverCalculator(distanceStrategy, dbscan_args);
        dbscanDriverCalculator.calculateClusters(dataset);
        return new DBSCAN_Result(dbscanDriverCalculator.getNoises(),dbscanDriverCalculator.getClusters());
    }
}
