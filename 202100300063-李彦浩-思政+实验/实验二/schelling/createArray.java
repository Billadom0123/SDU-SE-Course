package schelling;

public class createArray {


    private int n;
    static int [][]array;

    public createArray(int n) {
        this.n =n;
        array = new int[n][n];
        for(int i=0;i<n;i++) {
            for(int j =0;j<n;j++) {
                array[i][j] = 0;
            }
        }
    }




    public void checkNeighbor() {
        for(int i=0;i<n;i++) {
            for(int j =0;j<n;j++) {
                if(array[i][j]!=0) {
                    int count = 0;

                    if(i-1 >=0&&j-1>=0&&array[i-1][j-1] ==array[i][j] )count ++;
                    if(i-1>=0&&array[i-1][j]==array[i][j])count++;
                    if(i-1>=0&&j+1<n&&array[i-1][j+1]==array[i][j])count++;
                    if(j-1>=0&&array[i][j-1]==array[i][j])count++;
                    if(j+1<n&&array[i][j+1]==array[i][j])count++;
                    if(i+1<n&&j-1>=0&&array[i+1][j-1]==array[i][j])count++;
                    if(i+1<n&&array[i+1][j]==array[i][j])count++;
                    if(i+1<n&&j+1<n&&array[i+1][j+1]==array[i][j])count++;
                    //	    System.out.println("与("+i+","+j+")"+"相同的邻居数为"+count);

                    if(count <3) {
                        int temp = array[i][j];


                        int x = i;
                        int y = j;
                        while(array[x][y]!=0) {
                            x = (int) (Math.random()*n);
                            y = (int) (Math.random()*n);
                        }

                        array[x][y] = temp;
                        array[i][j] =0;



                    }



                }
            }
        }

    }


}
