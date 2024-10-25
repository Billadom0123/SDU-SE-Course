sx = input('输入行开始索引: ');
ex = input('输入行结束索引: ');
sy = input('输入列开始索引: ');
ey = input('输入列结束索引: ');

[p,q,r] = size(data);

time_seq = 1:r;

for i = sx:(ex-1)
    for j = sy:(ey-1)
        region = (i-1)*100+j;
        r_data = squeeze( data(:,region,:) );
        
        figure;
        hold on;
        
        for k = 1:p
            plot(time_seq,r_data(k,:))
        end
        
        legend('SMSIN','SMSOUT','CALLIN','CALLOUT','Internet');
        title(sprintf('Multiple business:region %d',region))
        xlabel('time points')
        ylabel('Traffic')
        
        grid on;

        print(gcf,'-dpng','-r2048',sprintf('D:\\workSpace\\BigDataLabs\\matlab_labs\\Data_visualization\\lab\\target\\region_%d_traffic.png',region))

        hold off;
    end
end
