xvalues = {'10','11','12','13','14','15','16','17','18','19'};
yvalues = {'10','11','12','13','14','15','16','17','18','19'};
h = heatmap(xvalues,yvalues,rand(10,10));



h.Title = 'Pearson Coeff Mat';

% 设置 X 轴标签
h.XLabel = 'X Axis Label';

% 设置 Y 轴标签
h.YLabel = 'Y Axis Label';