png_target_dir = 'D:\workSpace\BigDataLabs\matlab_labs\Data_visualization\target\time_region';
%time_region(5,200,225,100,150,data,png_target_dir);
%time_region(5,1,50,1,50,data,png_target_dir);

business = input('输入业务代码:  ');
st = input('输入时间开始始索引:  ');
et = input('输入时间结束索引:  ');
sr = input('输入小区号开始索引:  ');
er = input('输入小区号结束索引:  ');
time_region(int32(business),int32(st),int32(et),int32(sr),int32(er),data,png_target_dir);