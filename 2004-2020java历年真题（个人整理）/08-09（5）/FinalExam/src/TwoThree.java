public class TwoThree {
	public static void main(String[] args) {
	new ExceptionTest().m1();
	}
}
class ExceptionTest{		
	void m1() {
		int[] a = {0, 1 };
		try {
			for (int i=0; i<=a.length; i++) {
				m2(a, i);
				System.out.println();
			}
		}catch (Exception e){
			System.out.println("handle Exception");
		}
		System.out.println(" m1 runs ");
	}
	void m2(int[] a , int i) throws Exception {
		try{
			System.out.println(a[i]/i);
		}catch (ArithmeticException e){
			System.out.println("handle ArithmeticException");
		}
		finally{
			System.out.println("finally");
		}
		System.out.println("m2 ends");
	}	
}


