#include<iostream>
#include<string>
#include<algorithm>
using namespace std;

//�ڵ��࣬ģ��ָ��
class Node {
public:
	int equiv;
	//ָ����ģ��ָ��
	int next;
	//��Ԫ�غ��м���Ԫ��
	int size;
public:
	Node() {
		this->equiv = 0;
		this->next = 0;
		this->size = 1;
	}
	Node(int data, int next, int size) {
		this->equiv = data;
		this->next = next;
		this->size = size;
	}
};

int main() {
	cout << "Input" << endl;
	int n;
	cin >> n;
	Node* arr = new Node[n + 1];
	//��ʼ�ȼ���
	for (int i = 1; i <= n; i++) {
		arr[i].equiv = i;
	}
	int r;
	cin >> r;
	for (int i = 0; i < r; i++) {
		//��ȡ�ȼ۹�ϵ
		string str;
		cin >> str;
		char c1 = str.at(1);
		char c2 = str.at(3);
		int num1 = c1 - 48;
		int num2 = c2 - 48;
		//�������Ԫ��ͬ����һ���ȼ��࣬�Ǿ�û�б�Ҫ�ϲ���
		if (arr[num1].equiv == arr[num2].equiv) {
			continue;
		}
		// ����Ҫȷ�����ϲ������ĸ��ȼ���
		num1 = arr[num1].equiv;
		num2 = arr[num2].equiv;
		//�ϲ�Ԫ��
		//�ѽ�С�ĵȼ���ϲ����ϴ�ĵȼ����У�ѡ��num2��Ϊ��С�ĵȼ���
		if (arr[num1].size < arr[num2].size) {
			int temp = num1;
			num1 = num2;
			num2 = temp;
		}
		//�ȼ������ͷ��
		//���ȣ��ҵ����ϲ��ĵȼ����е����һ��Ԫ��
		Node current = arr[num2];
		//���һ��Ԫ�ص���������final�����������ϲ��ĵȼ���ֻ��һ��Ԫ�أ�������whileѭ������ôfinal��������ȼ����equiv
		int final = num2;
		while (current.next != 0) {
			current = arr[current.next];
			//���ϸ���final
			final = current.next;
		}
		//���һ��Ԫ��ָ�������ȼ���ͷ������һ��Ԫ��
		arr[final].next = arr[num1].next;
		//Node arrFinal = arr[final];
		//������ȼ���ͷ��ָ�������ȼ���ͷ��
		arr[num1].next = num2;
		//�޸��µĵȼ����size
		arr[num1].size += arr[num2].size;
		//Node arrNum1 = arr[num1];
		//�޸ı�����ȼ����equiv
		current = arr[num2];
		arr[num2].equiv = num1;
		while (current.next != 0) {
			arr[current.next].equiv = num1;
			current = arr[current.next];
		}
		//for (int i = 1; i <= n; i++) {
			//cout << arr[i].equiv << " " << arr[i].next << " " << arr[i].size << endl;
		//}
		//cout << endl;
	}

	cout << "Output" << endl;
	for (int i = 1; i <= n; i++) {
		//ÿ���жϵ�ǰ����Ԫ���Ƿ��ǵȼ�������
		//�жϹ���Ϊ��������ǵȼ�����ڣ���ôequivӦ�õ���������������ζ�������ϲ���
		if (arr[i].equiv == i) {
			int* equivalent = new int[arr[i].size];
			//��ʼ���������
			for (int j = 0; j < arr[i].size; j++) {
				equivalent[j] = 0;
			}
			//�ȼ���ͷ��
			equivalent[0] = i;
			int index = 0;
			Node current = arr[i];
			while (current.next != 0) {
				index++;
				equivalent[index] = current.next;
				current = arr[current.next];
			}
			sort(equivalent, equivalent + arr[i].size);
			cout << "(" << equivalent[0];
			for (int j = 1; j < arr[i].size; j++) {
				cout << "," << equivalent[j];
			}
			cout << ")" << endl;
		}
	}
	cout << "End";




	return 0;

}
