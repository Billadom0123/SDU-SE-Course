#include<iostream>
#include<string.h>
#include<algorithm>
using namespace std;

//插入排序的思路:保证在循环遍历时当前子列是有序的，这样
// 在后一轮元素移动时，只要让被移动的元素也按顺序插入，那么后一轮也是有序的，整体就是有序的。
//通过外层循环控制子列长度增长。内层循环保证子列有序。
void InsertSort(int* arr,int k) {
	//可惜的是，插入排序不能像冒泡排序一样及时终止。这是因为哪怕上一轮没移动，下一轮仍然可能移动元素
	//注意这里数组最后的元素也要参与插入过程
	for (int i = 0; i < k; i++) {
		//子列逆序遍历时为了比较和前一个元素的大小，j不能为0，否则j-1<0
		for (int j = i; j > 0; j--) {
			if (arr[j] < arr[j - 1]) {
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
			else {
				//如果上一次比较都没交换，因为原本就是有序的，说明下一次一定不会交换，直接跳出去
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
	//n个等价类
	for (int i = 1; i <= n; i++) {
		arr[i] = Node(i, 0);
	}
	cin >> n;
	string str;
	for (int i = 0; i < n; i++) {
		cin >> str;
		//拿到两个要合并的元素
		int num1 = str.at(1) - 48;
		int num2 = str.at(3) - 48;
		//找到这两个元素的等价类
		int equiv1 = arr[num1].equiv;
		int equiv2 = arr[num2].equiv;
		//也不管哪个大了，反正数据量都挺小的。
		//把第二个合并到第一个里去。
		if (equiv1 == equiv2) {
			//已经同一个了跳过
			continue;
		}
		else {
			//拿到第二个等价类的头指针。向后遍历找尾部节点
			Node* current = &arr[equiv2];
			while (current->next != 0) {
				//修改等价类
				current->equiv = equiv1;
				current = &arr[current->next];
			}
			current->equiv = equiv1;
			//先让第二个等价类尾部连接到第一个等价类的第二个元素上
			current->next = arr[equiv1].next;
			//再让第一个等价类的头元素连接到第二个等价类的头元素上
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