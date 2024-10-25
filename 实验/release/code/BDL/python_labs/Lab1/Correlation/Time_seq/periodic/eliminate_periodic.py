import matplotlib.pyplot as plt
import pandas as pd
from statsmodels.tsa.seasonal import seasonal_decompose


def remove_seasonality_and_trend(ts, start_date, target_dir, business, region, freq='10min'):
    # 根据起始时间和频率生成日期范围
    dates = pd.date_range(start=start_date, periods=len(ts), freq=freq)

    # 将一维数组和日期转换为pandas Series
    ts_series = pd.Series(ts, index=dates)

    # 季节性分解
    result = seasonal_decompose(ts, model='additive', period=24 * 6)

    # 绘制原始时间序列和分解结果
    plt.figure(figsize=(12, 8))

    # 原始时间序列
    plt.subplot(4, 1, 1)  # 使用 4 行 1 列的子图布局，当前是第 1 个子图
    plt.plot(ts_series, label='Original')
    plt.title('Original Time Series')
    plt.legend()

    # 分解结果的趋势
    plt.subplot(4, 1, 2)  # 当前是第 2 个子图
    # result.trend.plot(label='Trend')
    plt.plot(result.trend, label='Trend')
    plt.title('Trend')
    plt.legend()

    # 分解结果的季节性
    plt.subplot(4, 1, 3)  # 当前是第 3 个子图
    # result.seasonal.plot(label='Seasonal')
    plt.plot(result.seasonal, label='Seasonal')
    plt.title('Seasonal')
    plt.legend()

    # 如果需要，可以加上部分趋势
    # trend = result.trend
    # deseasonalized_ts = trend + resid

    # 分解结果的残差（即去季节化和去趋势化后的时间序列）
    deseasonalized_ts = result.resid
    plt.subplot(4, 1, 4)  # 当前是第 4 个子图
    plt.plot(deseasonalized_ts, label='Deseasonalized')
    # deseasonalized_ts.plot(label='Resid')
    plt.title('Deseasonalized Time Series')
    plt.legend()

    # 调整子图之间的间距，防止标签重叠
    plt.tight_layout()

    # plt.savefig(target_dir + "\\Deseaonalized_" + str(region) + "_" + str(business) + ".png", dpi=300)

    # 显示图形
    plt.show()

    # 返回去除季节性和周期性后的时间序列
    return deseasonalized_ts
