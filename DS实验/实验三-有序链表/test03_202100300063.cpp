#include<iostream>
#include<string.h>
using namespace std;

class ChainNode {
public:
	int value;
	ChainNode* next;
public:
	ChainNode(int value, ChainNode* next) {
		this->value = value;
		this->next = next;
	}
};

//有序链表
class LinkedList {
public:
	ChainNode* firstNode;
public:
	LinkedList() {
		firstNode = new ChainNode(NULL,NULL);
	}
	//有序链表添加元素
	void add(int element) {
		if (firstNode->next == NULL) {
			firstNode->next = new ChainNode(element, NULL);
			return;
		}
		ChainNode* current = firstNode;
		ChainNode* previous = firstNode;
		bool flag = false;
		while (current->next != NULL) {
			current = current->next;
			//如果当前节点的下一个节点大于等于了，说明找到了第一个大于等于要加入元素的节点，把要加入的元素添加到前面
			//也有可能当前链表里没有比这个元素还要大的元素了，这说明要插入到最后的位置
			if(current!=firstNode){
				if (current->value >= element) {
					ChainNode* newNode = new ChainNode(element, current);
					previous->next = newNode;
					flag = true;
					break;
				}
			}
			//后移寻找
			previous = current;
		}
		if (!flag) {
			current->next = new ChainNode(element, NULL);
		}
	}

	//有序链表查找元素
	int IndexOf(int element) {
		int i = 0;
		ChainNode* current = firstNode;
		while (current != NULL) {
			if (current->value == element) {
				return i;
			}
			current = current->next;
			i++;
		}
		return 0;
	}

	//有序链表合并
	void merge(LinkedList* list) {
		//指向参数链表的指针
		ChainNode* current = list->firstNode->next;
		while (current != NULL) {
			this->add(current->value);
			current = current->next;
		}
	}

};

int main() {
	cout << "Input1" << endl;
	int n;
	cin >> n;
	LinkedList* list = new LinkedList();
	while (n != 0) {
		list->add(n);
		cin >> n;
	}
	cout << "Output1" << endl;
	ChainNode* current = list->firstNode->next;
	while (current->next != NULL) {
		cout << current->value << ",";
		current = current->next;
	}
	cout << current->value << endl;

	cout << "Input2" << endl;
	cin >> n;
	list->add(n);
	cout << "Output2" << endl;
	current = list->firstNode->next;
	while (current->next != NULL) {
		cout << current->value << ",";
		current = current->next;
	}
	cout << current->value << endl;

	cout << "Input3" << endl;
	cin >> n;
	cout << "Output3" << endl;
	cout << list->IndexOf(n) << endl;

	cout << "Input4" << endl;
	cin >> n;
	cout << "Output4" << endl;
	cout << list->IndexOf(n) << endl;

	cout << "Input5" << endl;
	cin >> n;
	LinkedList* newList = new LinkedList();
	while (n != 0) {
		newList->add(n);
		cin >> n;
	}
	cout << "Output5" << endl;
	current = newList->firstNode->next;
	while (current->next != NULL) {
		cout << current->value << ",";
		current = current->next;
	}
	cout << current->value << endl;
	list->merge(newList);
	current = list->firstNode->next;
	while (current->next != NULL) {
		cout << current->value << ",";
		current = current->next;
	}
	cout << current->value << endl;
	cout << "End";

}