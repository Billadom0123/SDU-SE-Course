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

//��������
class LinkedList {
public:
	ChainNode* firstNode;
public:
	LinkedList() {
		firstNode = new ChainNode(NULL,NULL);
	}
	//�����������Ԫ��
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
			//�����ǰ�ڵ����һ���ڵ���ڵ����ˣ�˵���ҵ��˵�һ�����ڵ���Ҫ����Ԫ�صĽڵ㣬��Ҫ�����Ԫ����ӵ�ǰ��
			//Ҳ�п��ܵ�ǰ������û�б����Ԫ�ػ�Ҫ���Ԫ���ˣ���˵��Ҫ���뵽����λ��
			if(current!=firstNode){
				if (current->value >= element) {
					ChainNode* newNode = new ChainNode(element, current);
					previous->next = newNode;
					flag = true;
					break;
				}
			}
			//����Ѱ��
			previous = current;
		}
		if (!flag) {
			current->next = new ChainNode(element, NULL);
		}
	}

	//�����������Ԫ��
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

	//��������ϲ�
	void merge(LinkedList* list) {
		//ָ����������ָ��
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