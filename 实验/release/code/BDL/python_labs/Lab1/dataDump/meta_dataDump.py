import os
import sys

import h5py
import happybase
import numpy as np
from happybase import Connection


def dataDumpLog(log_path, log_info):
    if os.path.exists(log_path):
        with open(log_path, "w") as f:
            f.write(log_info)
    else:
        with open(log_path, "w") as f:
            f.write(log_info)


def readH5(file_path, datasets):
    res = []

    # 打开HDF5文件
    with h5py.File(file_path, 'r') as f:
        for name in datasets:
            # 读取数据集
            dataset = f[name]

            # 将数据集数据添加到列表中
            # 假设所有数据集都是NumPy数组
            res.append(np.array(dataset))

    return res


def generate_row_key(a, b, c):
    key = (a << 32) | (b << 16) | c
    return str(key)


def getSplit(path):
    path = path + "/list.txt"
    with open(path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    length = len(lines)
    res = ''
    if length >= 1:
        res = lines[0].rstrip('\n')
        lines = lines[1:]

    with open(path, 'w', encoding='utf-8') as file:
        file.writelines(lines)

    return res


def pick_table(table_name, con: Connection):
    if table_name.encode() not in con.tables():
        connection.create_table(
            table_name, {'y_label': dict(max_versions=10), 'window_size': dict(max_versions=10)}
        )
    return con.table(table_name)


if __name__ == '__main__':
    args = sys.argv

    split_path = args[1]
    hBase_Address = args[2]
    hBase_port = int(args[3])
    meta_name = args[4]
    window = int(args[5])

    print('------loading data split------')
    tarSplit = getSplit(split_path)
    if tarSplit == '':
        exit(0)
    data, idx = readH5(split_path + '/' + tarSplit, ['data', 'idx'])  # 读取目标分块
    print()

    print('------storing sliding windows to Hbase------')
    connection = happybase.Connection(hBase_Address, port=hBase_port)
    index = int(tarSplit.replace('.h5', ''))

    end = index & 0xFFFF
    start = (index >> 16) & 0xFFFF
    x, y, z = data.shape
    meta_table = pick_table(meta_name, connection)
    print('start,end:', start, end)

    # meta_table.put('window_size', {"window_size": str(window).encode()})
    if start > 0:
        for i in range(window):
            for j in range(y):
                for k in range(z):
                    value = data[i, j, k]

                    row_key = generate_row_key(i + start - window, j, k)
                    meta_table.put(row_key, {'y_label:value': str(value).encode()})
                    dataDumpLog(
                        split_path + '/' + meta_name + 'log',
                        "Data dump to " + meta_name + ": key->(" + str(i + start - window) + "," + str(j) + "," + str(
                            k) + "),value->" + str(value) + ",row_key=" + row_key + "\n"
                    )

    for i in range(x - window):
        for j in range(y):
            for k in range(z):
                value = data[i + window, j, k]

                row_key = generate_row_key(i + start, j, k)
                meta_table.put(row_key, {'y_label:value': str(value).encode()})
                dataDumpLog(
                    split_path + '/' + meta_name + 'log',
                    "Data dump to " + meta_name + ": key->(" + str(i + start) + "," + str(j) + "," + str(
                        k) + "),value->" + str(value) + ",row_key=" + row_key + "\n"
                )

    connection.close()
