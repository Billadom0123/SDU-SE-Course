#include<string.h>
#include<iostream>
using namespace std;

//�ڵ���
template<class T>
struct chainNode {
	//ֵ��
	T element;
	//ָ����
	chainNode<T>* next;

	chainNode(const T& element) {
		this->element = element;
	}
	chainNode(const T& element, chainNode<T>* next) {
		this->element = element;
		this->next = next;
	}
};

//������
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
	//��ת��������ĺ���
	void reverse() {
		//����ת����������Ҫ��ת��һ���ڵ㡣
		chainNode<T>* node=reverseNode(firstNode);
		node->next = NULL;
		
	};
	//����תһ���ڵ㣬��Ҫ��ת������һ���ڵ� 
	//����ת��һ���ڵ���Ҫ֪����һ���ڵ�ĵ�ַ����˷���һ��node�������nodeָ����һ������
	chainNode<T>* reverseNode(chainNode<T>* node) {
		//�ݹ�����Ѿ���β�ڵ㣬������
		if (node->next == NULL) {
			//β�ڵ��Ϊͷ�ڵ�
			firstNode = node;
			return node;
		}
		//����β�ڵ㣬��Ҫ����ǰ�ڵ���Ϊ���ؽڵ����һ���ڵ�
		chainNode<T>* nextNode = reverseNode(node->next);
		nextNode->next = node;
		return node;
	}
	chainNode<T>* firstNode;
	int ListSize;
};

int main() {
	chain<int> chain;
	cout << "����������";
	int n; cin >> n;
	cout << "����ÿ��Ԫ��";
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