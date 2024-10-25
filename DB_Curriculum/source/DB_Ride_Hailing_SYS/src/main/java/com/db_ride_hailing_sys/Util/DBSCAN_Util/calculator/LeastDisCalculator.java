package com.db_ride_hailing_sys.Util.DBSCAN_Util.calculator;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Geo_Distance;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.gavaghan.geodesy.Ellipsoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeastDisCalculator extends Geo_Distance {

    public static final Logger log = LoggerFactory.getLogger(LeastDisCalculator.class);

    public LeastDisCalculator(Ellipsoid ellipsoid) {
        super(ellipsoid);
    }

    public Cluster getLeastDistanceCluster(Point point, List<Cluster> cl){
        double min,dis;min = Double.MAX_VALUE;
        int minIndex=-1;
        for (int i = 0; i < cl.size(); i++) {
            dis = calculateDistance(point,cl.get(i).getKey());
            if(dis<min){
                min = dis;
                minIndex = i;
            }
        }
        return minIndex==-1?null:cl.get(minIndex);
    }

    public Cluster getKthLeastDistanceCluster(Point point,List<Cluster> cl,int k){
        int size = cl.size();
        if(k>size){
            log.debug("总共只有"+size+"个聚簇");
            return null;
        }
        if(k<=0){
            log.debug("k>0");
            return null;
        }
        ArrayList<clusterDistance> distances = new ArrayList<>(size);
        for (Cluster cluster : cl) {
            double distance = calculateDistance(point,cluster.getKey());
            distances.add(new clusterDistance(cluster,distance));
        }
        Collections.sort(distances);
        return distances.get(k-1).cluster;
    }

    @AllArgsConstructor
    @Data
    private static class clusterDistance implements Comparable<clusterDistance>{
        private Cluster cluster;
        private double distance;

        @Override
        public int compareTo(clusterDistance o) {
            return Double.compare(this.distance,o.distance);
        }
    }
}
