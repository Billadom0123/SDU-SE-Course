function [] = train(train_in,train_out,layers,options,region,business)
    model = trainNetwork(train_in,train_out,layers,options);
    
    save(sprintf('D:\workSpace\BigDataLabs\matlab_labs\LSTM\model_target\model_%d_%d.mat',region,business),'model');
    
end

