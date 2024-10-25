import sys

import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.stattools import acf, pacf

from Lab1.Correlation.Time_seq.periodic import eliminate_periodic
from h5Reader import readHdf5
from scipy.interpolate import UnivariateSpline
from statsmodels.stats.diagnostic import acorr_ljungbox


def ljung_box_q_test(time_series, lags=None):
    # 执行Ljung-Box Q检验
    res = acorr_ljungbox(time_series, lags=lags)

    return res


def cubic_spline_interpolate(data):
    # 找出非nan值的索引
    valid_data = data[~np.isnan(data)]
    valid_indices = np.arange(data.size)[~np.isnan(data)]

    # 使用三次样条插值
    spline = UnivariateSpline(valid_indices, valid_data, k=3)

    # 应用插值到整个数组
    data_interpolated = spline(np.arange(data.size))

    return data_interpolated


def ACF(ts, region: int, business: int, suffix: str):
    lags = np.arange(1, len(ts))  # 定义延迟范围
    acf_values = acf(ts, nlags=len(lags) - 1)  # 计算自相关函数值

    plt.figure(figsize=(12, 6))
    # plt.subplot(121)
    plt.plot(lags, acf_values)
    plt.title('Auto correlation Function (ACF)')
    plt.xlabel('Lags')
    plt.ylabel('ACF')
    plt.grid(True)
    print(target_dir + "\\ACF_" + str(region) + "_" + str(business) + ".png")
    plt.savefig(target_dir + "\\ACF_" + str(region) + "_" + str(business) + "_" + suffix + ".png", dpi=300)

    return acf_values


def PACF(ts, region: int, business: int, suffix: str):
    # 样本大小
    sample_size = len(ts)
    # 计算偏自相关函数时，nlags不能超过样本大小的50%
    max_nlags = sample_size // 1000
    lags = np.arange(max_nlags + 1)  # 延迟范围从0到max_nlags
    # 计算偏自相关函数值
    pacf_values = pacf(ts, nlags=max_nlags)

    # plt.subplot(122)
    plt.plot(lags, pacf_values)
    plt.title('Partial Auto correlation Function (PACF)')
    plt.xlabel('Lags')
    plt.ylabel('PACF')
    plt.grid(True)
    print(target_dir + "\\PACF_" + str(region) + "_" + str(business) + ".png")
    plt.savefig(target_dir + "\\PACF_" + str(region) + "_" + str(business) + "_" + suffix + ".png", dpi=300)

    return pacf_values


if __name__ == '__main__':
    args = sys.argv

    dataset_path = args[1]
    region = int(args[2])
    business = int(args[3])
    st = int(args[4])
    et = int(args[5])
    target_dir = args[6]

    data, idx = readHdf5.readH5(dataset_path, ['data', 'idx'])

    for i in range(5):
        ts_slice = data[:, region, i]

        ts_slice = (eliminate_periodic.
                    remove_seasonality_and_trend(ts_slice, idx[st].decode('utf-8'), target_dir, i, (region + 1)))
        ts_slice = cubic_spline_interpolate(ts_slice)
        # ACF(ts_slice, (region + 1), i, 'deseasonalized')
        PACF(ts_slice, (region + 1), i , 'deseasonalized')

    # ts_slice = data[:, region, business]

    #
    # lags = 8296
    # res = ljung_box_q_test(ts_slice, lags=lags)

    # # 判断哪些滞后阶数的自相关性是显著的（以0.05为显著性水平）
    # significance_level = 0.05
    # with open(
    #         target_dir + "\\Ljung_Box_Q_" + str(business) + "_" + str(region) + "_" + str(st) + "_" + str(et) + ".txt",
    #         "a") as test_file:
    #     test_file.truncate(0)
    #     for i in range(lags):
    #         p_value = res['lb_pvalue'][i+1]
    #         if p_value<significance_level:
    #             test_file.write(str(i+1) + " " + str(p_value) + "\n")
