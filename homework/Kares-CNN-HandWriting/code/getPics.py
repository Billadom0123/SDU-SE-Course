import cv2
from keras.datasets import mnist

(X_train, y_train), (X_test, y_test) = mnist.load_data()

path = "C:\\Users\\Administrator\\Desktop\\hwNums\\"
for i in range(0,100):
    cv2.imwrite(path+str(i)+".png", X_train[i])