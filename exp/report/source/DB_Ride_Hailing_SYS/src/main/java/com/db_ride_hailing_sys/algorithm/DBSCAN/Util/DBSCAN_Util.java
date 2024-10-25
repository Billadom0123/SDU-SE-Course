package com.db_ride_hailing_sys.algorithm.DBSCAN.Util;


import com.db_ride_hailing_sys.algorithm.DBSCAN.Args.DBSCAN_ARGS;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.DBSCAN_Result;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;

public class DBSCAN_Util {
    public static DBSCAN_Result DBSCAN(Object[][] dataset, DistanceStrategy distanceStrategy, DBSCAN_ARGS dbscan_args) throws DBSCAN_POINT_EXCEPTION {
        DBSCAN_Calculator dbscanCalculator = new DBSCAN_Calculator(distanceStrategy, dbscan_args);
        dbscanCalculator.calculateClusters(dataset);
        return new DBSCAN_Result(dbscanCalculator.getNoises(),dbscanCalculator.getClusters());
    }
}
