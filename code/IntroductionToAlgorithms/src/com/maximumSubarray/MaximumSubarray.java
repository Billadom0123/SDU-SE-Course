package com.maximumSubarray;

import com.DP.allocatedSumForSubSet.JudgeWhetherCouldFormTheAllocatedSum;

public class MaximumSubarray {
    public static int[] linearTimeFind(int[] arr){
        //maxStart\End记录最大子数组的开始结束索引
        int maxStart = 0;
        int maxEnd = 0;
        //curStart\End记录当前子数组的开始结束索引
        int curStart =0;
        int curEnd = 0;
        //CurSum记录当前最大子数组和，maxSum记录最大子数组和,sum记录当前子数组和
        int CurSum = 0;
        int maxSum = 0;
        int sum =0;
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i];
            //如果sum入不敷出，那么前方所有的位置都应该被舍弃，因为这个连续的串可以看作一个负数
            if(sum<=0){
                //前方弃用，更新最大子数组和以及其开始结束索引
                if(maxSum<CurSum){
                    maxSum = CurSum;
                    maxStart = curStart;
                    maxEnd = curEnd;
                }
                //重置sum与CurSum，准备重新记录一个新的最大子数组
                sum=0;CurSum=0;
                //重置curStart与curEnd
                if(i+1<arr.length){
                    curStart=i+1;
                    curEnd=i+1;
                }
            }
            //和增加了，就把当前尾指针移动到当前索引上
            if(CurSum+arr[i]>CurSum&&sum>CurSum){
                curEnd = i;
                //sum记录了连续的和
                CurSum = sum;
            }
            if(i==arr.length-1){
                if(CurSum>maxSum){
                    maxStart = curStart;
                    maxEnd = curEnd;
                }
            }
        }
        return new int[]{maxStart,maxEnd};
    }

    public static void main(String[] args) {
        //test
        int[] arr = {
                10,2,3,7,-38,11,-10,7,44,3,4,-5
        };
//        //another test
//        int[] arr ={
//          13,-3,-25,20,-3,-16,-23,18,20,-7,12,-5,-22,15,-4,7
//        };
        int[] indexes = linearTimeFind(arr);
        System.out.println("最大子数组开始索引: "+indexes[0]);
        System.out.println("最大子数组结束索引: "+indexes[1]);
        int max=0;
        for(int i=indexes[0];i<=indexes[1];i++){
            max+=arr[i];
        }
        System.out.println("最大子数组和: "+max);
    }
}
