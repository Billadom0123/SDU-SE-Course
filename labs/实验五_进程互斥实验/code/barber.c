#include "ipc.h"
key_t buff_key;
int buff_num;
char *buff_ptr;
key_t pput_key;
int pput_num;
int *pput_ptr;
key_t cget_key;
int cget_num;
int *cget_ptr;
key_t prod_key; 
key_t pmtx_key;
int prod_sem;
int pmtx_sem;
key_t cons_key; 
key_t cmtx_key;
int cons_sem;
int cmtx_sem;
int sem_val;
int sem_flg;
int shm_flg;
//消息队列相关参数:等候室的、沙发的
//信号量相关参数：顾客的、账本的
int wait_quest_flg;
key_t wait_quest_key;
int wait_quest_id;
int wait_respond_flg;
key_t wait_respond_key;
int wait_respond_id;
int sofa_quest_flg;
key_t sofa_quest_key;
int sofa_quest_id;
int sofa_respond_flg;
key_t sofa_respond_key;
int sofa_respond_id; 
key_t customer_key;
int customer_sem;
key_t account_key;
int account_sem;
int main(int argc,char *argv[]){
    int rate;
    if(argv[1]!=NULL) rate = atoi(argv[1]);
    else rate = 3;
    Msg_buf msg_arg;
    //创建等候室消息队列
    wait_quest_flg=IPC_CREAT|0644;
    wait_quest_key = 111;
    wait_quest_id = set_msq(wait_quest_key,wait_quest_flg);
    //等候室响应队列
    wait_respond_flg=IPC_CREAT|0644;
    wait_respond_key=112;
    wait_respond_id=set_msq(wait_respond_key,wait_respond_flg);
    //沙发等待队列
    sofa_quest_flg=IPC_CREAT|0644;
    sofa_quest_key = 211;
    sofa_quest_id = set_msq(sofa_quest_key,sofa_quest_flg);
    sofa_respond_flg=IPC_CREAT|0644;
    sofa_respond_key=212;
    //沙发相应队列
    sofa_respond_id=set_msq(sofa_respond_key,sofa_respond_flg);
    //顾客信号量
    customer_key=311;
    sem_flg=IPC_CREAT|0644;
    sem_val=0;
    customer_sem=set_sem(customer_key,sem_val,sem_flg);
    //账本信号量
    account_key=312;
    sem_flg=IPC_CREAT|0644;
    sem_val=1;
    account_sem=set_sem(account_key,sem_val,sem_flg);

    int pid1,pid2;
    pid1=fork();
    if(pid1==0){
        while(1){
            printf("1号理发师睡眠中...\n");
            sofa_quest_flg=0;
            if(msgrcv(sofa_quest_id,&msg_arg,sizeof(msg_arg),0,sofa_quest_flg)>=0){
                //查询是否沙发向理发椅的请求（要理发了）
                msgsnd(sofa_respond_id,&msg_arg,sizeof(msg_arg),0);
                printf("1号理发师正为 %d 号顾客理发...\n",msg_arg.mid);
                //理发时进程睡眠比较符合常理
                sleep(rate);
                //结账时占用账本
                down(account_sem);
                printf("1号理发师正为 %d 号顾客结账\n",msg_arg.mid);
                //释放账本
                up(account_sem);
            }else{
                printf("1号理发师正在睡觉...\n");
            }
        }
    }
    else{
        pid2=fork();
        if(pid2==0){
            while(1){
                printf("2号理发师睡眠中...\n");
                sofa_quest_flg=0;
                if(msgrcv(sofa_quest_id,&msg_arg,sizeof(msg_arg),0,sofa_quest_flg)>=0){
                msgsnd(sofa_respond_id,&msg_arg,sizeof(msg_arg),0);
                printf("2号理发师正为 %d 号顾客理发...\n",msg_arg.mid);
                sleep(rate);
                down(account_sem);
                printf("2号理发师正为 %d 号顾客结账\n",msg_arg.mid);
                up(account_sem);
                }else{
                    printf("2号理发师正在睡觉...\n");
                }
            }
        }
        else{
            while(1){
                printf("3号理发师睡眠中...\n");
                sofa_quest_flg=0;
                if(msgrcv(sofa_quest_id,&msg_arg,sizeof(msg_arg),0,sofa_quest_flg)>=0){
                //quest for sofa message queue
                msgsnd(sofa_respond_id,&msg_arg,sizeof(msg_arg),0);
                printf("3号理发师正为 %d 号顾客理发...\n",msg_arg.mid);
                sleep(rate);
                down(account_sem);
                printf("3号理发师正为 %d 号顾客结账\n",msg_arg.mid);
                up(account_sem);
                }else{
                    printf("3号理发师正在睡觉...\n");
                }
            }
        }
    }
    return EXIT_SUCCESS;
}
