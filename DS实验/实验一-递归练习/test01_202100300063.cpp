#include<iostream>
using namespace std;

//�洢���鳤��
static int k = 0;

// start֪ͨ����Ŀǰ�ɹ�ѡȡԪ�ص��׸�λ��
void getPermutation(int list[], int start) {
	//�ݹ�� ���������ȵ������鳤��ʱ����Ϊȫ����
	if (start == k - 1) {
		for (int i = 0; i < k; i++) {
			if (i != k - 1) {
				cout << list[i] << ",";
			}
			else {
				cout << list[i] << endl;
			}
		}
	}
	else {
		//ѭ��ѡȡһ��Ԫ����Ϊ��ǰ���е�ͷ��
		for (int i = start; i < k; i++) {
			//Ϊ�˱�֤�ֵ��� �����Ԫ��ǰ�����е�Ԫ�����Ųһ�� Ȼ������Ԫ�طŵ�ͷ��
			int temp = list[i];
			for (int j = i; j > start; j--) {
				list[j] = list[j - 1];
			}
			list[start] = temp;
			//���еݹ�
			int nextStart = start + 1;
			getPermutation(list, nextStart);
			//��֤�ֵ��� ���ð����Ԫ���ӻ�ȥ ʵ�ַ�ʽ�ǰ�i֮ǰ�ĳ���ͷ����Ԫ��ͳͳ��ǰŲһ��
			for (int j = start; j < i; j++) {
				list[j] = list[j + 1];
			}
			list[i] = temp;
		}
	}
}


int main() {
	cout << "Input" << endl;
	int n;
	//int k = 0;
	int* origin = new int[20];
	//��ʼ������
	for (int i = 0; i < 20; i++) {
		origin[i] = 0;
	}
	cin >> n;
	//��ֵ
	while (n != 0) {
		origin[k++] = n;
		//cout << n << endl;
		cin >> n;
	}
	//�������еĶ�����ȫ��ȥ��
	//����k �������1����Чֵ k=1 ����2����Чֵ k=2 ... ��˳�ʼ��k��Ԫ��
	int* list = new int[k];
	for (int i = 0; i < k; i++) {
		list[i] = origin[i];
	}

	cout << "Output" << endl;
	//һ��ʼ����start��0�������������Ԫ�ض����Գ�Ϊȫ���е�ͷ��
	getPermutation(list, 0);

	cout << "End" << endl;
	return 0;
}
