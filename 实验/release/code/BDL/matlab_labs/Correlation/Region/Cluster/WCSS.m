function [] = WCSS(region,k_max)
    % 将二维数组转换为一维数组
    region_1d = region(:);
    
    % 数据标准化
    region_std = (region_1d - mean(region_1d)) / std(region_1d);
    
    % 初始化 wcss 向量
    wcss = zeros(1, k_max);
    
    % 使用肘部法则确定聚类数目
    for k = 1:k_max
        [cluster_idx_1d, cluster_center] = kmeans(region_std, k, 'Replicates', 3);
        
        % 计算 WCSS
        for j = 1:k
            cluster_points = region_1d(cluster_idx_1d == j);
            cluster_center_j = cluster_center(j, :);
            within_cluster_sum = sum((cluster_points - cluster_center_j).^2, 2);
            wcss(k) = wcss(k) + sum(within_cluster_sum);
        end
    end
    
    % 绘制肘部曲线图
    plot(1:k_max, wcss);
    xlabel('Number of clusters (k)');
    ylabel('Within-cluster sum of squares (WCSS)');
    title('Elbow Method');
    hold on;
end

