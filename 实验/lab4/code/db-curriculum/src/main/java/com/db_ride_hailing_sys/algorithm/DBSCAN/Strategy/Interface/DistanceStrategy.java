package com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;

public interface DistanceStrategy {
    double calculateDistance(Point p1, Point p2);
}
