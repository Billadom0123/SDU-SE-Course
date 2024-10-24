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
		cout << current->element << " ";
	}

	//���Ҷ�ӽڵ�
	void findLeaf(Node* start) {
		//ǰ��������������������������ξͲ�����
		//һ���ӽڵ㶼û��Ҷ�ӽڵ�
		if (start != NULL) {
			if (start->leftNode == NULL && start->rightNode == NULL) {
				visit(start);
			}
			findLeaf(start->leftNode);
			findLeaf(start->rightNode);
		}
	}

	//����ڵ�ļ�
	int computeLevel(char element,Node* start) {
		if (start != NULL && start->element == element) {
			return 1;
		}
		else if (start == NULL) {
			//�����֧�����˶�û����
			return -1;
		}
		int pl = computeLevel(element, start->leftNode);
		int pr = computeLevel(element, start->rightNode);
		//���û�ҵ��ұ��ҵ���
		if (pl == -1 && pr != -1) {
			return ++pr;
		}
		//�ұ�û�ҵ�����ҵ���
		else if (pl != -1 && pr == -1) {
			return ++pl;
		}
		//����û�ҵ�
		else {
			return -1;
		}
	}

	//���ҵ�ָ���ڵ��ǰһ������
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
		//�����֧û������ڵ�
		else {
			//һ��Сtrick
			return 0;
		}
	}

	//�ҵ������ڵ�������ͬ����
	char findCommonAncestor(char e1, char e2) {
		//�ҳ������ڵ�ļ��������ͬ���������ߵ�Ҫ���������͵���һ��
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
				cout << e1 << "�����ڣ�" << endl;
			}
			if (level2 == -1) {
				cout << e2 << "�����ڣ�" << endl;
			}
			return NULL;
		}

	}

};

int main() {
	cout << "��ʾ:����һ��char���͵���ȫ�����������⣬�������ڵ�ļ�������-1��˵���������û������ڵ㡣" << endl;
	cout << "�������õ����Ĳ�������ַ���Ϊ:abcde" << endl;
	Tree* tree = new Tree();
	tree->add('a');
	tree->add('b');
	tree->add('c');
	tree->add('d');
	tree->add('e');

	cout << "Ҷ�ӽڵ��������Ϊ: ";
	tree->findLeaf(tree->root);
	cout << endl;
	int levelE=tree->computeLevel('e',tree->root);
	int levelC = tree->computeLevel('c', tree->root);
	int levelA = tree->computeLevel('a', tree->root);
	int levelF=tree->computeLevel('f',tree->root);
	if (levelE != -1) {
		cout << "�ڵ�e�ļ�Ϊ" << levelE << endl;
	}
	cout << "�ڵ�c�ļ�Ϊ" << levelC << endl;
	cout << "�ڵ�a�ļ�Ϊ" << levelA << endl;
	if (levelF == -1) {
		cout << "�޽ڵ�f!" << endl;
	}

	char DE=tree->findCommonAncestor('d', 'e');
	cout << "�ڵ�d��e������Ĺ�ͬ����Ϊ" << DE << endl;
	char DC = tree->findCommonAncestor('d', 'c');
	cout << "�ڵ�d��c������Ĺ�ͬ����Ϊ" << DC << endl;
	char EA = tree->findCommonAncestor('e', 'a');
	cout << "�ڵ�a��e������Ĺ�ͬ����Ϊ" << EA << endl;
	//cout << "�ڵ�f��e������Ĺ�ͬ����Ϊ" << tree->findCommonAncestor('f', 'e') << endl;
	char FE = tree->findCommonAncestor('f', 'e');
	if (FE == NULL) {
		cout << "�������룬������һ��Ԫ���ǲ����ڵģ�" << endl;
	}
	

	//cout << tree->findAncestor(tree->findAncestor('e', tree->root),tree->root) << endl;
	//cout << tree->findAncestor('a', tree->root) << 1;

}