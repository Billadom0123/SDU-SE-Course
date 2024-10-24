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
	//增加节点
	void add(char ele) {
		//自上向下自左向右层次遍历，第一个没有左/右子节点的节点添加
		queue<Node*> q;
		Node* current = root;
		//空树增加节点
		if (root == NULL) {
			root = new Node(ele);
		}
		while (current != NULL) {
			//添加
			if (current->leftNode == NULL) {
				current->leftNode = new Node(ele);
				break;
			}
			else if (current->rightNode == NULL) {
				current->rightNode = new Node(ele);
				break;
			}
			//加不上继续遍历
			else {
				//入队
				q.push(current->leftNode);
				q.push(current->rightNode);
				//不用判断队列是不是空了，如果空了肯定加上了
				current = q.front();
				q.pop();
			}
		}
		treeSize++;
	}

	void visit(Node* current) {
		cout << current->element << ",";
	}

	//前序遍历
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

	//中序遍历
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

	//后序遍历
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

	//获取高度
	int height(Node* start) {
		//递归基:空子树，高度是0
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

//后序遍历
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
	//递归基:二级子树三个元素
	if (right - left <= 2) {
		//区间中间的元素成为
		//索引向上取整
		int middle;
		//小数向上取整
		if ((left + right) % 2 != 0) {
			middle = (left + right) / 2 + 1;
		}
		else {
			middle = (left + right) / 2;
		}

		Node* middleNode = new Node(inStr.at(middle));
		//可能没有左节点或右节点
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
		//不是二级子树，接收下一层递归返回的子树根节点，和当前子树根节点连起来
		int middle;
		//小数向上取整
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
	//preStr获取根节点
	char c = preStr.at(0);
	int index;
	int left = 0;
	int right = inStr.length()-1;
	//在inStr中找到这个字符
	for (int i = 0; i < inStr.length(); i++) {
		if (inStr.at(i) == c) {
			index = i;
			break;
		}
	}

	//给出二分区间 返回子树根节点
	
	Node* leftNode = NULL;
	if (index - 1 >= 0) {
		leftNode = Build(left, index - 1, inStr);
	}
	Node* rightNode = NULL;
	if (index + 1 < inStr.length()) {
		rightNode = Build(index + 1, right, inStr);
	}
	//根节点与子树根节点相连
		//创建根节点
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
	//前序遍历
	//这个格式不能多逗号，只能手动前序根节点了
	tree->preOrder(tree->root);
	tree->count = 1;
	//中序遍历
	tree->inOrder(tree->root);
	tree->count = 1;
	//后序遍历
	tree->postOrder(tree->root);
	tree->count = 1;
	//结点数
	cout << tree->treeSize << endl;
	//高度
	cout << tree->height(tree->root) << endl;

	cout << "Input2" << endl;
	string preStr, inStr;
	cin >> preStr >> inStr;

	//给出前序中序 构建二叉树
	Node* root=BuildTree(preStr, inStr);
	cout << "Output2" << endl;
	postOrder(root,inStr.length());
	num = 1;
	levelOrder(root, inStr.length());
	cout << "End";
	return 0;
}