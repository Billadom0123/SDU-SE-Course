package com.db_ride_hailing_sys.algorithm.DBSCAN.Entity;

import java.util.List;

import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_COLLECTIONS_CONSTANT.LIST_HEAD;

public class Cluster {
    List<Point> cq;
    Point key;

    public Cluster(List<Point> cq, Point key) {
        this.cq = cq;
        this.key = key;
    }

    public boolean isEmpty(){
        return cq.isEmpty();
    }

    public int ClusterSize(){
        return cq.size();
    }

    public boolean contains(Point p){
        return cq.contains(p);
    }

    public boolean offer(Point p){
        return cq.add(p);

    }

    public Point poll(){
        return cq.remove(LIST_HEAD);
    }

    public Point head(){
        return cq.get(LIST_HEAD);
    }

    public boolean remove(Point p){
        return cq.remove(p);
    }

    public List<Point> getCq() {
        return cq;
    }

    public void setCq(List<Point> cq) {
        this.cq = cq;
    }

    public Point getKey() {
        return key;
    }

    public void setKey(Point key) {
        this.key = key;
    }
}
