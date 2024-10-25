function [res] = region1Dto2D(data,sx,ex,sy,ey,business,time_id)
% 地区是第二维,总计100*100
    data = data(business,1:size(data,2),time_id);
    region_len = size(data,2);
    mat_len = sqrt(region_len);
    mat = zeros(mat_len,mat_len);
    m = ex-sx+1;
    n = ey-sy+1;
    res = zeros(m,n);
    
    for i = 1:mat_len
        for j = 1:mat_len
            mat(i,j) = data((i-1)*100+j);
        end
    end
    
    for i = 1:m
        for j = 1:n
            res(i,j) = mat(i+sx-1,j+sy-1);
        end
    end
    
end

