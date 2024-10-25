import h5py
import numpy as np


def listOfDataSet(h5_path):
    with h5py.File(h5_path, 'r') as f:
        # 列出文件中的所有对象名称
        for name in f:
            print(name)


def readH5(filePath, datasets):
    res = []

    # 打开HDF5文件
    with h5py.File(filePath, 'r') as f:
        for name in datasets:
            # 读取数据集
            dataset = f[name]

            # 将数据集数据添加到列表中
            # 假设所有数据集都是NumPy数组
            res.append(np.array(dataset))

    return res


if __name__ == '__main__':
    data, idx = readH5('C:\\Users\\Administrator\\Desktop\\college\\junior2\\工业大数据\\实验\\lab1\\data\\split_10\\584590047.h5',
                       ['data', 'idx'])
    print()
