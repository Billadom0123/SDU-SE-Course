#include<stack>
#include<string>
#include<iostream>
using namespace std;


//传入表达式，直接整个传入，然后规定括号范围
int	calculate(string str,int start,int end) {
	//为表达式创建操作数栈和操作符栈
	stack<int> number;
	stack<char> operation;
	//遍历传入的字符串
	for (int i = start; i <= end; i++) {
		char c = str.at(i);
		if (c >= 48) {
			//如果是操作数，直接入栈
			number.push(c - 48);
		}
		//操作符,每一个操作符都要判断后续的符号是不是左括号
		else {
			//没有考虑到上来就是括号
			if (((i+1<=end)&&str.at(i + 1) == '(')) {
				//如果是左括号 找到和它匹配的右括号，递归计算这个括号的值 然后把i跳到右括号上去
				//此外，如果是有嵌套结构的括号，我们应该为较大的括号创建操作数和操作符栈，否则无法计算括号内式子的值
				int j = i + 1;
				stack<char> bracket;
				for (; j <= end; j++) {
					//括号匹配算法 为左括号直接入栈
					if (str.at(j) == '(') {
						bracket.push(str.at(j));
					}
					//由于一定合法，不用验证栈是否为空
					if (str.at(j) == ')') {
						bracket.pop();
						if (bracket.empty()) {
							//如果栈已经空了，那么说明找到了匹配的右括号，索引即为j
							break;
						}
					}
				}
				int newNumber = calculate(str, i + 1, j);
				//解决开头嵌套括号问题
				if (str.at(i) == '(') { number.push(newNumber); }
				//如果是加或者减
				if (str.at(i) == 43 || str.at(i) == 45) {
					//不着急，直接把计算括号新得到的数推进去就行
					number.push(newNumber);
					//还要把符号推进去
					operation.push(str.at(i));
				}
				//如果是乘或除
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//立即推出一个操作数进行运算
					int number1 = number.top();
					number.pop();
					int newNumber2;
					if (str.at(i) == 42) {
						newNumber2 = number1 * newNumber;
					}
					else {
						newNumber2 = number1 / newNumber;
					}
					number.push(newNumber2);
				}
				//这一步是跳过括号，因此可以不用担心会出现else里拿到括号的情况
				i = j;
			}
			//没考虑一上来就括号
			else if (str.at(0)=='('&&i==0) {
				//如果是左括号 找到和它匹配的右括号，递归计算这个括号的值 然后把i跳到右括号上去
				//此外，如果是有嵌套结构的括号，我们应该为较大的括号创建操作数和操作符栈，否则无法计算括号内式子的值
				int j = i;
				stack<char> bracket;
				for (; j <= end; j++) {
					//括号匹配算法 为左括号直接入栈
					if (str.at(j) == '(') {
						bracket.push(str.at(j));
					}
					//由于一定合法，不用验证栈是否为空
					if (str.at(j) == ')') {
						bracket.pop();
						if (bracket.empty()) {
							//如果栈已经空了，那么说明找到了匹配的右括号，索引即为j
							break;
						}
					}
				}
				int newNumber = calculate(str, i+1, j);
				//上来就是括号，说明后续可能的乘或除时没有前面的元素了，要先存进去。
				number.push(newNumber);
				//如果是加或者减
				if (str.at(i) == 43 || str.at(i) == 45) {
					//不着急，直接把计算括号新得到的数推进去就行
					//还要把符号推进去
					operation.push(str.at(i));
				}
				//如果是乘或除
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//立即推出一个操作数进行运算
					int number1 = number.top();
					number.pop();
					int newNumber2;
					if (str.at(i) == 42) {
						newNumber2 = number1 * newNumber;
					}
					else if(str.at(i)=='/') {
						newNumber2 = number1 / newNumber;
					}
					number.push(newNumber2);
				}
				//这一步是跳过括号，因此可以不用担心会出现else里拿到括号的情况
				i = j;
			}
			else {
				//如果后面一个不是左括号，说明是一个操作数
				if (str.at(i) == 43 || str.at(i) == 45) {
					//老样子，加减不急，先入栈等乘除括号
					operation.push(str.at(i));
				}
				//乘除的话，即便后面也括号，也先算的乘除，不用担心优先级没括号高
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//拿到后面的操作数
					int number2 = str.at(i + 1) - 48;
					int newNumber;
					int number1 = number.top();
					number.pop();
					if (str.at(i) == '*') {
						newNumber = number1 * number2;
					}
					else if(str.at(i)=='/') {
						newNumber = number1 / number2;
					}
					//乘除计算得到的数入栈
					number.push(newNumber);
					//因为已经计算了下一个数字，应该将指针后移
					i = i + 1;
				}
			}
		}
	}

	//由于我的严重失误，我们需要把两个栈的数据倒过来，因为计算是从前往后算的
	stack<int> reverseNumber;
	stack<char> reverseOperation;
	while (!number.empty()) {
		reverseNumber.push(number.top());
		number.pop();
	}
	while (!operation.empty()) {
		reverseOperation.push(operation.top());
		operation.pop();
	}

	//计算栈中剩下的所有内容的结果，此时操作数仅有+,-
	while (!reverseOperation.empty()) {
		int number1 = reverseNumber.top();
		reverseNumber.pop();
		int number2 = reverseNumber.top();
		reverseNumber.pop();
		char op = reverseOperation.top();
		reverseOperation.pop();
		if (op == 43) {
			reverseNumber.push(number1 + number2);
		}
		else {
			reverseNumber.push(number1-number2);
		}
	}
	//获取结果
	int result = reverseNumber.top();
	reverseNumber.pop();
	return result;
			
}

int main() {
	cout << "Input" << endl;
	string str;
	cin >> str;
	int result = calculate(str, 0, str.length() - 1);
	cout << "Output" << endl;
	cout << result << endl;
	cout << "End";
	return 0;
}

