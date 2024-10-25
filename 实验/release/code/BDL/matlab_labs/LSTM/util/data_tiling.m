function [train_cell,test_cell,train_out,test_out] = data_tiling(feature_dim,train_in,test_in,train_out,test_out)
    m = size(train_in,2);
    n = size(test_in,2);
    train_in = double(reshape(train_in,feature_dim,1,1,m));
    test_in = double(reshape(test_in,feature_dim,1,1,n));
    
    train_out = train_out';
    test_out = test_out';
    
    for i = 1:m
        train_cell{i,1} = train_in(:,:,1,i);
    end
    
    for i = 1:n
        test_cell{i,1} = test_in(:,:,1,i);
end

