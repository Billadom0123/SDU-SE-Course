package com.db_ride_hailing_sys.algorithm.DBSCAN.Constant;

import org.gavaghan.geodesy.Ellipsoid;

public abstract class DBSCAN_ARGS_CONSTANT {
    /**
     * Just in case the all-args constructor of the DBSCAN_ARGS was not used
     */
    public static final double defaultEps = 1.0;
    public static final long defaultMinPts = 1L;
    public static final Ellipsoid defaultEllipsoid = Ellipsoid.WGS84;
}
