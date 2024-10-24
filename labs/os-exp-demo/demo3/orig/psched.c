/*
* Filename : psched.c
* copyright : (C) 2006 by zhanghonglie
* Function : 父进程创建3 个子进程为它们设置不同的优先数和调度策略
*/
#include <stdio.h>
#include <stdlib.h>
#include <sched.h>
#include <sys/time.h>
#include <sys/resource.h>

int main(int argc, char *argv[])
{
    int i, j, status;
    int pid[3]; //存放进程号
    struct sched_param p[3]; //设置调度策略时使用的数据结构

    for(i = 0; i < 3; i++) {
        //循环创建3 个子进程
        if((pid[i] = fork()) > 0) {
            //取进程优先数放在调度策略数据结构中
            p[i].sched_priority = (argv[i + 1] != NULL) ? atoi(argv[i + 1]) : 10;

            //父进程设置子进程的调度策略.如果命令行第4,5,6 参数指定了3个策略值则按指定的数设置,否则都为默认策略
            sched_setscheduler(pid[i], (argv[i + 4] != NULL) ? atoi(argv[i + 4]) : SCHED_OTHER, &p[i]);
            //父进程设置子进程的优先数,如果命令行第1,2,3 参数指定了3个优先数则按指定的数设置,否则都为10
            setpriority(PRIO_PROCESS, pid[i], (argv[i + 1] != NULL) ? atoi(argv[i + 1]) : 10);
        }

        //各子进程循环报告其优先数和调度策略
        else {
            sleep(1);
            //每隔1秒报告一次进程号和优先级
            for(i = 0; i < 10; i++) {
                printf("Child PID = %d priority = %d\n", getpid(), getpriority(PRIO_PROCESS, 0));
                sleep(1);
            }
            exit(EXIT_SUCCESS);
        }
    }

    //父进程报告子进程调度策略后先行退出
    printf("My child %d policy is %d\n", pid[0], sched_getscheduler(pid[0]));
    printf("My child %d policy is %d\n", pid[1], sched_getscheduler(pid[1]));
    printf("My child %d policy is %d\n", pid[2], sched_getscheduler(pid[2]));
    return EXIT_SUCCESS;
}
