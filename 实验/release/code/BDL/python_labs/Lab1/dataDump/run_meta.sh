#!/bin/bash  
  
# 假设这是你的Python环境路径，根据你的实际环境修改  
PYTHON_ENV=/usr/bin/python3.10 
  
# 切换到Lab1目录  
cd /home/202100300063/code/BigDataLabs/python_labs/Lab1/dataDump 
  
# 循环执行9次  
for i in {1..895}  
do  
    # 这里是6个命令行参数的示例，你可以根据需要修改它们  
    # 假设它们是基于循环变量i的某种计算或固定值  
    param1="/home/202100300063/data/BigDataLabs/dataset/split_10"  
    param2="localhost"  
    param3="9090"  
    param5="meta_data"  
    param6="6"  
  
    # 执行dataDump.py，并传递参数  
    $PYTHON_ENV meta_dataDump.py "$param1" "$param2" "$param3" "$param5" "$param6"  
  
    # 如果需要，可以在每次执行后添加一些输出或延时  
    echo "Execution $i completed."  
    # sleep 1  # 如果需要等待一段时间再执行下一次，可以取消注释这一行  
done
