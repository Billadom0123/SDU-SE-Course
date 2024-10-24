#include "pctl.h"

static volatile int running =1;//没有键盘中断信号就一直跑

void Interruption_handler(int sig){
    running=0;
    printf("Interruption Signal accepted,ready to quit");
}

int main(){
    int pid;//进程标识
    int status;//子进程生命周期标识
    char *args[]={"/bin/ls",NULL};//linux下打印文件名列表的命令行参数
    signal(SIGINT,Interruption_handler);//注册键盘中断信号
    pid=fork();//分支创建父子进程
    printf("pid is %d\n",pid);
        if(pid<0){
            printf("Create Process fail!\n");
            exit(EXIT_FAILURE);
        }
        if(pid==0){
            while(running){
                //子进程代码段
                printf("I am Child process %d\nMy father is %d\n", getpid(), getppid());
                printf("%d child will Running: ", getpid());
                system(args[0]);
                sleep(3);
            }

        }
        else{
            //父进程执行
            printf("\nI am Parent process %d\n", getpid());
            printf("%d Waiting for child done.\n\n", getpid());
            waitpid(pid, &status, 0);  //等待子进程结束
            printf("\nMy child exit! status = %d\n\n", status);
        }

    return EXIT_SUCCESS;
}