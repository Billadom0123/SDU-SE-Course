/**
设有两个并发执行的父子进程，不断循环输出各自进程号、优先数和调度策略。
进程初始调度策略均为系统默认策略和默认优先级。当某个进程收到 SIGINT 信号时
会自动将其优先数加 1，收到 SIGCSTP 信号时会自动将其优先数减 1。
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <signal.h>
#include <sched.h>
#include <sys/time.h>
#include <sys/resource.h>
//初始默认优先级,10
static int default_pri = 10;

//自增1
void increase(int sig){
    default_pri++;
}

//自减1
void decrease(int sig){
    default_pri--;
}

int main(){
    int pid;
    //父子进程设置调度策略数据结构
    struct sched_param p[2];
    int pol[2],pri[2];
    //绑定键盘信号
    signal(SIGINT,increase);
    signal(SIGCSTP,decrease)

    //父子进程都是默认的分时调度策略
    for(int i=0;i<2;i++){
        pol[i]=0;
    }

    //父子进程默认系统优先数
    for(int i=0;i<2;i++){
        pri[i] = default_pri;
    }

    //父进程
    if( (pid = fork() )>0 ){
        while(1){
            //根据键盘信号产生的新的优先数设置进程优先数
            setpriority(PRIO_PROCESS,getpid(),default_pri);
            printf(
                "father pid = %d,father priority = %d,father scheduler = %d\n",
            getpid(),
            getpriority(PRIO_PROCESS,getpid()),
            sched_getscheduler(getpid())
            )
            sleep(3);//不要一直刷屏，过一段时间再显示
        }
    }
    //子进程
    else{
        while(1){
            setpriority(PRIO_PROCESS,pid,default_pri);
            printf(
                "child pid = %d,child priority = %d,child scheduler = %d\n",
            pid,
            getpriority(PRIO_PROCESS,pid),
            sched_getscheduler(pid)
            )
            sleep(3);
        }
    }
}
