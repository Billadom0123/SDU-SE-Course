function [clusters] = cluster_list(region,k)
    clusters = cell(1,k);
    global directions;
    directions = [-1,0;0,1;1,0;0,-1];
    
    [m,n] = size(region);
    global vis;
    vis = zeros(m,n);
    for i=1:m
        for j=1:n
            if(vis(i,j)==0)
                idx = region(i,j);
                disp(idx)
                disp(clusters{idx})
                res = dfs(region,idx,i,j);
                if length(res)>length(clusters{idx})
                    clusters{idx} = res;
                end
            end
        end
    end
end

