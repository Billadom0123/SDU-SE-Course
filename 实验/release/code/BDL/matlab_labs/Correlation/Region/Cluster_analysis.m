business = input('输入业务代码:  ');
time_id = input('输入时间索引:  ');
sx = input('输入开始行索引:  ');
ex = input('输入结束行索引:  ');
sy = input('输入开始列索引:  ');
ey = input('输入结束列索引:  ');

region = region1Dto2D(data,sx,ex,sy,ey,business,time_id);

target_dir = 'D:\workSpace\BigDataLabs\matlab_labs\Correlation\Region\target';

[cluster_idx,cluster_center] = K_Means(region,10);
HeatMapForCluster(cluster_center,cluster_idx);