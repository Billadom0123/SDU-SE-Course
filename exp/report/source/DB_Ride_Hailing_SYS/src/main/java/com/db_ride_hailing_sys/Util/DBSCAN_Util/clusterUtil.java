package com.db_ride_hailing_sys.Util.DBSCAN_Util;

import com.db_ride_hailing_sys.EntityInfo.DriverInfo;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.Entity.DBSCAN_driverPoint;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.calculator.LeastDisCalculator;
import com.db_ride_hailing_sys.Util.DBSCAN_Util.processor.NoiseProcessor;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Args.DBSCAN_ARGS;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Cluster;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.DBSCAN_Result;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Entity.Point;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.ARGS.DBSCAN_ARGS_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Exception.Entity.DBSCAN_POINT_EXCEPTION;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Impl.Geo_Distance;
import com.db_ride_hailing_sys.algorithm.DBSCAN.Strategy.Interface.DistanceStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.db_ride_hailing_sys.Util.DBSCAN_Util.DBSCAN_driverUtil.DBSCAN_driver;
import static com.db_ride_hailing_sys.Util.DataUtil.dataUtil.driverDataTranslate;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Constant.DBSCAN_ARGS_CONSTANT.defaultEllipsoid;
import static com.db_ride_hailing_sys.algorithm.DBSCAN.Util.Cluster_Maintainer.*;
import static com.db_ride_hailing_sys.constant.BusinessConstant.DBSCAN_eps;
import static com.db_ride_hailing_sys.constant.BusinessConstant.DBSCAN_minPts;


public class clusterUtil {
    public static void updateClusters() throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
        //初始化聚簇容器
        initMaintainer(new ArrayList<>(), 0L);
        //取出空闲司机列表，重新计算聚簇
        //数据格式转换:DriverInfo->double[][]:matrix nx3
        Object[][] driverPoints = driverDataTranslate();
        //获取聚簇计算结果
        DistanceStrategy distanceStrategy = new Geo_Distance(defaultEllipsoid);
        DBSCAN_ARGS dbscanArgs = new DBSCAN_ARGS(DBSCAN_eps, DBSCAN_minPts);
        DBSCAN_Result dbscanResult = DBSCAN_driver(driverPoints, distanceStrategy, dbscanArgs);
        //将噪声点加入距离最近的聚簇中
        List<Cluster> cl = new NoiseProcessor(defaultEllipsoid).processNoises(dbscanResult);
        initMaintainer(cl,0L);
    }

    public static void addDriverToCluster(DriverInfo driverInfo) {
        //处理还没有初始化聚簇或聚簇集合为空的情况
        if(clusters==null){
            initMaintainer(new ArrayList<Cluster>(),0L);
        }
        if(clusters.isEmpty()){
            DBSCAN_driverPoint key = new DBSCAN_driverPoint(driverInfo.getCurX(), driverInfo.getCurY(), driverInfo.getId());
            Cluster cluster = new Cluster(new LinkedList<Point>(), key);
            cluster.offer(key);
            clusters.add(cluster);
            return;
        }
        LeastDisCalculator leastDisCalculator = new LeastDisCalculator(defaultEllipsoid);
        //查询距离当前位置最近的聚簇的key，得到对应聚簇
        Point driverPoint = new DBSCAN_driverPoint(driverInfo.getCurX(),driverInfo.getCurY(),driverInfo.getId());
        Cluster leastDistanceCluster = leastDisCalculator.getLeastDistanceCluster(driverPoint, clusters);
        //将当前司机节点加入对应聚簇中
        leastDistanceCluster.offer(driverPoint);
    }

    public static void removeDriverFromCluster(Long id){
        outer:
        for (Cluster cluster : clusters) {
            List<Point> cq = cluster.getCq();
            for (int i = 0; i < cq.size(); i++) {
                DBSCAN_driverPoint driverPoint = (DBSCAN_driverPoint) cq.get(i);
                if(driverPoint.getUId()==id){
                    cq.remove(i);
                    //如果聚簇为空，删除该聚簇
                    if(cq.isEmpty()){
                        clusters.remove(cluster);
                    }
                    break outer;
                }
            }
        }
    }

    public static boolean updateBusyIdleFlow() throws DBSCAN_ARGS_EXCEPTION, DBSCAN_POINT_EXCEPTION {
        boolean flag = false;//聚簇是否已经更新到最新状态
        //当前没有派单，聚簇还未初始化，则更新聚簇
        if(null==queueFlow||null==clusters||clusters.size()==0){
            updateClusters();
            flag = true;
        }
        queueFlow++;
        return flag;
    }

    public static long getClustersSize(){
        long ans =0;
        for (Cluster cluster : clusters) {
            ans+=cluster.getCq().size();
        }
        return ans;
    }
}
