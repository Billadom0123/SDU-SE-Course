/**
 * 쳲��������ж��壺��һ���͵ڶ������ֶ���1����������ÿ����������ǰ��������֮�͡�
 * ��ע�����ִ�С����������죬���ܿ쳬��2��32�η�
 * @author dahogn
 *
 */
public class ThreeOne {
	public static long fib(int n) {
		if (n <= 1)
			return n;
		else
			return fib(n - 1) + fib(n - 2);
	}
	public static void main(String[] args) {
		System.out.println("����Fibonacci���еĳ���");
		System.out.println("������һ����������Ϊ����Ľ���: ");
		int N = SavitchIn.readLineInt();
		for (int i = 1; i <= N; i++)
			System.out.println(i + ": " + fib(i));
	}

}
