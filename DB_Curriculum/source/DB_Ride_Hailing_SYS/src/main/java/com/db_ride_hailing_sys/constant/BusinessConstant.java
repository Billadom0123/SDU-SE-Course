package com.db_ride_hailing_sys.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessConstant {
    //用户类型
    public static final long Customer_Role = 1L;
    public static final long Driver_Role = 2L;
    //一个司机最多注册1辆车
    public static final int Driver_Max_Vehicle = 1;
    //一辆车最多1个司机注册
    public static final int Vehicle_Max_Driver = 1;
    public static final List<String> Valid_Type;
    static{
        Valid_Type = new ArrayList<>();
        Valid_Type.add("C1");
        Valid_Type.add("C2");
    }
    public static final Map<String,List<String>> typeMatch;
    static {
        typeMatch = new HashMap<String, List<String>>();
        List<String> A1 = new ArrayList<>();
        A1.add("A3");A1.add("B1");A1.add("B2");A1.add("C1");A1.add("C2");A1.add("C3");A1.add("C4");A1.add("M");
        List<String> A2 = new ArrayList<>();
        A2.add("B1");A2.add("B2");A2.add("C1");A2.add("C2");A2.add("C3");A2.add("C4");A2.add("M");
        List<String> A3 = new ArrayList<>();
        A3.add("C1");A3.add("C2");A3.add("C3");A3.add("C4");
        List<String> B1 = new ArrayList<>();
        B1.add("C1");B1.add("C2");B1.add("C3");B1.add("C4");B1.add("M");
        List<String> B2 = new ArrayList<>();
        B2.add("C1");B2.add("C2");B2.add("C3");B2.add("C4");B2.add("M");
        List<String> C1 = new ArrayList<>();
        C1.add("C2");C1.add("C3");C1.add("C4");
        List<String> C2 = new ArrayList<>();
        List<String> C3 = new ArrayList<>();
        C3.add("C4");
        List<String> C4 = new ArrayList<>();
        List<String> D = new ArrayList<>();
        D.add("E");D.add("F");
        List<String> E = new ArrayList<>();
        E.add("F");
        List<String> F = new ArrayList<>();
        List<String> M = new ArrayList<>();
        List<String> N = new ArrayList<>();
        List<String> P = new ArrayList<>();

        typeMatch.put("A1",A1);
        typeMatch.put("A2",A2);
        typeMatch.put("A3",A3);
        typeMatch.put("B1",B1);
        typeMatch.put("B2",B2);
        typeMatch.put("C1",C1);
        typeMatch.put("C2",C2);
        typeMatch.put("C3",C3);
        typeMatch.put("C4",C4);
        typeMatch.put("D",D);
        typeMatch.put("E",E);
        typeMatch.put("F",F);
        typeMatch.put("M",M);
        typeMatch.put("N",N);
        typeMatch.put("P",P);
    }

    public static final double DBSCAN_eps = 10.0;
    public static final long DBSCAN_minPts = 1;
    public static final String WRONG_DATASET = "Data set must be a matrix with three columns";
    public static final long queueFlowMax = 100L;
    public static final String cancelRequest = "订单已取消";
    public static final Double maxTargetDiff = 3000.0;
    public static final Double OverMileAge = 300.0;
    public static final Double MileAgeCost = 3.6;
    public static final Double WaitCost = 1.2;
    public static final Double OverMileAgeCost = 2.0;
    public static final Double NightCost = 0.4;
}
