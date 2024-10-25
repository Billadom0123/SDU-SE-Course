function [] = time_region(business, st, et, sr, er, data,target_dir)  
    disp(business)
    % 获取grid数据  
    grid = data(business, sr:er, st:et);  
    grid = ArrayCopy2D(grid);
      
    % 绘制柱状图  
    figure;  
    bar3(grid);  
        
    % 设置标题和坐标轴标签  
    title(sprintf('第%d业务时空流量图', business));  
    xlabel(sprintf('时间(单位10分钟,从%d开始)',st));  
    ylabel(sprintf('小区索引(从%d开始)',sr));  
    zlabel('流量');  
      
    % 设置视角  
    view(3);  
      
    % 构建文件名  
    png_name = sprintf('%d %d_%d %d_%d.png', business, st, et, sr, er);  
    image_save(png_name,target_dir,'png')
end