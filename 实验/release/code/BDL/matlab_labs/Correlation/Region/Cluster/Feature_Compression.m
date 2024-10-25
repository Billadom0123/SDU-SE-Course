function compressed_features = Feature_Compression(cluster_centers)  
    % cluster_centers: 聚类中心矩阵，大小为 [num_clusters, num_features]  
    % compressed_features: 压缩后的特征向量，大小为 [num_clusters, 1]，包含每个聚类的特征均值  
      
    % 计算每个聚类中心向量的均值  
    num_clusters = size(cluster_centers, 1); % 获取聚类的数量  
    compressed_features = mean(cluster_centers); % 计算所有特征的均值，得到一个1x8928的行向量  
      
    % 因为我们想要一个列向量作为结果，所以需要转置  
    compressed_features = compressed_features'; % 转置行向量得到列向量  
      
    % 确保输出的向量有正确的行数（尽管这里的转置已经保证了）  
    compressed_features = compressed_features(1:num_clusters); % 截取到正确的行数  
end