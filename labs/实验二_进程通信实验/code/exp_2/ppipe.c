#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(){
    int argx;
    int argy;
    scanf("%d",&argx);scanf("%d",&argy);
    int pid1;//处理f(x)
    int pid2;//处理f(y)
    int pipe1[2];
    int pipe2[2];
    int volatile x=1;
    int volatile y=1;

    //使用pipe()系统调用建立两个无名管道。建立不成功程序退出，执行终止
    if(pipe(pipe1) < 0) {
        perror("pipe1 not create");
        exit(EXIT_FAILURE);
    }
    if(pipe(pipe2) < 0) {
        perror("pipe2 not create");
        exit(EXIT_FAILURE);
    }

    //创建两个子进程，一个处理f(x),一个处理f(y)
    pid1=fork();
    if(pid1 == 0) {
        //P1从管道1的1端写
        close(pipe1[0]);
        int fx;
        do{
            if(x==1){
                fx = 1;
            }
            else{
                fx = fx*x;
            }
            printf("P1 calculate: %d\n",fx);
            x++;
        }while(x<=argx);
        write(pipe1[1],&fx,sizeof(int));
        
        close(pipe1[1]);

        exit(EXIT_SUCCESS);
    }
    else{
        //如果运行的不是P1，创建一个P2处理f(y)
        pid2=fork();
        if(pid2==0){
            //P2从管道2的1端写
            close(pipe2[0]);
            int fy;
            int ppreFy;
            do{
                if(y==1||y==2){
                    fy=1;
                    ppreFy=1;
                }
                else{
                    int temp = fy;//临时保存fy
                    fy = fy+ppreFy;//更新fy
                    ppreFy=temp;//临时fy顶替ppreFy
                }

                printf("P2 calculate: %d\n",fy);
                y++;
            }while(y<=argy);
            write(pipe2[1],&fy,sizeof(int));
            close(pipe1[1]);
            exit(EXIT_SUCCESS);
        }
        else{
            //P1,P2的父进程P3运行
            //从管道1和2的零端读
            close(pipe1[1]);
            close(pipe2[1]);
            int fxy;
            int tempFx;
            int tempFy;

            read(pipe1[0],&tempFx,sizeof(int));
            read(pipe2[0],&tempFy,sizeof(int));
            printf("result from pipe1[0]: %d\nresult from pipe2[0]: %d\n",
            tempFx,tempFy);
            fxy = tempFx+tempFy;
            printf("P3(parent) get: %d\n",fxy);


            close(pipe1[0]);
            close(pipe2[0]);
            return EXIT_SUCCESS;
        }
    }



}