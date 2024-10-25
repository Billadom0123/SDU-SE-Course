package com.db_ride_hailing_sys.algorithm.DBSCAN.Entity;

import java.util.List;

public class DBSCAN_Result {
    private List<Point> noises;
    private List<Cluster> clusters;

    public DBSCAN_Result(List<Point> noises, List<Cluster> clusters) {
        this.noises = noises;
        this.clusters = clusters;
    }

    public List<Point> getNoises() {
        return noises;
    }

    public void setNoises(List<Point> noises) {
        this.noises = noises;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }
}
