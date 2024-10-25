import numpy as np
from keras.datasets import mnist
from keras.models import load_model

(X_train, y_train), (X_test, y_test) = mnist.load_data()
model = load_model('cnn.h5')

predictions = np.argmax(model.predict(X_test),axis=1)
correct = 0
for i in range(len(y_test)):
    if predictions[i] == y_test[i]:
        correct += 1
print("accuracy" + str(correct / len(predictions)))
