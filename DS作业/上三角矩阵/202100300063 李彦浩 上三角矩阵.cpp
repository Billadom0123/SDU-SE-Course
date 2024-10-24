#include<iostream>
using namespace std;

template<class T>
class UpperTriangularMatrix {
private:
	int n;
	T* element;
public:
	//n:Î¬Êý
	UpperTriangularMatrix(int n) {
		if (n < 1) {
			cout << "Matrix size must be >0" << endl;
		}
		this->n = n;
		element = new T[n * (n + 1) / 2];
		for (int i = 0; i < n * (n + 1) / 2;i++) {
			this->element[i] = 0;
		}
	}
	~UpperTriangularMatrix()
	{
		delete[]element;
	}
	T getByRow(int i, int j) {
		if (i<1 || j<1 || i>n || j>n) {
			cout << "index out of bounds" << endl;
			return -1;
		}

		if (i <= j) {
			return element[(2 * n - i + 2) * (i - 1) / 2 + j - i];
		}
		else {
			return 0;
		}

	}

	void setByRow(int i, int j, const T& value) {
		if (i<1 || j<1 || i>n || j>n) {
			cout << "index out of bounds" << endl;
		}

		if (i <= j) {
			element[(2 * n - i + 2) * (i - 1) / 2 + j - i] = value;
		}
		else {
			if (value != 0) {
				cout << "nonUpperTriangular elements must be zero" << endl;
			}
		}
	}

	T getByColumn(int i, int j, T& value) {
		if (i<1 || j<1 || i>n || j>n) {
			cout << "index out of bounds" << endl;
			return -1;
		}
		if (i <= j) {
			return element[j*(j-1)/2+(i-1)];
		}
		else {
			return 0;
		}

	}

	void setByColumn(int i, int j, const T& value) {
		if (i<1 || j<1 || i>n || j>n) {
			cout << "index out of bounds" << endl;
		}
		if (i <= j) {
			this->element[j * (j - 1) / 2 + (i - 1)] = value;
		}
		else {
			if (value != 0) {
				cout << "nonUpperTriangular elements must be zero" << endl;
			}
		}
	}

	T* getArray() {
		return element;
	}

};

int main() {
	//½ö²âÊÔset¼´¿É
	int n = 4;
	UpperTriangularMatrix<int> matrix = UpperTriangularMatrix<int>(4);
	int k = 0;
	for (int i = 1; i <= 4; i++) {
		for (int j = i; j <= 4; j++) {
			matrix.setByColumn(i, j, k++);
		}
	}
   for (int i = 0; i < 10; i++) {
		cout << matrix.getArray()[i] << " ";
	}

   cout << endl;
   k = 0;
   for (int i = 1; i <= 4; i++) {
	   for (int j = i; j <= 4; j++) {
		   matrix.setByRow(i, j, k++);
	   }
   }

   for (int i = 0; i < 10; i++) {
	   cout << matrix.getArray()[i] << " ";
   }


}