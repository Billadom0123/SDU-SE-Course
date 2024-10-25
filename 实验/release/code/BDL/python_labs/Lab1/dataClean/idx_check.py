import logging
from datetime import datetime as dt


def are_time_intervals_equal(idx):
    # 将字节串数组转换为datetime对象数组
    timestamps = [dt.strptime(i.decode('utf-8'), '%Y-%m-%d %H:%M:%S') for i in idx]

    # 检查数组长度是否大于1，因为至少需要两个时间戳才能比较间隔
    if len(timestamps) < 2:
        return False

        # 计算第一个时间间隔
    first_interval = (timestamps[1] - timestamps[0]).total_seconds()

    # 检查剩余时间间隔是否都等于第一个时间间隔
    for i in range(1, len(timestamps) - 1):
        current_interval = (timestamps[i + 1] - timestamps[i]).total_seconds()
        if current_interval != first_interval:
            logging.info("索引"+str(i)+"处时间间隔不同")
            return False

            # 所有时间间隔都相等
    return True
