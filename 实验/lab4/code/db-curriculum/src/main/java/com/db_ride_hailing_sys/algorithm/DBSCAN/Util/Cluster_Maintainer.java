package com.db_ride_hailing_sys.algorithm.DBSCAN.Util;

import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;

import java.util.List;

public class Cluster_Maintainer {
    public static List<Cluster> clusters = null;
    public static Long queueFlow = null;

    public static void initMaintainer(List<Cluster> l,Long flow){
        clusters=l;
        queueFlow = flow;
    }



    public static void cleanMaintainer(){
        if(clusters.isEmpty()){
            return;
        }
        clusters.clear();
    }
}
