function [train_in,train_out,test_in,test_out] = data_normalization(vector)
    [len,~] = size(vector);
    seperator = round(len*0.7);
    
    train_in = vector(1:seperator,1)';
    train_out = vector(1:seperator,2)';
    test_in = vector(seperator+1:len,1);
    test_out = vector(seperator+1:len,2);
   
end

