region = input('请输入地区索引(一维):  ');

r_data = data(:,region,:);
r_data = squeeze(r_data);
[m,n] = size(r_data);

figure;

time_seq = 1:n;

hold on

for i = 1:m
    plot(time_seq,r_data(i,:))
end

legend('SMSIN','SMSOUT','CALLIN','CALLOUT','Internet');
title(sprintf('Multiple business:region %d',region))
xlabel('time points')
ylabel('Traffic')

grid on;

print(gcf,'-dpng','-r2048',sprintf('D:\\workSpace\\BigDataLabs\\matlab_labs\\Data_visualization\\lab\\target\\region_%d_traffic.png',region))

hold off;