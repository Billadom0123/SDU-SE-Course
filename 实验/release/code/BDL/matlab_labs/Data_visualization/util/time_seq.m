function [] = time_seq(business,region,st,et,data,target_dir)
    arr = data(business,region,st:et);
    arr = ArrayCopy1D(arr);
    
    figure
    plot(arr)
    
    title(sprintf("第%d业务流量时间序列图",business))
    xlabel(sprintf('时间(以10分钟为单位,从%d开始)',st))
    ylabel('流量')
    
    image_save(gcf,sprintf('%d %d %d_%d.png',business,region,st,et),target_dir,'png')
    
end

