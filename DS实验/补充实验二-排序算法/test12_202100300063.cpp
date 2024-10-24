#include<iostream>
#include<string.h>
using namespace std;

static int i = 0;

class Node {
public:
	int value;
	Node* next;
public:
	Node() {
		this->value = NULL;
		this->next = NULL;
	}
};
void RadixSort(int* arr) {
	//������������ 9������
	Node chain[10];

	for (int j = 0; j < i; j++) {
		int index = arr[j];
		//��������ͷ��
		if (chain[index].value == NULL) {
			chain[index].value = index;
		}
		else {
			Node* newNode = new Node();
			newNode->next = chain[index].next;
			newNode->value = index;
			chain[index].next = newNode;
		}
	}

	int start = 0;

	//������������
	for (int j = 1; j < 10; j++) {
		//��������������Ԫ��
		if (chain[j].value != NULL) {
			Node* current = &chain[j];
			while (current->next != NULL) {
				arr[start++] = current->value;
				current= current->next;
			}
			arr[start++] = current->value;
		}
	}
}

int main() {
	cout << "Input" << endl;
	int n;
	int origin[18];
	bool flag = false;//��¼��û��Ԫ�ش��ڵ���10
	cin >> n;
	while (n != 0) {
		if (n > 9) {
			flag = true;
		}
		origin[i++] = n;
		cin >> n;
	}
	int* arr = new int[i];
	for (int j = 0; j < i; j++) {
		arr[j] = origin[j];
	}

	cout << "Output" << endl;
	cout << "Radix Sort" << endl;
	if (flag) {
		cout << 0 << endl;
	}
	else {
		//һλ���Ļ���������ɶ��˼��
		RadixSort(arr);
		cout << arr[0];
		for (int j = 1; j < i; j++) {
			cout << "," << arr[j];
		}
		cout << endl;
	}

	cout << "End";

	return 0;
}