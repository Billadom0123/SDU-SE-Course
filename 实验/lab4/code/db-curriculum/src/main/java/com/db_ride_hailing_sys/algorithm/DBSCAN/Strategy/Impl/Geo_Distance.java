package com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class Geo_Distance implements DistanceStrategy {
    private Ellipsoid ellipsoid;

    public Geo_Distance(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    @Override
    public double calculateDistance(Point p1, Point p2) {
        GlobalCoordinates source = new GlobalCoordinates(p1.getX(), p1.getY());
        GlobalCoordinates target = new GlobalCoordinates(p2.getX(), p2.getY());
        return getDistanceMeter(target,source);
    }

    private double getDistanceMeter(GlobalCoordinates source,GlobalCoordinates target){
        GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(this.ellipsoid, source, target);
        return geodeticCurve.getEllipsoidalDistance();
    }
}
