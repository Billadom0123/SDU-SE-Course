function [] = region_sum(time_id,sx,ex,sy,ey,data,target_dir)
    arr = data(:,1:10000,time_id);
    city = zeros(100,100);
    
    for i=1:100
        for j=1:100
            for k = 1:5
                city(i,j) = city(i,j)+ arr( k,(i-1)*100+j);
            end
        end
    end
    
    m = ex-sx+1;
    n = ey-sy+1;
    mat = zeros(m,n);
  
    for i=1:m
        for j=1:n
            mat(i,j) = city(i+sx-1,j+sy-1);
        end
    end
    
    figure
    h = heatmap(mat);
    
    title(sprintf('所有第%d时间段流量热力图',time_id))
    xlabel(sprintf('横向小区号(自%d开始)',sx))
    ylabel(sprintf('纵向小区号(自%d开始)',sy))
    
    colormap('hot')
    colorbar;
    
    grid on
    
    png_name = sprintf('%d_%d_%d %d_%d.png',time_id,sx,ex,sy,ey);
    filename = fullfile(target_dir,png_name);
    exportgraphics(gcf,filename,'Resolution',1024)
    
end

