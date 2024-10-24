/*
* Filename : vmrp.cc
* copyright : (C) 2006 by zhonghonglie
* Function : 模拟虚拟内存页置换算法的程序
*/
#include "vmrp.h"

/**
 * 他的代码写的真的很优雅，教科书级别的
*/

Replace::Replace()
{
    int i;

    //设定总得访问页数,并分配相应的引用页号和淘汰页号记录数组空间
    cout << "Please input page numbers :";
    cin >> PageNumber;
    ReferencePage = new int[sizeof(int) * PageNumber];
    EliminatePage = new int[sizeof(int) * PageNumber];

    //输入引用页号序列(页面走向),初始化引用页数组
    cout << "Please input reference page string :";
    for (i = 0; i < PageNumber; i++)
        cin >> ReferencePage[i]; //引用页暂存引用数组

    //设定内存实页数(帧数),并分配相应的实页号记录数组空间(页号栈)
    cout << "Please input page frames :";
    cin >> FrameNumber;
    PageFrames = new int[sizeof(int) * FrameNumber];
}

Replace::~Replace(){
}

void Replace::InitSpace(char * MethodName)
{
    int i;

    cout << endl << MethodName << endl;
    FaultNumber = 0;
    //引用还未开始,-1表示无引用页
    for (i = 0; i < PageNumber; i++)
        EliminatePage[i] = -1;
    for(i = 0; i < FrameNumber; i++)
        PageFrames[i] = -1;
}

//分析统计选择的算法对于当前输入的页面走向的性能
void Replace::Report(void)
{
    //报告淘汰页顺序
    cout << endl << "Eliminate page:";
    for(int i=0; EliminatePage[i]!=-1; i++)
        cout << EliminatePage[i] << " ";

    //报告缺页数和缺页率
    cout << endl << "Number of page faults = " << FaultNumber << endl;
    cout << setw(6) << setprecision(3) ;
    cout << "Rate of page faults = " << 100*(float)FaultNumber/(float)PageNumber <<
    "%" <<endl;
}

//最近最旧未用置换算法
void Replace::Lru(void)
{
    int i,j,k,l,next;

    InitSpace("LRU");
    //循环装入引用页
    for(k = 0, l = 0; k < PageNumber; k++) {
        next = ReferencePage[k];
        //检测引用页当前是否已在实存
        for(i = 0; i < FrameNumber; i++) {
            if(next == PageFrames[i]) {
                //引用页已在实存将其调整到页记录栈顶
                next = PageFrames[i];
                for(j = i; j > 0; j--)
                    PageFrames[j] = PageFrames[j-1];
                PageFrames[0]=next;
                break;
            }
        }

        if(PageFrames[0] == next) {
            //如果引用页已放栈顶，则为不缺页，报告当前内存页号
            for(j = 0; j < FrameNumber; j++)
                if(PageFrames[j]>=0)
                    cout << PageFrames[j] << " ";
            cout << endl;
            continue; //继续装入下一页
        }
        else
            // 如果引用页还未放栈顶，则为缺页，缺页数加1
            FaultNumber++;

        //栈底页号记入淘汰页数组中(用栈中元素的顺序标识最近的使用时间)
        EliminatePage[l] = PageFrames[FrameNumber-1];
        //向下压栈
        for(j = FrameNumber - 1; j > 0; j--)
            PageFrames[j] = PageFrames[j - 1];
        PageFrames[0]=next; //引用页放栈顶
        //报告当前实存中页号
        for(j = 0; j < FrameNumber; j++)
            if(PageFrames[j] >= 0)
                cout << PageFrames[j] << " ";

        //报告当前淘汰的页号
        if(EliminatePage[l] >= 0)
            cout << "->" << EliminatePage[l++] << endl;
        else
           cout << endl;
    }

    //分析统计选择的算法对于当前引用的页面走向的性能
    Report();
}

