#include<stack>
#include<string>
#include<iostream>
using namespace std;


//������ʽ��ֱ���������룬Ȼ��涨���ŷ�Χ
int	calculate(string str,int start,int end) {
	//Ϊ���ʽ����������ջ�Ͳ�����ջ
	stack<int> number;
	stack<char> operation;
	//����������ַ���
	for (int i = start; i <= end; i++) {
		char c = str.at(i);
		if (c >= 48) {
			//����ǲ�������ֱ����ջ
			number.push(c - 48);
		}
		//������,ÿһ����������Ҫ�жϺ����ķ����ǲ���������
		else {
			//û�п��ǵ�������������
			if (((i+1<=end)&&str.at(i + 1) == '(')) {
				//����������� �ҵ�����ƥ��������ţ��ݹ����������ŵ�ֵ Ȼ���i������������ȥ
				//���⣬�������Ƕ�׽ṹ�����ţ�����Ӧ��Ϊ�ϴ�����Ŵ����������Ͳ�����ջ�������޷�����������ʽ�ӵ�ֵ
				int j = i + 1;
				stack<char> bracket;
				for (; j <= end; j++) {
					//����ƥ���㷨 Ϊ������ֱ����ջ
					if (str.at(j) == '(') {
						bracket.push(str.at(j));
					}
					//����һ���Ϸ���������֤ջ�Ƿ�Ϊ��
					if (str.at(j) == ')') {
						bracket.pop();
						if (bracket.empty()) {
							//���ջ�Ѿ����ˣ���ô˵���ҵ���ƥ��������ţ�������Ϊj
							break;
						}
					}
				}
				int newNumber = calculate(str, i + 1, j);
				//�����ͷǶ����������
				if (str.at(i) == '(') { number.push(newNumber); }
				//����Ǽӻ��߼�
				if (str.at(i) == 43 || str.at(i) == 45) {
					//���ż���ֱ�ӰѼ��������µõ������ƽ�ȥ����
					number.push(newNumber);
					//��Ҫ�ѷ����ƽ�ȥ
					operation.push(str.at(i));
				}
				//����ǳ˻��
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//�����Ƴ�һ����������������
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
				//��һ�����������ţ���˿��Բ��õ��Ļ����else���õ����ŵ����
				i = j;
			}
			//û����һ����������
			else if (str.at(0)=='('&&i==0) {
				//����������� �ҵ�����ƥ��������ţ��ݹ����������ŵ�ֵ Ȼ���i������������ȥ
				//���⣬�������Ƕ�׽ṹ�����ţ�����Ӧ��Ϊ�ϴ�����Ŵ����������Ͳ�����ջ�������޷�����������ʽ�ӵ�ֵ
				int j = i;
				stack<char> bracket;
				for (; j <= end; j++) {
					//����ƥ���㷨 Ϊ������ֱ����ջ
					if (str.at(j) == '(') {
						bracket.push(str.at(j));
					}
					//����һ���Ϸ���������֤ջ�Ƿ�Ϊ��
					if (str.at(j) == ')') {
						bracket.pop();
						if (bracket.empty()) {
							//���ջ�Ѿ����ˣ���ô˵���ҵ���ƥ��������ţ�������Ϊj
							break;
						}
					}
				}
				int newNumber = calculate(str, i+1, j);
				//�����������ţ�˵���������ܵĳ˻��ʱû��ǰ���Ԫ���ˣ�Ҫ�ȴ��ȥ��
				number.push(newNumber);
				//����Ǽӻ��߼�
				if (str.at(i) == 43 || str.at(i) == 45) {
					//���ż���ֱ�ӰѼ��������µõ������ƽ�ȥ����
					//��Ҫ�ѷ����ƽ�ȥ
					operation.push(str.at(i));
				}
				//����ǳ˻��
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//�����Ƴ�һ����������������
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
				//��һ�����������ţ���˿��Բ��õ��Ļ����else���õ����ŵ����
				i = j;
			}
			else {
				//�������һ�����������ţ�˵����һ��������
				if (str.at(i) == 43 || str.at(i) == 45) {
					//�����ӣ��Ӽ�����������ջ�ȳ˳�����
					operation.push(str.at(i));
				}
				//�˳��Ļ����������Ҳ���ţ�Ҳ����ĳ˳������õ������ȼ�û���Ÿ�
				else if(str.at(i)=='*'||str.at(i)=='/') {
					//�õ�����Ĳ�����
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
					//�˳�����õ�������ջ
					number.push(newNumber);
					//��Ϊ�Ѿ���������һ�����֣�Ӧ�ý�ָ�����
					i = i + 1;
				}
			}
		}
	}

	//�����ҵ�����ʧ��������Ҫ������ջ�����ݵ���������Ϊ�����Ǵ�ǰ�������
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

	//����ջ��ʣ�µ��������ݵĽ������ʱ����������+,-
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
	//��ȡ���
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

