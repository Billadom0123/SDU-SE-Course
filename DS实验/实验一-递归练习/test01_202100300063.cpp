#include<iostream>
using namespace std;

//存储数组长度
static int k = 0;

// start通知函数目前可供选取元素的首个位置
void getPermutation(int list[], int start) {
	//递归基 当向量长度等于数组长度时，即为全排列
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
		//循环选取一个元素作为当前子列的头部
		for (int i = start; i < k; i++) {
			//为了保证字典序 让这个元素前面所有的元素向后挪一格 然后把这个元素放到头部
			int temp = list[i];
			for (int j = i; j > start; j--) {
				list[j] = list[j - 1];
			}
			list[start] = temp;
			//进行递归
			int nextStart = start + 1;
			getPermutation(list, nextStart);
			//保证字典序 还得把这个元素扔回去 实现方式是把i之前的除了头部的元素统统往前挪一格
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
	//初始化数组
	for (int i = 0; i < 20; i++) {
		origin[i] = 0;
	}
	cin >> n;
	//赋值
	while (n != 0) {
		origin[k++] = n;
		//cout << n << endl;
		cin >> n;
	}
	//把数组中的多余项全部去掉
	//对于k 如果输入1个有效值 k=1 输入2个有效值 k=2 ... 因此初始化k个元素
	int* list = new int[k];
	for (int i = 0; i < k; i++) {
		list[i] = origin[i];
	}

	cout << "Output" << endl;
	//一开始调用start是0，即整个数组的元素都可以成为全排列的头部
	getPermutation(list, 0);

	cout << "End" << endl;
	return 0;
}
