#include<iostream>
using namespace std;

static int k=0;
//�鲢����ĸ�������
static int* assist;


//ð�������˼·:ÿ�ΰѵ�ǰ����������Ԫ���õ����������ô�����Ȼ������ġ�
//���ѭ�������ִΣ��k-1�֡��ڲ�ѭ����֤�������Ԫ�������
void BubbleSort(int* arr) {
	//��ʱ��ֹ����¼��ǰ�Ƿ��Ѿ�����
	bool swapped = true; //Ϊ�˽�ѭ����Ҫ������Ϊtrue
	//�����������һ��ʼ������ģ����Ҫk-1�β��ܰ�����Ԫ������
	for (int i = 0; i < k - 1&&swapped; i++) { //�����һ�ξ����˽�������������򣬻���Ҫ��
		//ÿ�ζ���Ԥ���Ѿ�����
		swapped = false;
		//ÿһ�����򣬶�������ͷ����ʼ���������ѳ����Ѿ��źõ�Ԫ����������Ԫ���ҳ���
		for (int j = 0; j < k - i-1; j++) {
			if (arr[j] > arr[j + 1]) {
				int temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
				swapped = true;
			}
		}
	}
}

//���������˼·:��֤��ѭ������ʱ��ǰ����������ģ�����
// �ں�һ��Ԫ���ƶ�ʱ��ֻҪ�ñ��ƶ���Ԫ��Ҳ��˳����룬��ô��һ��Ҳ������ģ������������ġ�
//ͨ�����ѭ���������г����������ڲ�ѭ����֤��������
void InsertSort(int* arr) {
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

void merge(int* arr,int low,int mid,int high) {
	//�ϲ�˼·:ǰ��ε�ǰԪ�غͺ��ε�ǰԪ�رȽϣ�ȡС�ĳ����ŵ��������顣
	//ֱ������һ�����꣬ʣ�µ���Ϊ֮ǰ����ʱ�Ѿ����򣬰�˳����ں��漴�ɡ�
	int i = low;
	//����ָ�룬�ֱ����ǰ���ε�Ԫ��
	int p1 = low;
	int p2 = mid + 1;
	while (p1 <= mid && p2 <= high) {
		if (arr[p1] < arr[p2]) {
			assist[i++] = arr[p1++];
		}
		else {
			assist[i++] = arr[p2++];
		}
	}
	//ʣ��Ԫ�ط��븨������
	while (p1 <= mid) {
		assist[i++] = arr[p1++];
	}

	while (p2 <= high) {
		assist[i++] = arr[p2++];
	}

	for (int j = low; j <= high; j++) {
		arr[j] = assist[j];
	}
}

void sort(int* arr, int low, int high) {
	//�ݹ�� ͬʱ�ǰ�ȫ��У�� ��log=highʱ������Ҫ�ٷ���
	if (low >= high) {
		return;
	}
	int mid = (low + high) / 2;

	//ǰ��η���
	sort(arr, low, mid);
	//���η���
	int nextLow = mid + 1;
	sort(arr,nextLow, high);

	//ǰ��κ��κϲ�
	merge(arr, low, mid, high);
}

//�鲢�����˼·:���Ρ�ÿ�ηֳ����룬���ϲ�ʱ��˳��鲢��
//��������ʱ����������������ٹ鲢����������һֱ���������顣
void MergeSort(int* arr) {
	assist = new int[k];
	for (int i = 0; i < k; i++) {
		assist[i] = 0;
	}
	int low = 0;
	int high = k - 1;
	sort(arr, low, high);
}



int main() {
	//�������18�������� ��������
	cout << "Input" << endl;
	int n;
	cin >> n;
	int* origin = new int[18];
	//��ʼ������
	for (int i = 0; i < 18; i++) {
		origin[i] = 0;
	}
	//����Ԫ��
	while (n != 0 && k < 18) {
		origin[k++] = n;
		cin >> n;
	}
	//��������
	int* arr = new int[k];
	for (int i = 0; i < k; i++) {
		arr[i] = origin[i];
	}
	cout << "1-Bubble Sort,2-Insert Sort,3-Merge Sort" << endl;
	cin >> n;
	cout << "Output" << endl;
	if (n == 1) {
		cout << "Bubble Sort" << endl;
		BubbleSort(arr);
	}
	else if(n==2) {
		cout << "Insert Sort" << endl;
		InsertSort(arr);
	}
	else {
		cout << "Merge Sort" << endl;
		MergeSort(arr);
	}

	for (int i = 0; i < k; i++) {
		if (i == k - 1) {
			cout << arr[i];
		}
		else {
			cout << arr[i] << ",";
		}
	}

	cout << endl;

	cout << "End";

	return 0;
}