function [cluster_idx, cluster_center] = K_Means(region,k)
    % 将二维数组转换为一维数组
    region_1d = region(:);
    
    % 数据标准化
    region_std = (region_1d - mean(region_1d)) / std(region_1d);
   
    [cluster_idx_1d, cluster_center] = kmeans(region_std, k, 'Replicates', 3);
    
    % 将一维的聚类索引映射回原始的二维数组形式
    cluster_idx = reshape(cluster_idx_1d, size(region));
end