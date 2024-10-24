#include<queue>
#include<iostream>
#include<string.h>
#include<vector>
using namespace std;

static int* p;
static int number;
static vector<int>* result;

class graph {
public:
	//顶点个数
	int n;
	//边的个数
	int e;
	int** a;
	int noEdge = -2;
public:
	graph(int vertex) {
		n = vertex;
		e = 0;
		//初始化二维矩阵
		a = new int* [vertex + 1];
		for (int i = 1; i <= n; i++) {
			a[i] = new int[vertex + 1];
		}
		//一开始一条边都没
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				a[i][j] = noEdge;
			}
		}
	}
	
	void insertEdge(int v1, int v2,int weight) {
		if (a[v1][v2] == noEdge) {
			//新的边
			e++;
		}
		a[v1][v2] = weight;
		a[v2][v1] = weight;
	}

	void bfs(int v) {
		result = new vector<int>;
		p = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			p[i] = 0;
		}
		queue<int> q;
		p[v] = -1;
		q.push(v);
		int count = 1;
		while (!q.empty()) {
			int w = q.front();
			result->push_back(w);
			q.pop();

			for (int u = 1; u <= n; u++) {
				if (a[w][u] != noEdge&&p[u]!=-1) {
					q.push(u);
					p[u] = -1;
				}
			}
		}
	}

	void dfs(int v) {
		p[v] = -1;
		result->push_back(v);
		for (int u = 1; u <= n; u++) {
			if (a[v][u] != noEdge && p[u] != -1) {
				dfs(u);
			}
		}

	}

	void preDfs(int v) {
		result = new vector<int>;
		p = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			p[i] = 0;
		}
		number = 1;
		dfs(v);
	}

	//寻找最短路长
	int findShortestPath(int start, int end) {
		

			int min = 32767;

			for (int i = start; i <= n; i++) {
				if (a[start][i] != noEdge) {
					
					//递归找路长
					int length;
					//找到了
					if (i == end) {
						length = a[start][i];
					}
					else {
						int previous = findShortestPath(i, n);
						if (previous == 0) {
							length = 0;
						}
						else {
							length = a[start][i] + findShortestPath(i, end);
						}
					}
					if (length < min) {
						min = length;
					}
				}
			}

			if (min == 32767) {
				min = 0;
			}

			return min;
		}
	
	int preFind(int start, int end) {
		int* p = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			p[i] = 0;
		}
		return findShortestPath(start, end);
	}

};

int main() {
	cout << "Input" << endl;
	int n, e;
	char comma;
	cin >> n;
	cin >> comma;
	cin >> e;
	graph* g = new graph(n);
	for (int i = 1; i <= e; i++) {
		int v1, v2, weight;
		cin >> v1 >> comma >> v2 >> comma >> weight;
		g->insertEdge(v1, v2, weight);
	}
	cout << "Output" << endl;
	g->bfs(1);
	vector<int> temp = *result;
	for (int i = 0; i < result->size(); i++) {
		
		if (i != result->size() - 1) {
			
			cout << temp[i] << ",";
		}
		else {
			cout << temp[i] << endl;
		}
	}
	g->preDfs(1);
	temp = *result;
	for (int i = 0; i < result->size(); i++) {

		if (i != result->size() - 1) {

			cout << temp[i] << ",";
		}
		else {
			cout << temp[i] << endl;
		}
	}
	cout << g->preFind(1,n) << endl;
	cout << "End";
	return 0;
}