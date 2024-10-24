#include<iostream>
#include<string.h>
using namespace std;

class AdjacencyWDigraph {
public:
	int** graph;
	int n;//n�Ƕ������
public:
	//��ʼ��
	AdjacencyWDigraph(int number) {
		this->n = number;
		graph = new int* [n + 1];
		for (int i = 0; i <= n; i++) {
			graph[i] = new int[n + 1];
		}
		for (int i = 0; i <= n; i++) {
			for (int j = 0;j <= n; j++) {
				graph[i][j] = 0;
			}
		}
	}
	//��ӱ�,��v1��v2��Ȩ��Ϊweight
	void addEdge(int v1, int v2, int weight) {
		graph[v1][v2] = weight;
	}
	void dfs(int v, int reach[]) {
		cout << v << " ";
		reach[v] = 1;//Ĭ��0����1��
		for (int j = 1; j <= n; j++) {
			//jû��������v��j�б�
			if (reach[j] == 0 && graph[v][j] != 0) {
				dfs(j,reach);
			}
		}
	}
	//DFS,��start��ʼ
	void DFSForAdjacencyWDigraph(int start) {
		int* reach = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			reach[i] = 0;
		}
		dfs(start, reach);
	}
		
};

class Node {
public:
	int element;
	Node* next;
public:
	Node() {
		this->element = 0;
		this->next = NULL;
	}
	Node(int element, Node* next) {
		this->element = element;
		this->next = next;
	}
};

class LinkedDigraph {
public:
	int n;//�������
	Node** graph;
public:
	LinkedDigraph(int number) {
		n = number;
		graph = new Node * [n + 1];
		for (int i = 1; i <= n; i++) {
			graph[i] = new Node();
		}
	}
	//��ӱߣ���v1��v2
	void addEdge(int v1, int v2) {
		//��û���Ͻڵ�
		if (graph[v1]->next == NULL) {
			graph[v1]->next = new Node(v2, NULL);
		}
		else {
			Node* temp = graph[v1]->next;
			if (temp->next != NULL) {
				temp = temp->next;
			}
			//graph[v1]->next = new Node(v2, graph[v1]->next);
			temp->next = new Node(v2, NULL);
		}
	}
	void dfs(int start, int reach[]) {
		cout << start << " ";
		reach[start] = 1;
		Node* temp = graph[start]->next;
		while (temp != NULL) {
			if (reach[temp->element] == 0) {
				dfs(temp->element, reach);
			}
			temp = temp->next;
		}
	}
	//DFS
	void DFSForLinkedDigraph(int start) {
		int* reach = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			reach[i] = 0;
		}
		dfs(start, reach);
	}
};

int main() {
	AdjacencyWDigraph* a = new AdjacencyWDigraph(5);
	//a->addEdge(1, 2, 10);
	//a->addEdge(1, 3, 50);
	//a->addEdge(1, 5, 100);
	//a->addEdge(2, 3, 200);
	//a->addEdge(2, 4, 30);
	//a->addEdge(3, 4, 290);
	//a->addEdge(3, 5, 250);
	//a->addEdge(4, 5, 280);
	//���������̫trivial�ˣ���������·����
	a->addEdge(1, 2, 1);
	a->addEdge(2, 3, 1);
	a->addEdge(3, 1, 1);
	a->addEdge(3, 4, 1);
	a->addEdge(3, 5, 1);
	a->DFSForAdjacencyWDigraph(1);//�����߻�·
	cout << endl;
	LinkedDigraph* l = new LinkedDigraph(5);
	l->addEdge(1, 2);
	l->addEdge(2, 3);
	l->addEdge(3, 1);
	l->addEdge(3, 4);
	l->addEdge(3, 5);
	l->DFSForLinkedDigraph(1);


	
}