//先进先出置换算法
void Replace::Fifo(void)
{
    int i,j,k,l,next;

    InitSpace("FIFO");
    //循环装入引用页
    for(k = 0,j = l = 0; k < PageNumber; k++) {
        next = ReferencePage[k];
        //如果引用页已在实存中，报告实存页号
        for(i = 0; i < FrameNumber; i++)
            if(next == PageFrames[i])
                break;

        if(i < FrameNumber) {
            for(i = 0; i < FrameNumber; i++)
            //-1的那些不要报告
                if(PageFrames[i] >= 0)
                    cout << PageFrames[i] << " ";
            cout << endl;
            continue; // 继续引用下一页
        }

        //引用页不在实存中，缺页数加1
        FaultNumber++;
        EliminatePage[l] = PageFrames[j]; //最先入页号记入淘汰页数组
        PageFrames[j] = next; //引用页号放最先入页号处
        j = (j + 1) % FrameNumber; //最先入页号循环下移

        //报告当前实存页号和淘汰页号
        for(i = 0; i < FrameNumber; i++)
            if(PageFrames[i] >= 0)
                cout << PageFrames[i] << " ";

        if(EliminatePage[l] >= 0)
            cout << "->" << EliminatePage[l++] << endl;
        else
            cout << endl;
    }

    //分析统计选择的算法对于当前引用的页面走向的性能
    Report();
}

//未实现的其他页置换算法入口
void Replace::Clock(void)
{
    int i,j,k,l,m,eli_index,currentPage,currentNum;
    currentNum=0;
    eli_index=0;
    //初始化reference_bit数组全部为0(一开始都没有二次机会)
    int* reference_bit = new int[sizeof(int)*FrameNumber];
    for(i=0;i<FrameNumber;i++){
        reference_bit[i]=0;
    }

    InitSpace("Clock");
    for(i=0;i<PageNumber;i++){
        currentPage = ReferencePage[i];
        /**
         * 根据这个算法的定义，我需要先在实存中已有的所有页号，如果有匹配的，那么不需要置换，同时把reference_bit置为1
         * 如果没有匹配的，那么需要从头重新查找第一个reference_bit为0的元素，并把沿途所有reference_bit为1的页面的r_bit全部置为0
         * 这个过程持续到找到这个元素为止
         * 然后替换这个页面，并把reference_bit置为1
        */

       //首先查找是否有匹配的页面
        for(j=0;j<FrameNumber;j++){
            if(currentPage == PageFrames[j]){
                break;
            }
        }

        //如果找到了，不需要置换，把reference_bit置为1
        if(j<FrameNumber){
            reference_bit[j]=1;
            //报告实存所有页面的页号和reference_bit
            cout << "reference_bit:  Page_Number:  " << endl;
            for(k=0;k<FrameNumber;k++){
                if(PageFrames[k]>=0){
                    cout << reference_bit[k] << "  " << PageFrames[k]<< endl;
                }
            }
            continue;//继续下一个引用串中的页面
        }
        else{
            //没有找到
            FaultNumber++;
            if(currentNum<FrameNumber){
                reference_bit[currentNum]=1;
                PageFrames[currentNum++]=currentPage;
            }
            else{
                //直到找到一个reference_bit为0的才停下
                bool r_bit_zero = true;
                while(r_bit_zero){
                    for(l=0;l<FrameNumber;l++){
                        //如果实存的这个位置是有页面的，并且它的reference_bit为1,那么逃过一劫，还有机会
                        if(PageFrames[l]>=0&&reference_bit[l]!=0){
                            reference_bit[l]=0;
                        }
                        else if(PageFrames[l]>=0&&reference_bit[l]==0){
                            r_bit_zero=false;
                            break;
                        }
                    }  
                }
                //记录即将淘汰的页面
                EliminatePage[eli_index] = PageFrames[l];
                //替换这个索引的页面，置reference_bit为1
                PageFrames[l] = currentPage;
                reference_bit[l] = 1;
            }


            //报告实存中页面的改动
            cout << "reference_bit:  Page_Number:  " << endl;
            for(m=0;m<FrameNumber;m++){
                if(PageFrames[m]>=0){
                    cout << reference_bit[m] << "  " << PageFrames[m]<< endl;
                }
            }
            if(EliminatePage[eli_index]>=0){
                cout << "->" << EliminatePage[eli_index++] << endl;
            }
            else{
                cout<<endl;
            }
        }
    }
    //分析统计
    Report();
}

