/**
 * 斐波纳契数列定义：第一个和第二个数字都是1，而后续的每个数字是其前两个数字之和。
 * 请注意数字大小的增长极其快，将很快超过2的32次方
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
		System.out.println("计算Fibonacci数列的程序。");
		System.out.println("请输入一个整数，作为计算的阶数: ");
		int N = SavitchIn.readLineInt();
		for (int i = 1; i <= N; i++)
			System.out.println(i + ": " + fib(i));
	}

}
