package com.db_ride_hailing_sys.algorithm.DBSCAN.Args;


import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;

import static com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Constant.ARGS_Constant.WRONG_EPS;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Constant.ARGS_Constant.WRONG_MINPTS;

public class DBSCAN_ARGS {
    /**
     * Euclidean Distance Radius
     */
    private double eps;
    /**
     * least neighbours to become a core point
     */
    private long minPts;

    public DBSCAN_ARGS(double eps, long minPts) throws DBSCAN_ARGS_EXCEPTION {
        if(eps<=0){
            throw new DBSCAN_ARGS_EXCEPTION(WRONG_EPS);
        }
        if(minPts<0){
            throw new DBSCAN_ARGS_EXCEPTION(WRONG_MINPTS);
        }
        this.eps = eps;
        this.minPts = minPts;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public long getMinPts() {
        return minPts;
    }

    public void setMinPts(long minPts) {
        this.minPts = minPts;
    }
}
