#include<iostream>
#include<vector>
using namespace std;


static int i = 0;
static vector<int> v;

void printVector(vector<int> v) {
	cout << "{" << v[0];
	for (int j = 1; j < v.size(); j++) {
		cout << "," << v[j];
	}
	cout << "}" << endl;
}

//这是一个很有趣的输出顺序
//首先，输出所有1开头的子集。
//但在1开头的子集中，优先输出所有2开头的子集。
//在2开头的子集中，优先输出所有3开头的子集
//势更大的优先
//也就是说，1，2，3即便作为一个势只有3的子集，由于他是1开头，2开头，3开头的，所以他的优先级高于1，2，4，5这个势为4的子集
//显然，输入有序是用户决定的，也就是说，如果他输入了1，2，4，3，那么根据他制定某一规则，4优先于3，It's none of my business。我只负责按照他的意愿输出
//因此我们认为数组是有序的。
void subset(int arr[], int start) {
	//我们可以这样理解这个输出顺序。假设我们已知输入就是1，2，3，4。第一次选取1开头，然后进入递归，第二次分别选取2，3，4开头。
	//第一个，2。分别选取3，4开头 3可以选取4开头，但是4后面没了，他没有选择开头的权力。
	//第二个，3。选取4开头。同样，4后面没了，没得选
	//第三个，4。直接没得选
	//因此在回溯时，1，2，3，4回溯到1，2，3和1，2，4的并列，然后输出了1，2，3和1，2，4
	//1,2,3和1，2，4这一对平行分支，回溯到1，2时，输出了1，2
	//1，3，4回溯到1，3，输出了1，3
	//1，4没有回溯
	//1，2；1，3；1，4作为1的平行分支，回溯到1时，输出了1。以下同理。
	//所以，这个输出顺序，决定了递归中，只要数组和选择范围即可，其他参数一律不要
	//递归基：如果可供选择的范围已经到了数组尾部，输出
	if (start >= i - 1) {
		if (start == i - 1) {
			v.push_back(arr[start]);
			printVector(v);
		}

	}
	else {
		for (int k = start; k <= i - 1; k++) {
			//记录当前向量长度，以待回溯
			int length = v.size();
			//把当前这个元素放进向量，进行递归
			v.push_back(arr[k]);
			subset(arr, k + 1);
			if (k + 1 == i - 1) {
				v.pop_back();
			}
			//打印当前这个向量
			printVector(v);
			//回溯
			v.pop_back();

		}
	}


}

int main() {

	cout << "Input" << endl;
	int n;
	int origin[20];
	cin >> n;

	while (n != 0) {
		origin[i++] = n;
		cin >> n;
	}
	int* arr = new int[i];
	for (int j = 0; j < i; j++) {
		arr[j] = origin[j];
	}

	cout << "Output" << endl;
	subset(arr, 0);
	//单独输出一个空集
	cout << "{}" << endl;

	cout << "End1";
	return 0;
}
