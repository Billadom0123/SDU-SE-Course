import numpy as np
# from torchvision.datasets import MNIST#获取MNIST的数据集
from keras.models import Sequential
from keras.layers import Conv2D, MaxPool2D, Dense, Flatten
from keras.utils import to_categorical
import tensorflow._api.v2.compat.v1 as tf
import keras
from keras.datasets import mnist
import matplotlib.pyplot as plt
import cv2
tf.disable_v2_behavior()

# --------------------------------Preparing data--------------------------------
# trainData = MNIST(root="/MNIST_data", train=True, download=True)
# testData = MNIST(root="/MNIST_data",train=False,download=True)
# train_images, train_labels = trainData.train_data,trainData.train_labels
# test_images, test_labels = testData.test_data,testData.test_labels
(X_train, y_train), (X_test, y_test) = mnist.load_data()
img_x, img_y = X_train.shape[1], X_train.shape[2]


# plt.imshow(X_train[1])
# plt.show()


X_train = X_train.reshape(X_train.shape[0], img_x, img_y, 1)
X_test = X_test.reshape(X_test.shape[0], img_x, img_y, 1)
X_train = X_train.astype('float32')
X_test = X_test.astype('float32')
X_train /= 255
X_test /= 255

y_train = keras.utils.to_categorical(y_train, num_classes=10)
# --------------------------------Keras--------------------------------
#构建模型
model = Sequential()
model.add(Conv2D(32, kernel_size=(5,5), activation='relu', input_shape=(img_x, img_y, 1)))
model.add(MaxPool2D(pool_size=(2,2), strides=(2,2)))
model.add(Conv2D(64, kernel_size=(5,5), activation='relu'))
model.add(MaxPool2D(pool_size=(2,2), strides=(2,2)))
model.add(Flatten())
model.add(Dense(1000, activation='relu'))
model.add(Dense(10, activation='softmax'))

#模型编译
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

#训练
model.fit(X_train, y_train, batch_size=128, epochs=10)

#评估模型
score = model.evaluate(X_test, keras.utils.to_categorical(y_test, num_classes=10))
print('acc', score[1])
# #Save the model to disk.
model.save('cnn.h5')
#
# #Load the model from disk later using:
# model.load_weights('cnn.mnist')

# Predict on the first 8 test images.
predictions = model.predict(X_test[:10])

# Print our model's predictions.
print('model''s predictions')
print(np.argmax(predictions, axis=1))

# Check our predictions against the ground truths.
print('checker')
print(y_test[:10])
