function [grid] = ArrayCopy2D(data)
    [~,q,r] = size(data);

    grid = zeros(r,q);
    for i=1:r
        for j=1:q
            grid(i,j) = data(1,j,i);
        end
    end
end

