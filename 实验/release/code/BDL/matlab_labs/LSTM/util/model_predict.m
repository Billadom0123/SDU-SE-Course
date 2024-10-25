function [prediction] = model_predict(region,business,test_in)
    load(sprintf('D:\workSpace\BigDataLabs\matlab_labs\LSTM\model_target\model_%d_%d.mat',region,business),'model');
    
    prediction = predict(model,test_in);
end

