import os
import threading
from glob import glob

import cv2
import numpy as np
from keras.models import load_model


def readFiles(f_path):
    return glob(
        os.path.join(f_path + "\\*.png")
    )


def showImages(eles):
    list = []
    for i in range(len(eles)):
        img = cv2.imread(eles[i])
        cv2.imshow('img' + str(i), adjustImg(img, 640))
        cv2.imshow('img' + str(i) + str(0), adjustImg(img, 28))
    cv2.waitKey(0)


def getImages(eles):
    l = []
    for i in range(len(eles)):
        img = cv2.imread(eles[i])
        l.append(img)
    img = l[0]
    ans = np.empty((len(l), 28, 28))
    # ans = np.empty((len(l),img.shape[0],img.shape[1]))
    for i, e in enumerate(l):
        e = adjustImg(e, 28)
        ans[i] = cv2.cvtColor(e, cv2.COLOR_BGR2GRAY)
    return ans


def adjustImg(e, target):
    while min(e.shape[0], e.shape[1]) > target:
        e = cv2.pyrDown(e, )
    e = cv2.resize(e, (target, target), interpolation=cv2.INTER_CUBIC)
    return e


# C:\workplace\Machine_Vision_Practice\Lec3\pic\hwNums
if __name__ == '__main__':
    path = input("输入手写数字图片所在文件夹路径:")
    files = readFiles(path)
    images = getImages(files)
    t_show = threading.Thread(target=showImages, args=(files,))
    t_show.start()
    img_x, img_y = images.shape[1], images.shape[2]
    images = images.reshape(images.shape[0], img_x, img_y, 1)
    images = images.astype('float32')
    images /= 255

    model = load_model('cnn.h5')

    predictions = model.predict(images)
    print('predictions')
    print(np.argmax(predictions, axis=1))
