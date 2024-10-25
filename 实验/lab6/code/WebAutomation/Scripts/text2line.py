import os
import re
import json
import sys

def text2line(file_path:str):
    print(f'正在处理任务:${file_path}')
    with open(file_path,"r",encoding = 'utf-8') as file:
        text = file.readlines()
        file.close()

    with open(file_path,"w",encoding = 'utf-8') as file:
        text = ''.join(text)
        text = re.sub(r'\r|\n', '', text)
        file.write(text)

if __name__ == '__main__':

    config_path = sys.argv[1]

    with open(config_path,'r',encoding = 'utf-8') as file:
        config = json.load(file)

    root = config['root']
    print(root)
    assert os.path.isdir(root)

    files = os.listdir(root)

    for src in files:
        text2line(os.path.join(root,src))