function [flag] = index_valid(i,j,m,n)
    flag = (i>=1) && (i<=m) && (j>=1) && (j<=n);
end

