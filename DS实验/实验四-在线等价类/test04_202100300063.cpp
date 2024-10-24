#include<iostream>
#include<string>
#include<algorithm>
using namespace std;

//节点类，模拟指针
class Node {
public:
	int equiv;
	//指针域，模拟指针
	int next;
	//该元素后还有几个元素
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
	//初始等价类
	for (int i = 1; i <= n; i++) {
		arr[i].equiv = i;
	}
	int r;
	cin >> r;
	for (int i = 0; i < r; i++) {
		//获取等价关系
		string str;
		cin >> str;
		char c1 = str.at(1);
		char c2 = str.at(3);
		int num1 = c1 - 48;
		int num2 = c2 - 48;
		//如果两个元素同属于一个等价类，那就没有必要合并了
		if (arr[num1].equiv == arr[num2].equiv) {
			continue;
		}
		// 首先要确定被合并的是哪个等价类
		num1 = arr[num1].equiv;
		num2 = arr[num2].equiv;
		//合并元素
		//把较小的等价类合并到较大的等价类中，选择num2作为较小的等价类
		if (arr[num1].size < arr[num2].size) {
			int temp = num1;
			num1 = num2;
			num2 = temp;
		}
		//等价类插入头部
		//首先，找到被合并的等价类中的最后一个元素
		Node current = arr[num2];
		//最后一个元素的数组索引final，如果这个被合并的等价类只有一个元素，进不了while循环，那么final就是这个等价类的equiv
		int final = num2;
		while (current.next != 0) {
			current = arr[current.next];
			//不断更新final
			final = current.next;
		}
		//最后一个元素指向待插入等价类头部的下一个元素
		arr[final].next = arr[num1].next;
		//Node arrFinal = arr[final];
		//被插入等价类头部指向待插入等价类头部
		arr[num1].next = num2;
		//修改新的等价类的size
		arr[num1].size += arr[num2].size;
		//Node arrNum1 = arr[num1];
		//修改被插入等价类的equiv
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
		//每次判断当前数组元素是否是等价类的入口
		//判断规则为：如果它是等价类入口，那么equiv应该等于索引，否则意味着他被合并了
		if (arr[i].equiv == i) {
			int* equivalent = new int[arr[i].size];
			//初始化结果数组
			for (int j = 0; j < arr[i].size; j++) {
				equivalent[j] = 0;
			}
			//等价类头部
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
