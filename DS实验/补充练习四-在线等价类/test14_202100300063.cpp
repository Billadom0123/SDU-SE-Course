#include<iostream>
#include<string.h>
#include<algorithm>
using namespace std;

//���������˼·:��֤��ѭ������ʱ��ǰ����������ģ�����
// �ں�һ��Ԫ���ƶ�ʱ��ֻҪ�ñ��ƶ���Ԫ��Ҳ��˳����룬��ô��һ��Ҳ������ģ������������ġ�
//ͨ�����ѭ���������г����������ڲ�ѭ����֤��������
void InsertSort(int* arr,int k) {
	//��ϧ���ǣ�������������ð������һ����ʱ��ֹ��������Ϊ������һ��û�ƶ�����һ����Ȼ�����ƶ�Ԫ��
	//ע��������������Ԫ��ҲҪ����������
	for (int i = 0; i < k; i++) {
		//�����������ʱΪ�˱ȽϺ�ǰһ��Ԫ�صĴ�С��j����Ϊ0������j-1<0
		for (int j = i; j > 0; j--) {
			if (arr[j] < arr[j - 1]) {
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
			else {
				//�����һ�αȽ϶�û��������Ϊԭ����������ģ�˵����һ��һ�����ύ����ֱ������ȥ
				break;
			}
		}
	}
}

class Node {
public:
	int equiv;
	int next;
public:
	Node() {
		equiv = 0;
		next = 0;
	}
	Node(int value, int next) {
		this->equiv = value;
		this->next = next;
	}
};

int main() {
	cout << "Input" << endl;
	int n;
	cin >> n;
	int size = n;
	Node* arr = new Node[n+1];
	//n���ȼ���
	for (int i = 1; i <= n; i++) {
		arr[i] = Node(i, 0);
	}
	cin >> n;
	string str;
	for (int i = 0; i < n; i++) {
		cin >> str;
		//�õ�����Ҫ�ϲ���Ԫ��
		int num1 = str.at(1) - 48;
		int num2 = str.at(3) - 48;
		//�ҵ�������Ԫ�صĵȼ���
		int equiv1 = arr[num1].equiv;
		int equiv2 = arr[num2].equiv;
		//Ҳ�����ĸ����ˣ�������������ͦС�ġ�
		//�ѵڶ����ϲ�����һ����ȥ��
		if (equiv1 == equiv2) {
			//�Ѿ�ͬһ��������
			continue;
		}
		else {
			//�õ��ڶ����ȼ����ͷָ�롣��������β���ڵ�
			Node* current = &arr[equiv2];
			while (current->next != 0) {
				//�޸ĵȼ���
				current->equiv = equiv1;
				current = &arr[current->next];
			}
			current->equiv = equiv1;
			//���õڶ����ȼ���β�����ӵ���һ���ȼ���ĵڶ���Ԫ����
			current->next = arr[equiv1].next;
			//���õ�һ���ȼ����ͷԪ�����ӵ��ڶ����ȼ����ͷԪ����
			arr[equiv1].next = equiv2;
		}
	}
	cin >> n;
	cout << "Output" << endl;
	if (n >= 1 && n <= size) {
		int equiv = arr[n].equiv;
		int* sort = new int[size];
		int i = 1;
		Node* current = &arr[equiv];
		sort[0] = equiv;
		while (current->next != 0) {
			sort[i++] = current->next;
			current = &arr[current->next];
		}
		int* result = new int[i];
		for (int j = 0; j < i; j++) {
			result[j] = sort[j];
		}
		InsertSort(result, i);

		cout << "(" << result[0];
		for (int j = 1; j < i; j++) {
			cout << "," << result[j];
		}
		cout << ")" << endl;
	}
	else {
		cout << 0 << endl;
	}

	cout << "End";
	return 0;
}