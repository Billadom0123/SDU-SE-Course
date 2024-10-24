/**
 * 假设一个系统中有三个抽烟者进程，每个抽烟者不断地卷烟并抽烟。
 * 抽烟者卷起并抽掉一颗烟需要有三种材料：烟草、纸和胶水。
 * 一个抽烟者有烟草，一个有纸，另一个有胶水。系统中还有两个供应者进程，它们无限地供应所有三种材料，但每次仅轮流提供三种材料中的两种。
 * 得到缺失的两种材料的抽烟者在卷起并抽掉一颗烟后会发信号通知供应者，让它继续提供另外的两种材料。这一过程重复进行。
*/
#include "ipc.h"

//生产消费者共享缓冲区即其有关的变量
key_t buff_key;
int buff_num;
char *buff_ptr;
//生产者放产品位置的共享指针
key_t pput_key;
int pput_num;
int *pput_ptr;
//消费者取产品位置的共享指针
key_t cget_key;
int cget_num;
int *cget_ptr;
//生产者和消费者有关的信号量
key_t ab_key;
key_t ac_key;
key_t bc_key;
key_t all_key;
key_t produce_key;
int ab_int;
int ac_int;
int bc_int;
int all_int;
int produce_int;

int sem_val;
int sem_flg;
int shm_flg;

int main(int argc,char *argv[])
{
    int rate;
//可在在命令行第一参数指定一个进程睡眠秒数，以调解进程执行速度
    if(argv[1] != NULL)
        rate = atoi(argv[1]);
    else
        rate = 3; //不指定为 1 秒
//共享内存使用的变量
    buff_key = 101;//缓冲区任给的键值
    buff_num = 3;//缓冲区任给的长度
    pput_key = 102;//生产者放产品指针的键值
    pput_num = 1; //指针数
    shm_flg = IPC_CREAT | 0644;//共享内存读写权限

//获取缓冲区使用的共享内存，buff_ptr 指向缓冲区首地址
    buff_ptr = (char *)set_shm(buff_key,buff_num,shm_flg);
//获取生产者放产品位置指针 pput_ptr
    pput_ptr = (int *)set_shm(pput_key,pput_num,shm_flg);
//信号量使用的变量

    ab_key = 201;//C的消费者控制键值
    ac_key = 202;//B的消费者控制键值
    bc_key = 203;//A的消费者控制键值
    all_key = 204;//对一个缓冲区的控制键值
    produce_key = 205;//对两个生产者的同步键值
    sem_flg = IPC_CREAT | 0644;
//生产者同步信号灯初值设为缓冲区最大可用量
    sem_val = buff_num;
//获取生产者同步信号灯，引用标识存 all_int
    all_int = set_sem(all_key,sem_val,sem_flg);
//消费者初始无产品可取，同步信号灯初值设为 0
    sem_val = 0;
    ab_int = set_sem(ab_key,sem_val,sem_flg);
    ac_int = set_sem(ac_key,sem_val,sem_flg);
    bc_int = set_sem(bc_key,sem_val,sem_flg);
//生产者互斥信号灯初值为 1
    sem_val = 1;
//获取生产者互斥信号灯，引用标识存 produce_int
    produce_int = set_sem(produce_key,sem_val,sem_flg);
    int i = 0;
    
    while(1){
        int d = (i++)%3;
        down(all_int);
        down(produce_int);
        buff_ptr[*pput_ptr] = 'A'+ d;
        
	if(d==0)
            printf("%d把烟草和纸放入[%d]缓存区\n",getpid(),*pput_ptr);
        else if(d==1)
            printf("%d把胶水和纸放入[%d]缓存区\n",getpid(),*pput_ptr);
        else
            printf("%d把烟草和胶水放入[%d]缓存区\n",getpid(),*pput_ptr);
	*pput_ptr = (*pput_ptr+1) % buff_num;
        up(produce_int);
        if(d==0)
            up(bc_int);
        else if(d==1)
            up(ac_int);
        else
            up(ab_int);
        sleep(rate);
    }
    return EXIT_SUCCESS;
}

