#include<iostream>
#include<string.h>
using namespace std;

static int number = 1;

class Node {
public:
	Node* leftNode;
	Node* rightNode;
	int element;
public:
	Node() {
		leftNode = NULL;
		rightNode = NULL;
		element = NULL;
	}
	Node(int ele) {
		element = ele;
		leftNode = NULL;
		rightNode = NULL;
	}
	Node(int ele, Node* left, Node* right) {
		element = ele;
		leftNode = left;
		rightNode = right;
	}
};



class BinarySearchTree {
public:
	Node* root;
	int treeSize;
public:
	BinarySearchTree() {
		root = NULL;
		treeSize = 0;
	}

	void insert(int element) {
		Node* p = root;
		//ǰ��
		Node* pp = NULL;
		
		while (p != NULL) {
			//ǰ���³�
			pp = p;
			if (p->element > element) {
				//���֧Ѱ��
				p = p->leftNode;
			}
			else if (p->element < element) {
				//�ҷ�֧Ѱ��
				p = p->rightNode;
			}
			else {
				//����ֱ�ӷ��أ�û��Ҫ������
				return;
			}
		}

		Node* temp = new Node(element);
		if (root != NULL) {
			if (element < pp->element) {
				pp->leftNode = temp;
			}
			else {
				pp->rightNode = temp;
			}
		}
		else {
			root = temp;
		}
		treeSize++;
	}

	void visit(Node* node) {
		if (number != treeSize) {
			cout << node->element << ",";
			number++;
		}
		else {
			cout << node->element;
		}
	}

	void preOrder(Node* node) {
		if (node != NULL) {
			visit(node);
			preOrder(node->leftNode);
			preOrder(node->rightNode);
		}
	}

	void inOrder(Node* node) {
		if (node != NULL) {
			inOrder(node->leftNode);
			visit(node);
			inOrder(node->rightNode);
		}
	}
};

class maxHeap {
public:
	int* heap;
	int heapSize;
public:
	maxHeap() {
		heap = NULL;
		heapSize = 0;
	}
	void pop() {
		if (heapSize == 0) {
			return;
		}
		else {
			//ɾ�����ڵ� �ó���α������һ���ڵ�
			int last = heap[heapSize--];

			//��lastӦ�÷�������Ӹ��ڵ㿪ʼ��
			int current = 1;
			int child = 2;
			while (child <= heapSize) {

				//��֧�нϴ���Ǹ�����������
				if (child < heapSize && heap[child] < heap[child + 1]) {
					child++;
				}

				//������һ���ڵ���Է��ڵ�ǰλ�ã�������
				if (last >= heap[child]) {
					break;
				}

				//�ϴ�child�ϸ�
				heap[current] = heap[child];
				current = child;
				child *= 2;
			}
			heap[current] = last;
		}
	}

	void init(int* theHeap, int theSize) {
		delete[] heap;
		heap = theHeap;
		heapSize = theSize;

		//����Ӹ������
		for (int root = heapSize / 2; root >= 1; root--) {
			int rootEle = heap[root];

			int child = 2 * root;
			while (child <= heapSize) {
				if (child < heapSize && heap[child] < heap[child + 1]) {
					child++;
				}
				if (rootEle >= heap[child]) {
					break;
				}
				heap[child/2] = heap[child];
				child = child * 2;
			}
			heap[child / 2] = rootEle;
		}
	}

	int top() {
		if (heapSize == 0) {
			return -1;
		}
		else {
			return heap[1];
		}
	}
};

void heapSort(int* arr, int n) {
	maxHeap* heap = new maxHeap();
	heap->init(arr, n);

	for (int i = n - 1; i >= 1; i--) {
		int biggest = heap->top();
		heap->pop();
		arr[i + 1] = biggest;
	}
}

int main() {
	cout << "Input" << endl;
	int ele;
	int origin[20];
	for (int i = 0; i < 20; i++) {
		origin[i] = 0;
	}
	int count = 0;
	BinarySearchTree* bs = new BinarySearchTree();
	cin >> ele;
	while (ele != 0) {
		bs->insert(ele);
		origin[count++] = ele;
		cin >> ele;
	}
	//arr[1:n]
	int* arr = new int[count + 1];
	for (int i = 1; i <= count; i++) {
		arr[i] = origin[i - 1];
	}
	cout << "Output" << endl;
	maxHeap* mH = new maxHeap();
	mH->init(arr, count);
	cout << mH->heap[1];
	for (int i = 2; i <= count; i++) {
		cout << "," << mH->heap[i] ;
	}
	cout << endl;
	heapSort(arr,count);
	cout << arr[1];
	for (int i = 2; i <= count; i++) {
		cout << "," << arr[i] ;
	}
	cout << endl;
	bs->preOrder(bs->root);
	number = 1;
	cout << endl;
	bs->inOrder(bs->root);
	cout << endl;
	cout << "End";
	return 0;






}