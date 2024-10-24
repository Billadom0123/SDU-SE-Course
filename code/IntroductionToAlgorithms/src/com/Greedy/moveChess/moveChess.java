package com.Greedy.moveChess;

import java.util.Arrays;

/**
 * 贪心相对于DP在这道题上的优势是更加直观
 * 但是缺点是需要证明正确性
 * 简单来说，策略就是每次挑选两个相邻的且和最小的元素合并，累计cost
 */
public class moveChess {
    private final int[] weights;//零索引弃用
    private int n;//dynamic length
    private String[][] solutionTable;//和合并过程一起迭代

    public moveChess(int[] weights) {
        this.weights = weights;
        this.n = weights.length-1;
        initSolutionTable();
    }

    private void initSolutionTable(){
        this.solutionTable = new String[weights.length][weights.length];
        //为了防空指针我还是全部初始化成空串了，其实不需要的
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                solutionTable[i][j]="";
            }
        }
        for(int j=1;j<=n;j++){
            solutionTable[1][j] = "a"+j;
        }
    }

    public int calculateMinimalCost(){
        int totalCost=0;
        int row =1;//记录解表行数
        while(n>1){ //合并到最后一个值
            totalCost+=mergeAdjacentMinimalSum(row);
            row+=1;
        }
        return totalCost;
    }

    /**
     * 合并相邻和最小的两个元素
     * @return 本次合并的开销
     */
    private int mergeAdjacentMinimalSum(int row){
        if(n<=1){
            return weights[1];
        }
        int min = weights[1]+weights[2];
        int formerIndex = 1;
        //找到当前合并的两个元素的前面的索引，并更新最小值
        for(int i=1;i<=n-1;i++){
            int sum = weights[i] + weights[i + 1];
            if(sum <min){
                formerIndex=i;
                min = sum;
            }
        }
        weights[formerIndex]=weights[formerIndex]+weights[formerIndex+1];//merge
        for(int i=formerIndex+2;i<=n;i++){
            weights[i-1]=weights[i];//update
        }
        //下一行索引
        int nextRow = row+1;
        //把formerIndex之前的全部顺延挪到下一行
        for(int i=row;i<formerIndex+row-1;i++){
            solutionTable[nextRow][i+1] = solutionTable[row][i];
        }
        //formerIndex和formerIndex+1合并到下一行的formerIndex+1处
        solutionTable[nextRow][formerIndex+1+row-1] = "("+solutionTable[row][formerIndex+row-1]+solutionTable[row][formerIndex+1+row-1]+")";
        //formerIndex+1之后的直接挪到下一行
        for(int i = formerIndex+1+1+row-1;i<= weights.length-1;i++){
            solutionTable[nextRow][i] = solutionTable[row][i];
        }
        n-=1;
        return  weights[formerIndex];
    }

    public String getSolution(){
        return solutionTable[weights.length-1][weights.length-1];
    }

    public static void main(String[] args) {
        //test for the segment of DP code
        //answer supposed to be 30,71(by DP)
        int[] test = {-1,5,2,7,1};
        com.Greedy.moveChess.moveChess moveChess = new com.Greedy.moveChess.moveChess(test);
        int i = moveChess.calculateMinimalCost();
        System.out.println("The first test result: "+i);
        System.out.println("The first solution result: "+moveChess.getSolution());
        int[] test2 = {-1,2,3,2,5,7,1,6};
        com.Greedy.moveChess.moveChess moveChess1 = new com.Greedy.moveChess.moveChess(test2);
        int i1 = moveChess1.calculateMinimalCost();
        System.out.println("The second test result: "+i1);
        System.out.println("The second solution result: "+moveChess1.getSolution());
    }
}
