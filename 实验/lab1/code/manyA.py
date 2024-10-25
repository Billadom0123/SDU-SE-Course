time = pow(10, 5) + 7

# 需要自己在代码工作的目录下建立好manyA/manyA.txt文件
with open('./manyA/manyA.txt', 'a') as file:
    for i in range(1, time):
        file.write('a')
