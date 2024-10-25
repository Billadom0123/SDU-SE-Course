import os
import sys

import h5py


def split_data_and_save(hdf5_path, target_dir, b_size):
    # 打开HDF5文件
    with h5py.File(hdf5_path, 'r') as f:
        # 读取data和idx数据集
        data = f['data'][:]
        idx = f['idx'][:]

        # 初始化分割参数
    block_size = b_size
    num_full_blocks = data.shape[0] // block_size
    remainder = data.shape[0] % block_size

    # 遍历并分割data和idx
    for i in range(num_full_blocks):
        start_idx = i * block_size
        end_idx = start_idx + block_size
        print("start end", start_idx, end_idx)

        # 分割data和idx
        data_block = data[start_idx:end_idx]
        idx_block = idx[start_idx:end_idx]

        # 计算文件名
        begin = start_idx
        end = end_idx - 1
        filename = f"{begin << 16 | end}.h5"

        # 创建输出文件的路径
        output_file = os.path.join(target_dir, filename)

        # 将分割后的数据写入新的HDF5文件
        with h5py.File(output_file, 'w') as outfile:
            outfile.create_dataset('data', data=data_block)
            outfile.create_dataset('idx', data=idx_block)

            # 处理剩余部分
    if remainder > 0:
        start_idx = num_full_blocks * block_size
        end_idx = start_idx + remainder

        # 分割data和idx
        data_remainder = data[start_idx:end_idx]
        idx_remainder = idx[start_idx:end_idx]

        # 计算文件名
        begin = start_idx
        end = end_idx - 1
        filename_remainder = f"{begin << 16 | end}.h5"

        # 创建输出文件的路径
        output_file_remainder = os.path.join(target_dir, filename_remainder)

        # 将剩余数据写入新的HDF5文件
        with h5py.File(output_file_remainder, 'w') as outfile_remainder:
            outfile_remainder.create_dataset('data', data=data_remainder)
            outfile_remainder.create_dataset('idx', data=idx_remainder)


def main(hdf5_path, target_dir, b_size):
    # 确保目标目录存在
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)

        # 调用函数拆分数据并保存
    split_data_and_save(hdf5_path, target_dir, b_size)

    # 创建文件名列表并写入txt文件
    filenames = os.listdir(target_dir)
    filenames.sort(key=lambda x: int(x.split('.')[0]))  # 按文件名中的数字排序
    with open(os.path.join(target_dir, 'list.txt'), 'w') as txtfile:
        for filename in filenames:
            txtfile.write(filename + '\n')


if __name__ == "__main__":
    args = sys.argv
    # HDF5文件的路径
    hdf5_path = args[1]
    # 目标保存目录
    target_dir = args[2]
    chunk_size = int(args[3])
    main(hdf5_path, target_dir, chunk_size)
