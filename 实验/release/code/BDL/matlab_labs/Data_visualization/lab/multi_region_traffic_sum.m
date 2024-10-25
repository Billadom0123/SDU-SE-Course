sx = input('输入行开始索引: ');
ex = input('输入行结束索引: ');
sy = input('输入列开始索引: ');
ey = input('输入列结束索引: ');

[p,q,r] = size(data);
mr = zeros(p,r);

for i = sx:(ex-1)
    for j = sy:(ey-1)
        region = (i-1)*100+j;
        mr = mr + squeeze(data(:,region,:));
    end
end

figure;
hold on;

time_seq = 1:r;
for i = 1:p
    plot(time_seq,mr(i,:))
end

legend('SMSIN','SMSOUT','CALLIN','CALLOUT','Internet');
xlabel('time points')
ylabel('Traffic')
title(sprintf('Region Multiple Business:sx=%d,ex=%d,sy=%d,ey=%d',sx,ex,sy,ey));

print(gcf,'-dpng','-r2048',sprintf('D:\\workSpace\\BigDataLabs\\matlab_labs\\Data_visualization\\lab\\target\\region_%d_%d_%d_%d_traffic',sx,ex,sy,ey))

hold off;