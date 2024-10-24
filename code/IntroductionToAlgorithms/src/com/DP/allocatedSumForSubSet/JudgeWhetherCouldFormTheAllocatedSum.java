package com.DP.allocatedSumForSubSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 复杂度上:
 * 对于迭代而言:
 *      初始化: θ(n*sum)
 *      迭代: O(sum*n)
 *      因此总体为: θ(sum*n)
 * 对于回溯解而言:
 *      初始化: θ(n)
 *      回溯: θ(n)
 *      总体为: θ(n)
 */
public class JudgeWhetherCouldFormTheAllocatedSum {
    //我不想采取类字段的形式将该类封装为一个单纯的工具类，需要为每一个实例创建对象加以判断
    private final int[] A;//给定的正整数集合A
    private final int sum;//给定的和S
    private  boolean[][] DPTable;//用来迭代更新的DP2维数组
    private boolean[] solution;

    public JudgeWhetherCouldFormTheAllocatedSum(int[] A,int sum){
        //先按照升序排列一下
        Arrays.sort(A);
        this.A=A;
        this.sum=sum;
        initDPTable();
        initSolution();
    }

    private void initDPTable(){
        this.DPTable = new boolean[sum+1][A.length];
        for (boolean[] rows : DPTable) {
            for (boolean grid : rows) {
                grid = false;
            }
        }
        for (boolean firstRow : DPTable[0]) {
            firstRow = true;
        }
    }

    private void initSolution(){
        this.solution = new boolean[A.length];
        for (boolean b : solution) {
            b=false;
        }
    }

    /**
     * 状态转移方程:
     *      T(P,i) = T(P,i-1) || (ai==P) || T(P-ai,i-1) (注意ai范围防止数组越界)
     *      ∑是累or的意思
     * @return 是否能得到给定和
     */
    public boolean Judge(){
        //应不断迭代列,i为列,P为行
        for(int i=1;i<=A.length-1;i++){ //A.length为(n+1),规定A零索引弃用

            for(int P=1;P<=sum;P++){
                //第一个是前一列能不能形成P，第二个是第i个元素能否形成P，第三个是由当前元素和前一列能够形成的所有数中的某一个组合能否形成P
                DPTable[P][i] = DPTable[P][i-1] || (A[i]==P) || JudgeForeColumnCombineWithCurrentNumberForAllocatedSum(P,i);
                if(P==sum&& DPTable[P][i]){
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * T(P,i)的形成可能是由当前元素和前一列能够形成的所有数中的某一个组合形成的
     * @param P 给定的和(local)
     * @param i 给定的元素索引
     * @return 能否组成给定和P
     */
    private boolean JudgeForeColumnCombineWithCurrentNumberForAllocatedSum(int P,int i){
        if(A[i]>P){
            return false;
        }
        else if(A[i]==P){
            return true;
        }
        else{
            return DPTable[P-A[i]][i-1];
        }
    }

    public List<Integer> getSolution(){
        generateSolution();
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=1;i<=A.length-1;i++){
            if (solution[i]){
                result.add(A[i]);
            }
        }
        return result;
    }

    private void generateSolution(){
        //没解直接返回
        if(!Judge()){
            return;
        }
        int tempSum = sum;
        //检查DPTable的相邻两列
        for(int i=A.length-1;i>=2;i--){
            //找到第一个达成sum的列
            if(!DPTable[tempSum][i]){
                continue;
            }
            //还需要tempSum,结果ai就是tempSum，直接返回了
            if(A[i]==tempSum){
                solution[i]=true;
                return;
            }
            //tempSum是由ai和前i-1项的自然数系数的线性组合组合成的
            if(DPTable[tempSum-A[i]][i-1]){
                solution[i]=true;
                solution[i-1]=true;
                tempSum-=A[i];
                continue;
            }
            //这一段其实可以不用写，但是为了表意，还是写了，意思就是前i-1个的自然数系数的线性组合已经组成了tempSum
            if(DPTable[tempSum][i-1]){
                solution[i]=false;
            }
        }
    }

    public static void main(String[] args) {
        //A test for the DP code
        //supposed to be {true,true,false}
        int[] A = {-1,1,4,5,7,9};
        int S = 17;
        JudgeWhetherCouldFormTheAllocatedSum judgeWhetherCouldFormTheAllocatedSum = new JudgeWhetherCouldFormTheAllocatedSum(A, 17);
        System.out.println("The first test result: "+ judgeWhetherCouldFormTheAllocatedSum.Judge());
        System.out.println("The first solution result: "+ judgeWhetherCouldFormTheAllocatedSum.getSolution() );
        S= 19;
        JudgeWhetherCouldFormTheAllocatedSum judgeWhetherCouldFormTheAllocatedSum1 = new JudgeWhetherCouldFormTheAllocatedSum(A, S);
        System.out.println("The second test result: " + judgeWhetherCouldFormTheAllocatedSum1.Judge());
        System.out.println("The second solution result: "+ judgeWhetherCouldFormTheAllocatedSum1.getSolution());
        S= 24;
        JudgeWhetherCouldFormTheAllocatedSum judgeWhetherCouldFormTheAllocatedSum2 = new JudgeWhetherCouldFormTheAllocatedSum(A, S);
        System.out.println("The third test result: " + judgeWhetherCouldFormTheAllocatedSum2.Judge());
        System.out.println("The third solution result: "+judgeWhetherCouldFormTheAllocatedSum2.getSolution());
    }
}
