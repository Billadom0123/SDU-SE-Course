center = input('请输入正中心小区的索引(一维): ');

row = floor(center/100);
col = center - row*100;

sx = row-4;
ex = row+5;

sy = col-4;
ey = col+5;

[p,q,r] = size(data);
center_data = squeeze(data(:,center,:));

center_sum = zeros(1,r);

for i=1:p
    center_sum = center_sum + squeeze(center_data(i,:));
end

corr_mat = zeros(ex-sx+1,ey-sy+1);
for j = sx:ex
    for k = sy:ey
        neighbour = j*100+k;
        neighbour_sum = zeros(1,r);
        for i=1:p
            neighbour_sum = neighbour_sum + transpose(squeeze( data(i,neighbour,:) ));
        end

        coef_mat = corrcoef(transpose(center_sum),transpose(neighbour_sum));
        corr_mat(j-sx+1,k-sy+1) = coef_mat(1,2);
        
    end
end

figure;

bar3(corr_mat);
% 设置标题和坐标轴标签  
title(sprintf('Pearson Coeff Mat based on %d:All Business',center,i));  
xlabel('region row');  
ylabel('region col');  
zlabel('Correlation');  

%x_values = string(sx:ex);
%y_values = string(sy:ey);

%h = heatmap(x_values,y_values,corr_mat);
%h.Title = sprintf('Pearson Coeff Mat based on %d:All Business',center,i);
%h.XLabel = 'region row';
%h.YLabel = 'region col';

%suffix = 'heatmap';
suffix = 'bar3';
saveas(gcf,sprintf('D:\\workSpace\\BigDataLabs\\matlab_labs\\Data_visualization\\lab\\target\\Pearson_Coeff_based_on_%d_All_Business_%s.png',center,suffix),'png')

