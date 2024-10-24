#include<string.h>
#include<iostream>
using namespace std;

//节点类
template<class T>
struct chainNode {
	//值域
	T element;
	//指针域
	chainNode<T>* next;

	chainNode(const T& element) {
		this->element = element;
	}
	chainNode(const T& element, chainNode<T>* next) {
		this->element = element;
		this->next = next;
	}
};

//链表类
template <class T>
class chain {
public:
	chain() {
		firstNode = NULL;
		ListSize = 0;
	};
	chain(const chain<T>& theList) {
		ListSize = theList.ListSize;
		if (ListSize == 0) {
			firstNode = NULL;
			return;
		}
		chainNode<T>* sourceNode = theList.firstNode;
		firstNode = new chainNode<T>(sourceNode->element);
		sourceNode = sourceNode->next;
		chainNode<T>* targetNode = firstNode;
		while (sourceNode != NULL) {
			targetNode->next = new chainNode<T>(sourceNode->element);
			sourceNode = sourceNode->next;
			targetNode = targetNode->next;
		}
		targetNode->next = NULL;
	};
	~chain() {
		while (firstNode != NULL) {
			chainNode<T>* nextNode = firstNode->next;
			delete firstNode;
			firstNode = nextNode;
		}
	};
	void add(T& element) {
		if (ListSize == 0) {
			firstNode = new chainNode<T>(element, NULL);
		}
		else {
			chainNode<T>* currentNode = firstNode;
			while (currentNode->next != NULL) {
				currentNode = currentNode->next;
			}
			currentNode->next = new chainNode<T>(element, NULL);
		}
		ListSize++;
	}
	//反转整个链表的函数
	void reverse() {
		//欲反转整个链表，需要反转第一个节点。
		chainNode<T>* node=reverseNode(firstNode);
		node->next = NULL;
		
	};
	//欲反转一个节点，需要反转他的下一个节点 
	//而反转下一个节点需要知道上一个节点的地址，因此返回一个node，令这个node指向上一个即可
	chainNode<T>* reverseNode(chainNode<T>* node) {
		//递归基，已经是尾节点，返回它
		if (node->next == NULL) {
			//尾节点成为头节点
			firstNode = node;
			return node;
		}
		//不是尾节点，需要将当前节点作为返回节点的上一个节点
		chainNode<T>* nextNode = reverseNode(node->next);
		nextNode->next = node;
		return node;
	}
	chainNode<T>* firstNode;
	int ListSize;
};

int main() {
	chain<int> chain;
	cout << "输入链表长度";
	int n; cin >> n;
	cout << "输入每个元素";
	for (int i = 1; i <= n; i++) {
		int element;
		cin >> element;
		chain.add(element);
	}
	chain.reverse();
	chainNode<int>* currentNode = chain.firstNode;
	for (int i = 1; i <= n; i++) {
		cout << currentNode->element << " ";
		currentNode = currentNode->next;
	}

}