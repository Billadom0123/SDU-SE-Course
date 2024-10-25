function [res] = ArrayCopy1D(data)
    [~,~,r] = size(data);
    res = zeros(r);
    
    for i=1:r
        res(i) = data(1,1,i);
    end
end