void Replace::Eclock (void)
{
    int i,j,k,l,m,eli_index,currentPage,currentNum;
    currentNum=0;
    eli_index=0;
    //初始化reference_bit数组全部为0(一开始都没有二次机会)
    int* reference_bit = new int[sizeof(int)*FrameNumber];
    for(i=0;i<FrameNumber;i++){
        reference_bit[i]=0;
    }
    //初始化modify_bit数组全部为0(一开始都没有被修改过)
    int* modify_bit = new int[sizeof(int)*FrameNumber];
    for(i=0;i<FrameNumber;i++){
        modify_bit[i]=0;
    }

    InitSpace("Eclock");
    for(i=0;i<PageNumber;i++){
        currentPage = ReferencePage[i];
        //这里关于这个modify_bit，由于没有用户的交互，我也不知道啥时候页面数据被修改了
        //所以我只能找一种方式去模拟modify_bit置1的情况，暂时就定为如果不需要置换，那么m_bit和r_bit同时置1

       //首先查找是否有匹配的页面
        for(j=0;j<FrameNumber;j++){
            if(currentPage == PageFrames[j]){
                break;
            }
        }

        //如果找到了，不需要置换，把reference_bit置为1，同时置modify_bit为1
        if(j<FrameNumber){
            reference_bit[j]=1;
            modify_bit[j]=1;
            //报告实存所有页面的页号和reference_bit
            cout << "reference_bit:  modify_bit:  Page_Number:  " << endl;
            for(k=0;k<FrameNumber;k++){
                if(PageFrames[k]>=0){
                    cout << reference_bit[k] << "  "<< modify_bit[k] << "  "<< PageFrames[k]<< endl;
                }
            }
            continue;//继续下一个引用串中的页面
        }
        else{
            //没有找到
            FaultNumber++;
            if(currentNum<FrameNumber){
                reference_bit[currentNum]=1;
                PageFrames[currentNum++]=currentPage;
            }
            else{
                //直到找到一个reference_bit为0的才停下,这里(0,0)被替换的优先级要高于(0,1),需要一些trick来实现
                bool r_bit_zero = true;
                bool round_already = false; //是否走过了一轮，走过一轮了还没确定这个元素就说明全是(0,1)，找第一个就完事了
                while(r_bit_zero){
                    for(l=0;l<FrameNumber;l++){
                        if((l+1)==FaultNumber){
                            //最后一个元素了，走过了一轮
                            round_already=true;
                        }
                        //如果实存的这个位置是有页面的，并且它的reference_bit为1,那么逃过一劫，还有机会
                        if(PageFrames[l]>=0&&reference_bit[l]!=0){
                            reference_bit[l]=0;
                        }
                        else if(PageFrames[l]>=0&&reference_bit[l]==0){
                            //检查修改位，如果为1那么先跳过，为0直接锁定
                        if(modify_bit[l]!=0){
                            if(round_already){
                                //走过一轮，锁定成功，不要再找了
                                r_bit_zero=false;
                                break;
                            }
                            //没锁定别改r_bit_zero
                            continue;
                        }
                        else{
                            r_bit_zero=false;
                            break;
                        }
                        }
                    }  
                }
                //记录即将淘汰的页面
                EliminatePage[eli_index] = PageFrames[l];
                //替换这个索引的页面，置reference_bit为1
                PageFrames[l] = currentPage;
                reference_bit[l] = 1;
                modify_bit[l]=0;
            }


            //报告实存中页面的改动
            cout << "reference_bit:  modify_bit:  Page_Number:  " << endl;
            for(m=0;m<FrameNumber;m++){
                if(PageFrames[m]>=0){
                    cout << reference_bit[m] << "  "<< modify_bit[m]<< "  " << PageFrames[m]<< endl;
                }
            }
            if(EliminatePage[eli_index]>=0){
                cout << "->" << EliminatePage[eli_index++] << endl;
            }
            else{
                cout<<endl;
            }
        }
    }
    //分析统计
    Report();
}

