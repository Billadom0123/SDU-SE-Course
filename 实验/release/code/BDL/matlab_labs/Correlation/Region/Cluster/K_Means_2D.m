function [cluster_idx, cluster_centers] = K_Means_2D(data,business,k) 
    data = data(business,:,:);
    [num_business, num_regions, num_timepoints] = size(data);  
      
    % 将三维数组转换为二维数组，每行是一个地区的所有业务在所有时间点的流量特征值  
    % 每个地区有 num_business * num_timepoints 个特征  
    num_features_per_region = num_business * num_timepoints;  
    features = reshape(data, num_regions, num_features_per_region);  
      
    [cluster_idx, cluster_centers] = kmeans(features, k);  
      
    % 保持聚类结果为一维向量  
    cluster_idx = reshape(cluster_idx, [100,100]);
end

