function [list] = dfs(region,idx,x,y)
    [m,n] = size(region);
    global vis;
    vis(x,y) = 1;
    
    list = [];
    list(1) = (x-1)*100+y;
    global directions;
    for i=1:length(directions)
        direction = directions(i,:);
        nx = x+direction(1);
        ny = y+direction(2);
        if(index_valid(nx,ny,m,n) && vis(nx,ny)==0 && region(nx,ny)==idx)
            list = [list,dfs(region,idx,nx,ny)];
        end
    end
    
end

