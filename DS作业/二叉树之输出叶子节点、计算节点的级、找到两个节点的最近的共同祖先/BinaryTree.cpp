#include<iostream>
#include<string.h>
#include<queue>
using namespace std;

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
public:
	Tree() {
		root = NULL;
		treeSize = 0;
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
		cout << current->element << " ";
	}

	//输出叶子节点
	void findLeaf(Node* start) {
		//前序遍历可以做到从左到右输出，层次就不行了
		//一个子节点都没是叶子节点
		if (start != NULL) {
			if (start->leftNode == NULL && start->rightNode == NULL) {
				visit(start);
			}
			findLeaf(start->leftNode);
			findLeaf(start->rightNode);
		}
	}

	//计算节点的级
	int computeLevel(char element,Node* start) {
		if (start != NULL && start->element == element) {
			return 1;
		}
		else if (start == NULL) {
			//这个分支走完了都没遇见
			return -1;
		}
		int pl = computeLevel(element, start->leftNode);
		int pr = computeLevel(element, start->rightNode);
		//左边没找到右边找到了
		if (pl == -1 && pr != -1) {
			return ++pr;
		}
		//右边没找到左边找到了
		else if (pl != -1 && pr == -1) {
			return ++pl;
		}
		//俩都没找到
		else {
			return -1;
		}
	}

	//先找到指定节点的前一个祖先
	char findAncestor(char e,Node* node) {
		if (e == root->element) {
			return root->element;
		}
		if (node != NULL) {
			if(node->leftNode!=NULL&&node->leftNode->element==e){
				return node->element;
			}
			else if (node->rightNode != NULL && node->rightNode->element == e) {
				return node->element;
			}
			char c1 = findAncestor(e, node->leftNode);
			char c2 = findAncestor(e, node->rightNode);
			return c1 + c2;
		}
		//这个分支没有这个节点
		else {
			//一个小trick
			return 0;
		}
	}

	//找到两个节点的最近共同祖先
	char findCommonAncestor(char e1, char e2) {
		//找出两个节点的级，如果不同级，级更高的要爬到级更低的那一级
		int level1=computeLevel(e1,root);
		int level2 = computeLevel(e2, root);
		if (level1 != -1 && level2 != -1) {
			if (level1 > level2) {
				while (level1 != level2) {
					e1 = findAncestor(e1, root);
					level1--;
				}
			}
			else if (level2 > level1) {
				while (level2 != level1) {
					e2 = findAncestor(e2, root);
					level2--;
				}
			}
			char ancestor1 = findAncestor(e1, root);
			char ancestor2 = findAncestor(e2, root);
			while (ancestor1 != ancestor2) {
				ancestor1 = findAncestor(ancestor1, root);
				ancestor2 = findAncestor(ancestor2, root);
			}
			return ancestor1;
		}
		else {
			if (level1 == -1) {
				cout << e1 << "不存在！" << endl;
			}
			if (level2 == -1) {
				cout << e2 << "不存在！" << endl;
			}
			return NULL;
		}

	}

};

int main() {
	cout << "提示:这是一颗char类型的完全二叉树。此外，如果计算节点的级返回了-1，说明这棵树里没有这个节点。" << endl;
	cout << "测试所用的树的层序遍历字符串为:abcde" << endl;
	Tree* tree = new Tree();
	tree->add('a');
	tree->add('b');
	tree->add('c');
	tree->add('d');
	tree->add('e');

	cout << "叶子节点从左至右为: ";
	tree->findLeaf(tree->root);
	cout << endl;
	int levelE=tree->computeLevel('e',tree->root);
	int levelC = tree->computeLevel('c', tree->root);
	int levelA = tree->computeLevel('a', tree->root);
	int levelF=tree->computeLevel('f',tree->root);
	if (levelE != -1) {
		cout << "节点e的级为" << levelE << endl;
	}
	cout << "节点c的级为" << levelC << endl;
	cout << "节点a的级为" << levelA << endl;
	if (levelF == -1) {
		cout << "无节点f!" << endl;
	}

	char DE=tree->findCommonAncestor('d', 'e');
	cout << "节点d和e的最近的共同祖先为" << DE << endl;
	char DC = tree->findCommonAncestor('d', 'c');
	cout << "节点d和c的最近的共同祖先为" << DC << endl;
	char EA = tree->findCommonAncestor('e', 'a');
	cout << "节点a和e的最近的共同祖先为" << EA << endl;
	//cout << "节点f和e的最近的共同祖先为" << tree->findCommonAncestor('f', 'e') << endl;
	char FE = tree->findCommonAncestor('f', 'e');
	if (FE == NULL) {
		cout << "请检查输入，至少有一个元素是不存在的！" << endl;
	}
	

	//cout << tree->findAncestor(tree->findAncestor('e', tree->root),tree->root) << endl;
	//cout << tree->findAncestor('a', tree->root) << 1;

}