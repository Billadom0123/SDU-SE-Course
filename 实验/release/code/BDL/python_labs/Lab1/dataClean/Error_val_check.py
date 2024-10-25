import sys

import numpy as np

from h5Reader.readHdf5 import readH5

if __name__ == '__main__':
    args = sys.argv
    h5_path = args[1]
    data, idx = readH5(args[1], ['data', 'idx'])

    print("Data Elements error value check:")
    print(np.all(data >= 0))
