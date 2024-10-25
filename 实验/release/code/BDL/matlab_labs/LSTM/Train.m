sx = input('输入地区下标行开始索引: ');
ex = input('输入地区下标行结束索引: ');
sy = input('输入地区下标列开始索引: ');
ey = input('输入地区下标列结束索引: ');

for i = sx:(ex-1)
    for j = sy:(ey-1)
        region = (i-1)*100+j;
        for k = 1:5
            vector = squeeze(:,region,k);
            vector = cat(2,(1:len(vector))',vector);
            
            [train_in,train_out,test_in,test_out] = data_normalization(vector);
            
            [train_in,input] = mapminmax(train_in,0,1);
            test_in = mapminmax('apply',test_in,input);
            
            [train_out,output] = mapminmax(train_out,0,1);
            test_out = mapminmax('apply',test_out,output);
            
            [train_cell,test_cell,train_out,test_out] = data_tiling(1,train_in,test_in,train_out,test_out);
            
            [layers,options] = create_network(1,50);
            
            train(train_cell,train_out,layers,options,region,k);
        end
    end
end