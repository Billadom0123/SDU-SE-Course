#include<iostream>
#include<string.h>
using namespace std;

class Node {
public:
	int value;
	Node* next;
public:
	Node() {
		this->value = NULL;
		this->next = NULL;
	}
	Node(int element, Node* next) {
		this->value = element;
		this->next = next;
	}
};

class LinkedList {
public:
	Node* firstNode;
	//给个长度方便特判
	int size;
public:
	LinkedList() {
		this->firstNode = new Node(NULL,NULL);
		this->size = 0;
	}
	void add(int element) {
		if (size == 0) {
			firstNode->value = element;
			size++;
			return;
		}
		else {
			size++;
			if (firstNode->value > element) {
				Node* temp = new Node(firstNode->value, firstNode->next);
				firstNode = new Node(element, temp);
				return;
			}
			Node* current = firstNode;
			Node* previous = firstNode;
			while (current->next != NULL) {
				current = current->next;
				if (current->value > element) {
					Node* newNode = new Node(element,current);
					previous->next = newNode;
					size++;
					return;
				}
				previous = current;
			}
			current->next = new Node(element, NULL);
			size++;
		}
	}
	void cancel(int element) {
		if (firstNode->value == element) {
			firstNode = firstNode->next;
			return;
		}
		Node* current = firstNode;
		Node* previous = firstNode;
		while (current->next != NULL) {
			current = current->next;
			if (current->value == element) {
				previous->next = current->next;
				delete current;
				size--;
				return;
			}
			previous = current;
		}
	}
	void printLinkedList() {
		Node* temp = firstNode;
		while (temp->next != NULL) {
			cout << temp->value << ",";
			temp = temp->next;
		}
		cout << temp->value << endl;
	}
};

int main() {
	cout << "Input1" << endl;
	LinkedList chain;
	int n;
	cin >> n;
	while (n != 0) {
		chain.add(n);
		cin >> n;
	}
	cout << "Output1" << endl;
	chain.printLinkedList();
	cout << "Input2" << endl;
	cin >> n;
	chain.cancel(n);
	cout << "Output2" << endl;
	chain.printLinkedList();
	cout << "Input3" << endl;
	cin >> n;
	chain.cancel(n);
	cout << "Output3" << endl;
	chain.printLinkedList();
	cout << "End";
	return 0;
	


}