#include<iostream>
#include<vector>
using namespace std;

//�洢ÿһ�ε��Ӽ�����Ҫ����
template<class T>
static vector<T> v;
static int n;

template <class T>
//list��ȫ�� total�ǵ�ǰ�ɹ�ѡ���Ԫ������ select�ǵ�ǰ����Ҫѡ��ĸ��� start�ǵ�ǰѡ�������Ŀ�ʼ����
void subset(T list[], int total, int select, int start) {
	//��ȫ��У�� ȫ������һ��Ҫ���ڵ��ڵ�ǰ�ɹ�ѡ��Ԫ�صĸ���
		//�ݹ���������ǰ��Ҫѡ��ĸ����Ѿ���0�ˣ�˵����ǰ�Ӽ�������ϣ����
		if (select == 0) {
			cout << "{"; cout << v<T>[0];
			for (int i = 1; i < v<T>.size(); i++) {
				cout << ","; cout << v<T>[i];
			}
			cout << "}";
			cout << endl;
		}
		else {
			//����ÿһ��ͷ��
			for (int i = start; i < n ; i++) {
				//��ȡ��ǰ�������� �ڵõ���һ���Ӽ������
				int index = v<T>.size();
				//��ѡ��Ԫ������������
				v<T>.push_back(list[i]);
				//��һ��ѡ���Ԫ����Ҫ����һ��Ԫ�صĺ���һ����ʼ ��ֹ�ظ�
				int temptotal=total-1; int tempselect=select-1;int tempi= i+1;
				subset(list, temptotal,tempselect,tempi);
				//���� �������ص�δ��Ӹ�Ԫ�ص�״̬ ����Ϊ ��ǰ�������ȼ�ȥ��ǰ��������
				int current = v<T>.size();
				for (int k = 1; k <= current - index; k++) {
					//cout << v.back() << " ";
					v<T>.pop_back();
				}

			}
		}
	

}

template<class T>
void getSubset(T list[], int n) {
	//�ռ�����Ҫ�ݹ� ���м��� ��Ϊ�ռ����κμ��ϵ��Ӽ�
	cout << "{}" << endl;
	for (int i = 1; i <= n; i++) {
		subset(list, n, i, 0);
	}
}

int main() {
	//���� ��Ԫ�ظ���
	cout << "��ǰ���ϵ�����Ϊ:char���� ��Ҫ���ģ�����main������������" << endl;
	cout << "������ȫ������" << endl;
	cin >> n;
	char* arr = new char[n];
	cout << "������ȫ���ĸ���Ԫ��" << endl;
	char m;
	//��ʼ������
	for (int i = 0; i < n; i++) {
		cin >> m;
		arr[i]=m;
	}
	getSubset(arr, n);
	return 0;
}


