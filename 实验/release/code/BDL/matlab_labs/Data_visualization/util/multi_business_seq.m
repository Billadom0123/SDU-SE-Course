function [] = multi_business_seq(data,st,region)
    % 获取业务的数量（行数）和时间点的数量（列数）  
    num_businesses = size(data, 1);  
    num_timepoints = size(data, 2);  

    % 创建一个新的图形窗口  
    figure;  

    % 初始化hold on，以便在同一个图上绘制多个折线图  
    hold on;  

    % 设置不同的颜色以便区分不同的业务  
    colors = hsv(num_businesses); % 使用HSV颜色空间生成不同颜色  

    % 遍历每种业务，绘制其流量时间序列  
    for i = 1:num_businesses  
    % 绘制第i种业务的流量时间序列，只使用实线，不使用标记  
    plot(1:num_timepoints, data(i, :), 'Color', colors(i, :), '-'); % 指定颜色为colors(i,:)  
    end  



    % 为每种业务的折线图添加图例  
    legend_strings = cellstr(arrayfun(@(x) sprintf('业务 %d', x), 1:num_businesses, 'UniformOutput', false));  
    legend(legend_strings, 'Location', 'NorthWest'); % 将图例放在图的左上角  

    % 关闭hold on，以便后续绘图操作不会添加到当前图上  
    hold off;  

    % 显示网格（可选）  
    grid on;
end

    % 设置图的标题和坐标轴标签  
    title(sprintf('第%小区d业务流量时间序列',region));  
    xlabel(sprintf('时间点(从第%d个时间点开始)',st));  
    ylabel('流量');

