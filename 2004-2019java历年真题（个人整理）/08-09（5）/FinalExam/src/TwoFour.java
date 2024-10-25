public class TwoFour {
	public static void main(String args[]) {
		int a[][] = new int[5][5];
		int i, j, k = 10;
		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				if ((i + j) < 5) {
					a[i][j] = k;
					k++;
				} else {
					a[i][j] = 10;
				}
			}
		}
		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
	}
}
