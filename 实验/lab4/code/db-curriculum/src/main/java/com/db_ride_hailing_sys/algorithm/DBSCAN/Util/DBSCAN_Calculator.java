package com.db_ride_hailing_sys.algorithm.DBSCAN.Util;


import com.db_ride_hailing_sys.algorithm.DBSCAN.Args.DBSCAN_ARGS;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Euclidean_Distance;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Geo_Distance;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_ARGS_CONSTANT.*;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_COLLECTIONS_CONSTANT.LIST_HEAD;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Constant.POINT_EXCEPTION.WRONG_DATASET;

public class DBSCAN_Calculator {
    protected DistanceStrategy distanceStrategy;
    protected List<Point> noises;
    protected List<Cluster> clusters;
    protected List<Point> points;
    protected List<Point> unvisited;
    protected DBSCAN_ARGS args;

    public void setDataSet(Object[][] dataSet) throws DBSCAN_POINT_EXCEPTION {
        if(dataSet[0].length!=2){
            throw new DBSCAN_POINT_EXCEPTION(WRONG_DATASET);
        }
        for (Object[] data : dataSet) {
            points.add(
                    new Point((double)data[0], (double)data[1])
            );
        }
        Collections.copy(unvisited,points);
    }

    public void calculateClusters(Object[][] dataSet) throws DBSCAN_POINT_EXCEPTION {
        setDataSet(dataSet);
        double eps = args.getEps();
        long minPts = args.getMinPts();

        while(!unvisited.isEmpty()){
            /**
             * check if this point is noise or core point
             */
            Point point = unvisited.get(LIST_HEAD);
            List<Point> neighborPts = regionQuery(point, eps);
            /**
             * mark it as visited
             */
            unvisited.remove(LIST_HEAD);
            if(neighborPts.size()<minPts){
                /**
                 * a noise point,push it into the noise list
                 */
                noises.add(point);
            }
            else{
                /**
                 * a core point,find its cluster
                 */
                Cluster cluster = expandCluster(point, neighborPts, eps, minPts);
                /**
                 * add this cluster to the cluster list
                 */
                clusters.add(cluster);
            }
        }
    }

    private List<Point> regionQuery(Point p,double eps){
        List<Point> ans = new ArrayList<>();
        for (Point q : points) {
            /**
             * same coordinates,continue
             */
            if(p.equals(q)){
                ans.add(q);
                continue;
            }
            /**
             * q is in the eps-neighbourhood of p
             */
            if(distanceStrategy.calculateDistance(p,q) <= eps){
                ans.add(q);
            }
        }
        return ans;
    }

    private Cluster expandCluster(Point core,List<Point> neighborPts,double eps,long minPts){
        Cluster c = new Cluster(
                Collections.synchronizedList(new LinkedList<Point>()),
                core
        );
        /**
         * the core point must be an element of the cluster
         */
//        c.offer(core);
        while(!neighborPts.isEmpty()){
            /**
             * update the neighbors
             */
            Point p = neighborPts.get(LIST_HEAD);
            if(!isVisited(p)){
                unvisited.remove(p);
                /**
                 * new neighborPts
                 */
                List<Point> accretion = regionQuery(p, eps);
                if(accretion.size()>=minPts){
                    /**
                     * propagate
                     */
                    neighborPts.addAll(accretion);
                }
            }
            if(!isMemberOfAnyCluster(p)){
                c.offer(p);
            }
            neighborPts.remove(LIST_HEAD);
        }
        return c;
    }


    private boolean isVisited(Point p){
        return !unvisited.contains(p);
    }

    private boolean isMemberOfAnyCluster(Point p){
        for (Cluster cluster : clusters) {
            if(cluster.contains(p)){
                return true;
            }
        }
        return false;
    }

    public DBSCAN_Calculator() throws DBSCAN_ARGS_EXCEPTION {
        this.distanceStrategy = new Geo_Distance(defaultEllipsoid);
        this.noises = new ArrayList<Point>();
        this.points = new ArrayList<Point>();
        this.clusters = new ArrayList<Cluster>();
        this.args = new DBSCAN_ARGS(defaultEps,defaultMinPts);
    }

    public DBSCAN_Calculator(DistanceStrategy distanceStrategy, List<Point> noises, List<Cluster> clusters, List<Point> points,  DBSCAN_ARGS args) {
        this.distanceStrategy = distanceStrategy;
        this.noises = noises;
        this.clusters = clusters;
        this.points = points;
        this.args = args;
    }

    public DBSCAN_Calculator(DistanceStrategy distanceStrategy,DBSCAN_ARGS dbscan_args){
        this.distanceStrategy = distanceStrategy;
        this.args = dbscan_args;
        this.noises = new ArrayList<Point>();
        this.points = new ArrayList<Point>();
        this.clusters = new ArrayList<Cluster>();
        this.unvisited = new ArrayList<Point>();
    }
    public DistanceStrategy getDistanceStrategy() {
        return distanceStrategy;
    }

    public void setDistanceStrategy(DistanceStrategy distanceStrategy) {
        this.distanceStrategy = distanceStrategy;
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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getUnvisited() {
        return unvisited;
    }

    public void setUnvisited(List<Point> unvisited) {
        this.unvisited = unvisited;
    }

    public DBSCAN_ARGS getArgs() {
        return args;
    }

    public void setArgs(DBSCAN_ARGS args) {
        this.args = args;
    }
}
