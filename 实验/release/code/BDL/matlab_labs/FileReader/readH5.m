function [data,idx] = readH5(h5_path)
    data = h5read(h5_path,'/data');
    idx = h5read(h5_path,'/idx');
end

