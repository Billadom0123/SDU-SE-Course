% t检验
threshold = 0.05;

[a,b] = size(corr_mat);
ht = zeros(a,b);

n = 8928;
for i=1:a
    for j=1:b
        r = corr_mat(i,j);
        t = r * sqrt((n-2)/(1-r*r));
        
        p = 2*(1-tcdf(t,n-2)); %双侧检验
        fprintf('r:%f,t:%f,p%f\n',r,t,p);
        if p<0.05
            ht(i,j) = 1;
        end
    end
end

figure;

x_values = string(sx:ex);
y_values = string(sy:ey);

h = heatmap(x_values,y_values,ht);
h.Title = sprintf('Pearson t hypothesis testing');
h.XLabel = 'region row';
h.YLabel = 'region col';

saveas(gcf,sprintf('D:\\workSpace\\BigDataLabs\\matlab_labs\\Data_visualization\\lab\\target\\hypothesis_testing.png'),'png')
