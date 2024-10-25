function [data_compressed] = time_seq_compression(data)
    % 假设 data 是一个 p x q x r 的三维数组  
    % p 是业务数量，q 是地区数量，r 是时间点的数量（每个时间点代表10分钟）  

    % 获取数组的尺寸  
    [p, q, r] = size(data);  

    % 计算每天的时间点数量（144个10分钟）  
    time_per_day = 144;  

    % 检查r是否是time_per_day的整数倍  
    if mod(r, time_per_day) ~= 0  
        error('时间点的数量不是每天时间点数量（144）的整数倍。');  
    end  

    % 初始化一个用于存储压缩后数据的三维数组  
    data_compressed = zeros(p, q, r/time_per_day);  

    % 遍历每一天的数据，计算平均值  
    for day = 1:time_per_day:r  
        % 获取当前天的时间点索引范围  
        idx = (day-1)+1:(day+time_per_day-1);  
        % 提取当前天的数据（对于所有业务和地区）  
        data_day = squeeze(data(:,:,idx)); % 去除可能的单一维度  
        % 计算当前天的平均值（沿着时间维度）  
        data_day_mean = mean(data_day, 3); % 3 是时间维度的索引  
        % 将计算得到的平均值存储到压缩后的数组中  
        data_compressed(:,:,ceil(day/time_per_day)) = data_day_mean;  
    end
end

