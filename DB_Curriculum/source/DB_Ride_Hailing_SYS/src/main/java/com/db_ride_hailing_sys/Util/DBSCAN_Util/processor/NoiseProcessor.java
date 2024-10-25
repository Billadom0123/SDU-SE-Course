package com.db_ride_hailing_sys.Util.DBSCAN_Util.processor;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.DBSCAN_Result;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Geo_Distance;
import org.gavaghan.geodesy.Ellipsoid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NoiseProcessor extends Geo_Distance {
    public NoiseProcessor(Ellipsoid ellipsoid) {
        super(ellipsoid);
    }

    public List<Cluster> processNoises(DBSCAN_Result dbscanResult){
        List<Point> noises = dbscanResult.getNoises();
        List<Cluster> cl = dbscanResult.getClusters();
        if(cl==null||cl.isEmpty()){
            //如果全是噪声点而没有聚簇，将所有噪声点划为一个聚簇
            Cluster cluster = new Cluster(new LinkedList<Point>(),noises.get(0));
            for (Point noise : noises) {
                cluster.offer(noise);
            }
            if(cl==null){
                cl = new ArrayList<Cluster>();
            }
            cl.add(cluster);
            return cl;
        }
        double dis;
        //对于每个噪声点
        for (Point noise : noises) {
            //查询距离key最近的聚簇
            double min = Double.MAX_VALUE;
            int minIndex = -1;
            for (int i = 0; i < cl.size(); i++) {
                Cluster cluster = cl.get(i);
                dis = calculateDistance(noise,cluster.getKey());
                if(dis<min){
                    min = dis;
                    minIndex = i;
                }
            }
            //加入该cluster
            cl.get(minIndex).offer(noise);
        }
        return cl;
    }
}
