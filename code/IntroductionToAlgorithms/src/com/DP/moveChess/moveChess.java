package com.DP.moveChess;

/**
 * 这题完完全全就是矩阵乘法链的改版
 */
public class moveChess {
    private final int[] weights;//零索引弃用
    private int[][] DPTable;
    private int[][] solutionTable;//保存解的二维表
    private final int n;//方阵行数

    public moveChess(int[] weights){
        this.weights=weights;
        this.n = weights.length-1;
        initDPTable();
        initSolutionTable();
    }

    private void initDPTable(){
        //其实DP表是个三角矩阵，可以通过一些映射函数缩小申请的内存空间，但我懒得做了，因为渐进复杂度都是n²
        this.DPTable = new int[weights.length][weights.length];
        //一堆棋子没有合并的必要，对角线元素均为0
        for(int i=1;i<=n;i++){
            DPTable[i][i]=0;
        }
    }

    private void initSolutionTable(){
        this.solutionTable = new int[weights.length][weights.length];
    }

    //其实一堆int相加不一定也是int，但这里主要考虑的内容是算法，溢出无所谓，就当是脏数据

    /**
     * 状态转移方程:
     *      T(i,j) = min(0≤k≤(j-i)/2) { T(i,i+k) + T(i+k+1,j) + ∑(p start from i,j) weight[p] }
     * @return 最小耗费
     */
    public int calculateMinimalCost(){
        //先两个两个，再三个三个，最后n个n个
        //eg. 1-2,2-3,...,n-1-n; 1-3,2-4,......
        //l为本次迭代的序列长度
        for(int l=2;l<=n;l++){

            //i为本次迭代的行号
            for(int i=1;i<=n-l+1;i++){
                int j = i+l-1;//j为对应的列号,(i,j)意即从第i堆棋子到第j堆棋子
                int subSum = sumOfItoJ(i, j);
                //DP方程计算计算最小消费
                int min = DPTable[i][i]+DPTable[i+1][j]+ subSum;
                int minK = i;//记录断点位置
                for(int k=0;k<=l-2;k++){
                    int cur = DPTable[i][i + k] + DPTable[i + k + 1][j] + subSum;
                    if(cur < min){
                        min = cur;
                        minK = i+k;
                    }
                }
                //迭代二维表
                DPTable[i][j]=min;
                //更新解表断点
                solutionTable[i][j] = minK;
            }

        }
        return DPTable[1][n];//返回T(1,n)的值
    }

    private int sumOfItoJ(int i,int j){
        int sum=0;
        for(int index=i;index<=j;index++){
            sum+=weights[index];
        }
        return sum;
    }

    public String getSolution(){
        return traceBackSolution(1, n);
    }

    private String traceBackSolution(int start,int end){
        if(start==end){
            return "a"+start;
        }
        else{
            return "("+
                    traceBackSolution(start,solutionTable[start][end])+
                    traceBackSolution(solutionTable[start][end]+1,end)+
                    ")";
        }
    }

    public static void main(String[] args) {
        //test for the segment of DP code
        //answer supposed to be 30,71(by the greedy algorithm)
        int[] test = {-1,5,2,7,1};
        moveChess moveChess = new moveChess(test);
        int i = moveChess.calculateMinimalCost();
        System.out.println("The first test result: "+i);
        System.out.println("The first test solution: "+moveChess.getSolution());
        int[] test2 = {-1,2,3,2,5,7,1,6};
        moveChess moveChess1 = new moveChess(test2);
        int i1 = moveChess1.calculateMinimalCost();
        System.out.println("The second test result: "+i1);
        System.out.println("The first test solution: "+moveChess1.getSolution());
    }
}