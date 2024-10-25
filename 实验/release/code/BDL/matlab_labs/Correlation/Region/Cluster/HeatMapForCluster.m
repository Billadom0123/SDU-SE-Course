function [] = HeatMapForCluster(cluster_center,cluster_idx)

% 将 cluster_idx 转换为与聚类中心对应的流量大小
cluster_size = length(cluster_center);
cluster_value = zeros(size(cluster_idx));
[m,n] = size(cluster_idx);
for i = 1:m
    for j = 1:n
        cluster_value(i,j) = cluster_center(cluster_idx(i,j));
    end
end

% 使用 heatmap 函数绘制热图
heatmap(cluster_value);

colormap('hot')
colorbar;

grid on
% 设置标题和轴标签
title('Heatmap of Cluster Centers Based on Traffic Flow');
xlabel('row');
ylabel('col');
end

