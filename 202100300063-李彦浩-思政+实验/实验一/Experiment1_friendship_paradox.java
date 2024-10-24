import java.util.*;
public class Experiment1_friendship_paradox {
        public static void main(String[] args) {

            Scanner sc=new Scanner(System.in);
            System.out.println("请输入图的节点个数：");
            int n=sc.nextInt();
            String t=sc.nextLine();
            String[] strs=new String[n];
            int[][] mtr=new int[n][n];
            System.out.println("请输入图的邻接矩阵，数字之间以空格分隔");

            for(int i=0;i<n;i++){
                strs[i]=sc.nextLine();
                String[] ss=strs[i].split(" ");
                int j=0;
                for (String s:ss){
                    mtr[i][j]=Integer.parseInt(s);
                    j++;
                }
            }

            int[] fri=new int[n];

            for (int i = 0; i < n; i++) {
                ArrayList<Integer> friends=new ArrayList();
                for (int j = 0; j < n; j++) {
                    if (mtr[i][j]==1){
                        fri[i]+=1;
                        friends.add(j);
                    }
                }
            }
            int count=0;
            for (int i = 0; i < n; i++) {
                ArrayList<Integer> friends=new ArrayList();
                for (int j = 0; j < n; j++) {
                    if (mtr[i][j]==1){
                        friends.add(j);
                    }
                }
                if(is(i,friends,fri,mtr)){
                    count++;
                }
            }

            double x = (double)count/(double)n ;
            System.out.println(String.format("%.2f", x*100)+"%");
        }

        public static boolean is(int i, ArrayList<Integer> friends, int[] fri, int[][] mtr){

            int number=0;

            for (int k = 0; k < friends.size(); k++) {
                number+=fri[friends.get(k)];
            }
            double avg=(double)number/(double)fri[i];

            if (avg>(double)fri[i]){
                return true;
            }
            return false;

        }

}
