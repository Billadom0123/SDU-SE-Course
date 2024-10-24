#include<iostream>
#include<queue>
#include<string.h>
using namespace std;

static int count=0;

class Node {
public:
	Node* leftNode;
	Node* rightNode;
	char element;
public:
	Node() {
		leftNode = NULL;
		rightNode = NULL;
		element = NULL;
	}
	Node(char ele) {
		element = ele;
		leftNode = NULL;
		rightNode = NULL;
	}
	Node(char ele, Node* left, Node* right) {
		element = ele;
		leftNode = left;
		rightNode = right; 
	}
};

class Tree {
public:
	Node* root;
	int treeSize;
	int count;
public:
	Tree() {
		root = NULL;
		treeSize = 0;
		count = 1;
	}
	//���ӽڵ�
	void add(char ele) {
		//���������������Ҳ�α�������һ��û����/���ӽڵ�Ľڵ����
		queue<Node*> q;
		Node* current = root;
		//�������ӽڵ�
		if (root == NULL) {
			root = new Node(ele);
		}
		while (current != NULL) {
			//���
			if (current->leftNode == NULL) {
				current->leftNode = new Node(ele);
				break;
			}
			else if (current->rightNode == NULL) {
				current->rightNode = new Node(ele);
				break;
			}
			//�Ӳ��ϼ�������
			else {
				//���
				q.push(current->leftNode);
				q.push(current->rightNode);
				//�����ж϶����ǲ��ǿ��ˣ�������˿϶�������
				current = q.front();
				q.pop();
			}
		}
		treeSize++;
	}

	void visit(Node* current) {
		cout << current->element << ",";
	}

	//ǰ�����
	void preOrder(Node* start) {
		if (start != NULL) {
			if (count != treeSize) {
				visit(start);
			}
			else {
				cout << start->element << endl;
			}
			count++;
			preOrder(start->leftNode);
			preOrder(start->rightNode);
		}
	}

	//�������
	void inOrder(Node* start) {
		if (start != NULL) {
			inOrder(start->leftNode);
			if (count != treeSize) {
				visit(start);
			}
			else {
				cout << start->element << endl;
			}
			count++;
			inOrder(start->rightNode);
		}
	}

	//�������
	void postOrder(Node* start) {
		if (start != NULL) {
			postOrder(start->leftNode);
			postOrder(start->rightNode);
			if (count != treeSize) {
				visit(start);
			}
			else {
				cout << start->element << endl;
			}
			count++;
		}
	}

	//��ȡ�߶�
	int height(Node* start) {
		//�ݹ��:���������߶���0
		if (start == NULL) {
			return 0;
		}
		int hl = height(start->leftNode);
		int hr = height(start->rightNode);
		if (hl > hr) {
			return ++hl;
		}
		else {
			return ++hr;
		}
	}
};

static int num = 1;;

void visit(Node* current) {
	cout << current->element << ",";
}

//�������
void postOrder(Node* start,int treeSize) {
	if (start != NULL) {
		postOrder(start->leftNode,treeSize);
		postOrder(start->rightNode,treeSize);
		if (num != treeSize) {
			visit(start);
		}
		else {
			cout << start->element << endl;
		}
		num++;
	}
}

void levelOrder(Node* start,int treeSize) {
	queue<Node*> q;
	while (start != NULL) {
		if (num != treeSize) {
			visit(start);
		}
		else {
			cout << start->element << endl;
		}
		if (start->leftNode != NULL) {
			q.push(start->leftNode);
		}
		if (start->rightNode != NULL) {
			q.push(start->rightNode);
		}

		if (q.empty()) {
			return;
		}
		else {
			start = q.front();
		}
		q.pop();
		num++;
	}
}

Node* Build(int left, int right, string inStr) {
	//�ݹ��:������������Ԫ��
	if (right - left <= 2) {
		//�����м��Ԫ�س�Ϊ
		//��������ȡ��
		int middle;
		//С������ȡ��
		if ((left + right) % 2 != 0) {
			middle = (left + right) / 2 + 1;
		}
		else {
			middle = (left + right) / 2;
		}

		Node* middleNode = new Node(inStr.at(middle));
		//����û����ڵ���ҽڵ�
		if (middle - 1 == left) {
			Node* leftNode = new Node(inStr.at(middle - 1));
			middleNode->leftNode = leftNode;
		}
		if (middle + 1 == right) {
			Node* rightNode = new Node(inStr.at(middle + 1));
			middleNode->rightNode = rightNode;
		}
		return middleNode;
	}
	else {
		//���Ƕ���������������һ��ݹ鷵�ص��������ڵ㣬�͵�ǰ�������ڵ�������
		int middle;
		//С������ȡ��
		if ((left + right) % 2 != 0) {
			middle = (left + right) / 2 + 1;
		}
		else {
			middle = (left + right) / 2;
		}
		Node* middleNode = new Node(inStr.at(middle));
		Node* leftNode = Build(left, middle - 1, inStr);
		Node* rightNode = Build(middle + 1, right, inStr);
		middleNode->leftNode = leftNode;
		middleNode->rightNode = rightNode;
	}
}

Node* BuildTree(string preStr, string inStr) {
	//preStr��ȡ���ڵ�
	char c = preStr.at(0);
	int index;
	int left = 0;
	int right = inStr.length()-1;
	//��inStr���ҵ�����ַ�
	for (int i = 0; i < inStr.length(); i++) {
		if (inStr.at(i) == c) {
			index = i;
			break;
		}
	}

	//������������ �����������ڵ�
	
	Node* leftNode = NULL;
	if (index - 1 >= 0) {
		leftNode = Build(left, index - 1, inStr);
	}
	Node* rightNode = NULL;
	if (index + 1 < inStr.length()) {
		rightNode = Build(index + 1, right, inStr);
	}
	//���ڵ����������ڵ�����
		//�������ڵ�
	Node* root = new Node(c,leftNode,rightNode);

	return root;
}




int main() {
	string str;
	cout << "Input1" << endl;
	cin >> str;
	Tree* tree = new Tree();
	for (int i = 0; i < str.length(); i++) {
		char c = str.at(i);
		tree->add(c);
	}
	cout << "Output1" << endl;
	//ǰ�����
	//�����ʽ���ܶය�ţ�ֻ���ֶ�ǰ����ڵ���
	tree->preOrder(tree->root);
	tree->count = 1;
	//�������
	tree->inOrder(tree->root);
	tree->count = 1;
	//�������
	tree->postOrder(tree->root);
	tree->count = 1;
	//�����
	cout << tree->treeSize << endl;
	//�߶�
	cout << tree->height(tree->root) << endl;

	cout << "Input2" << endl;
	string preStr, inStr;
	cin >> preStr >> inStr;

	//����ǰ������ ����������
	Node* root=BuildTree(preStr, inStr);
	cout << "Output2" << endl;
	postOrder(root,inStr.length());
	num = 1;
	levelOrder(root, inStr.length());
	cout << "End";
	return 0;
}