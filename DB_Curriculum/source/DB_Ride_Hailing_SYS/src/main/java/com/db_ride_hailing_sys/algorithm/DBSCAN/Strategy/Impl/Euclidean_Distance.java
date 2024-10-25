package com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl;


import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;

/**
 * Strategy for calculating the Euclidean Distance between two Points/Vectors
 */
public class Euclidean_Distance implements DistanceStrategy {
    public Euclidean_Distance() {
    }

    @Override
    public double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(
                Math.pow((p1.getX())- p2.getX(),2)
                +
                Math.pow((p1.getY()- p2.getY()),2)
        );
    }
}
