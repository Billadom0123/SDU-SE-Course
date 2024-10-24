#include<iostream>
#include<vector>
using namespace std;

//存储每一次的子集，需要回溯
template<class T>
static vector<T> v;
static int n;

template <class T>
//list是全集 total是当前可供选择的元素总数 select是当前还需要选择的个数 start是当前选择样本的开始索引
void subset(T list[], int total, int select, int start) {
	//安全性校验 全集个数一定要大于等于当前可供选择元素的个数
		//递归基，如果当前需要选择的个数已经是0了，说明当前子集构建完毕，输出
		if (select == 0) {
			cout << "{"; cout << v<T>[0];
			for (int i = 1; i < v<T>.size(); i++) {
				cout << ","; cout << v<T>[i];
			}
			cout << "}";
			cout << endl;
		}
		else {
			//遍历每一种头部
			for (int i = start; i < n ; i++) {
				//获取当前向量长度 在得到下一个子集后回溯
				int index = v<T>.size();
				//将选中元素置入向量中
				v<T>.push_back(list[i]);
				//下一次选择的元素需要从上一个元素的后面一个开始 防止重复
				int temptotal=total-1; int tempselect=select-1;int tempi= i+1;
				subset(list, temptotal,tempselect,tempi);
				//回溯 让向量回到未添加该元素的状态 次数为 当前向量长度减去先前向量长度
				int current = v<T>.size();
				for (int k = 1; k <= current - index; k++) {
					//cout << v.back() << " ";
					v<T>.pop_back();
				}

			}
		}
	

}

template<class T>
void getSubset(T list[], int n) {
	//空集不需要递归 特判即可 因为空集是任何集合的子集
	cout << "{}" << endl;
	for (int i = 1; i <= n; i++) {
		subset(list, n, i, 0);
	}
}

int main() {
	//输入 ，元素个数
	cout << "当前集合的类型为:char类型 若要更改，请至main函数更改类型" << endl;
	cout << "请输入全集的势" << endl;
	cin >> n;
	char* arr = new char[n];
	cout << "请输入全集的各个元素" << endl;
	char m;
	//初始化数组
	for (int i = 0; i < n; i++) {
		cin >> m;
		arr[i]=m;
	}
	getSubset(arr, n);
	return 0;
}


