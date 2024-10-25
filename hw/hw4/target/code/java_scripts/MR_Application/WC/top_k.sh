#!/bin/bash  
  
# 检查参数数量  
if [ "$#" -ne 3 ]; then  
    echo "Usage: $0 <input_file_path> <k> <output_file_path>"  
    exit 1  
fi  
  
input_file="$1"  
k="$2"  
output_file="$3"  
  
# 使用awk读取文件，使用sort排序，使用head获取前k个，最后输出到文件  
awk '{print $2, $1}' "$input_file" | sort -nrk 1 | head -n "$k" > "$output_file"  
  
echo "Top $k <单词,词频> 键值对已输出到 $output_file"