void Replace::Lfu(void)
{
    int i,j,k,l,m,currentPage,currentNum;
    currentNum=0;
    l=0;
    InitSpace("Lfu");
    //初始化reference_count用来记录一个页面被引用的次数,一开始没有页面被引用，均为0
    int* reference_count = new int[sizeof(int)*FrameNumber];
    for(i=0;i<FrameNumber;i++){
        reference_count[i]=0;
    }

    for(i=0;i<PageNumber;i++){
        currentPage = ReferencePage[i];

        /**
         * 查找当前页是否已经在实存中了，如果在，则其reference_count++
         * 如果不在，暴力搜索所有页，找到其中reference_count最小的，替换，并置新页面的reference_count为0
        */
        for(j=0;j<FrameNumber;j++){
            if(currentPage == PageFrames[j]){
                break;
            }
        }

        if(j<FrameNumber){
            //找到了
            reference_count[j]++;
            //报告实存页面以及其引用次数
            cout << "Reference count:  Page Number:  " << endl;
            for(k=0;k<FrameNumber;k++){
                if(PageFrames[k]>=0){
                    cout << reference_count[k] << "  " << PageFrames[k] << endl;
                }
            }
            continue;
        }
        else{
            //没找到
            FaultNumber++;
            if(currentNum<FrameNumber){
                PageFrames[currentNum++]=currentPage;
            }
            else{
                int minIndex=0;
                int min = reference_count[0];
                for(j=1;j<FrameNumber;j++){
                    if(reference_count[j]<min){
                        minIndex=j;
                        min = reference_count[0];
                    }
                }
                //替换这一页(一开始什么都没有的时候minIndex=0)
                EliminatePage[l] = PageFrames[minIndex];
                reference_count[minIndex] = 0;
                PageFrames[minIndex]=currentPage;
            }


            //报告实存情况
            cout << "Reference count:  Page Number:  " << endl;
            for(m=0;m<FrameNumber;m++){
                if(PageFrames[m]>=0){
                    cout << reference_count[m] << "  " << PageFrames[m] << endl;
                }
            }
            if(EliminatePage[l]>=0){
                cout << "->" << EliminatePage[l++] << endl;
            }
            else{
                cout << endl;
            }
        }
    }
    Report();
}

void Replace::Mfu(void)
{
    int i,j,k,l,m,currentPage,currentNum;
    currentNum=0;
    l=0;
    InitSpace("Mfu");
    //初始化reference_count用来记录一个页面被引用的次数,一开始没有页面被引用，均为0
    int* reference_count = new int[sizeof(int)*FrameNumber];
    for(i=0;i<FrameNumber;i++){
        reference_count[i]=0;
    }

    for(i=0;i<PageNumber;i++){
        currentPage = ReferencePage[i];

        /**
         * 查找当前页是否已经在实存中了，如果在，则其reference_count++
         * 如果不在，暴力搜索所有页，找到其中reference_count最小的，替换，并置新页面的reference_count为0
        */
        for(j=0;j<FrameNumber;j++){
            if(currentPage == PageFrames[j]){
                break;
            }
        }

        if(j<FrameNumber){
            //找到了
            reference_count[j]++;
            //报告实存页面以及其引用次数
            cout << "Reference count:  Page Number:  " << endl;
            for(k=0;k<FrameNumber;k++){
                if(PageFrames[k]>=0){
                    cout << reference_count[k] << "  " << PageFrames[k] << endl;
                }
            }
            continue;
        }
        else{
            //没找到
            FaultNumber++;
            if(currentNum<FrameNumber){
                PageFrames[currentNum++]=currentPage;
            }
            else{
                int maxIndex=0;
                int max = reference_count[0];
                for(j=1;j<FrameNumber;j++){
                    if(reference_count[j]>max){
                        maxIndex=j;
                        max = reference_count[0];
                    }
                }
                //替换这一页(一开始什么都没有的时候minIndex=0)
                EliminatePage[l] = PageFrames[maxIndex];
                reference_count[maxIndex] = 0;
                PageFrames[maxIndex]=currentPage;
            }


            //报告实存情况
            cout << "Reference count:  Page Number:  " << endl;
            for(m=0;m<FrameNumber;m++){
                if(PageFrames[m]>=0){
                    cout << reference_count[m] << "  " << PageFrames[m] << endl;
                }
            }
            if(EliminatePage[l]>=0){
                cout << "->" << EliminatePage[l++] << endl;
            }
            else{
                cout << endl;
            }
        }
        
    }
    Report();
}

int main(int argc, char *argv[])
{
    Replace * vmrp = new Replace();
    // vmrp->Fifo();
    // vmrp->Lru();
    vmrp->Clock();
    vmrp->Eclock();
    vmrp->Lfu();
    vmrp->Mfu();
    return 0;
}
