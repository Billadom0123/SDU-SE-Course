package com.db_ride_hailing_sys.Util.DBSCAN_Util.Entity;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;

public class DBSCAN_driverPoint extends Point {
    private long uId;

    public DBSCAN_driverPoint(double x, double y, long uId) {
        super(x, y);
        this.uId = uId;
    }

    public long getUId() {
        return uId;
    }

    public void setUId(long uId) {
        this.uId = uId;
    }
}
