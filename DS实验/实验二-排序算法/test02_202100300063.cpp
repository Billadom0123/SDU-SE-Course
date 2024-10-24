#include<iostream>
using namespace std;

static int k=0;
//归并排序的辅助数组
static int* assist;


//冒泡排序的思路:每次把当前子列中最大的元素拿到子列最后，那么最后显然是有序的。
//外层循环控制轮次，最坏k-1轮。内层循环保证子列最大元素在最后。
void BubbleSort(int* arr) {
	//及时终止，记录当前是否已经有序
	bool swapped = true; //为了进循环需要先设置为true
	//如果整个数组一开始是逆序的，最坏需要k-1次才能把所有元素排序
	for (int i = 0; i < k - 1&&swapped; i++) { //如果上一次经历了交换，则可能无序，还需要排
		//每次都先预设已经有序
		swapped = false;
		//每一次排序，都从数组头部开始向后遍历，把除了已经排好的元素以外的最大元素找出来
		for (int j = 0; j < k - i-1; j++) {
			if (arr[j] > arr[j + 1]) {
				int temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
				swapped = true;
			}
		}
	}
}

//插入排序的思路:保证在循环遍历时当前子列是有序的，这样
// 在后一轮元素移动时，只要让被移动的元素也按顺序插入，那么后一轮也是有序的，整体就是有序的。
//通过外层循环控制子列长度增长。内层循环保证子列有序。
void InsertSort(int* arr) {
	//可惜的是，插入排序不能像冒泡排序一样及时终止。这是因为哪怕上一轮没移动，下一轮仍然可能移动元素
	//注意这里数组最后的元素也要参与插入过程
	for (int i = 0; i < k; i++) {
		//子列逆序遍历时为了比较和前一个元素的大小，j不能为0，否则j-1<0
		for (int j = i; j > 0; j--) {
			if (arr[j] < arr[j - 1]) {
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
			else {
				//如果上一次比较都没交换，因为原本就是有序的，说明下一次一定不会交换，直接跳出去
				break;
			}
		}
	}
}

void merge(int* arr,int low,int mid,int high) {
	//合并思路:前半段当前元素和后半段当前元素比较，取小的出来放到辅助数组。
	//直到其中一个放完，剩下的因为之前分治时已经有序，按顺序放在后面即可。
	int i = low;
	//两个指针，分别遍历前后半段的元素
	int p1 = low;
	int p2 = mid + 1;
	while (p1 <= mid && p2 <= high) {
		if (arr[p1] < arr[p2]) {
			assist[i++] = arr[p1++];
		}
		else {
			assist[i++] = arr[p2++];
		}
	}
	//剩余元素放入辅助数组
	while (p1 <= mid) {
		assist[i++] = arr[p1++];
	}

	while (p2 <= high) {
		assist[i++] = arr[p2++];
	}

	for (int j = low; j <= high; j++) {
		arr[j] = assist[j];
	}
}

void sort(int* arr, int low, int high) {
	//递归基 同时是安全性校验 当log=high时，不需要再分了
	if (low >= high) {
		return;
	}
	int mid = (low + high) / 2;

	//前半段分治
	sort(arr, low, mid);
	//后半段分治
	int nextLow = mid + 1;
	sort(arr,nextLow, high);

	//前半段后半段合并
	merge(arr, low, mid, high);
}

//归并排序的思路:分治。每次分成两半，随后合并时按顺序归并。
//这样回溯时，两半里各自有序，再归并，还是有序，一直到整个数组。
void MergeSort(int* arr) {
	assist = new int[k];
	for (int i = 0; i < k; i++) {
		assist[i] = 0;
	}
	int low = 0;
	int high = k - 1;
	sort(arr, low, high);
}



int main() {
	//接收最多18个正整数 进行排序
	cout << "Input" << endl;
	int n;
	cin >> n;
	int* origin = new int[18];
	//初始化数组
	for (int i = 0; i < 18; i++) {
		origin[i] = 0;
	}
	//接收元素
	while (n != 0 && k < 18) {
		origin[k++] = n;
		cin >> n;
	}
	//整理数组
	int* arr = new int[k];
	for (int i = 0; i < k; i++) {
		arr[i] = origin[i];
	}
	cout << "1-Bubble Sort,2-Insert Sort,3-Merge Sort" << endl;
	cin >> n;
	cout << "Output" << endl;
	if (n == 1) {
		cout << "Bubble Sort" << endl;
		BubbleSort(arr);
	}
	else if(n==2) {
		cout << "Insert Sort" << endl;
		InsertSort(arr);
	}
	else {
		cout << "Merge Sort" << endl;
		MergeSort(arr);
	}

	for (int i = 0; i < k; i++) {
		if (i == k - 1) {
			cout << arr[i];
		}
		else {
			cout << arr[i] << ",";
		}
	}

	cout << endl;

	cout << "End";

	return 0;
}