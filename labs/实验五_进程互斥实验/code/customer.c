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
    else rate = 1;
    //消息队列、信号量配置与producer相同
    Msg_buf msg_arg;
    wait_quest_flg=IPC_CREAT|0644;
    wait_quest_key = 111;
    wait_quest_id = set_msq(wait_quest_key,wait_quest_flg);
    wait_respond_flg=IPC_CREAT|0644;
    wait_respond_key=112;
    wait_respond_id=set_msq(wait_respond_key,wait_respond_flg);
    sofa_quest_flg=IPC_CREAT|0644;
    sofa_quest_key = 211;
    sofa_quest_id = set_msq(sofa_quest_key,sofa_quest_flg);
    sofa_respond_flg=IPC_CREAT|0644;
    sofa_respond_key=212;
    sofa_respond_id=set_msq(sofa_respond_key,sofa_respond_flg);
    customer_key=311;
    sem_flg=IPC_CREAT|0644;
    sem_val=0;
    customer_sem=set_sem(customer_key,sem_val,sem_flg);
    account_key=312;
    sem_flg=IPC_CREAT|0644;
    sem_val=1;
    account_sem=set_sem(account_key,sem_val,sem_flg);
    //一开始沙发上没人，等候室也没人，顾客id从1开始记录
    int sofa_count=0;
    int wait_count=0;
    int id=0;
    int current_id=0;
    while(1){
        sleep(rate);
        id++;
        msg_arg.mid=id;
        //若沙发有空位置
        if(sofa_count<4){
            //如果等候室有顾客，则将这个顾客从等候室挪到沙发上
            if(wait_count!=0)
            {
                wait_quest_flg=IPC_NOWAIT;
                msgrcv(wait_quest_id,&msg_arg,sizeof(msg_arg),0,wait_quest_flg);
                msgsnd(wait_respond_id,&msg_arg,sizeof(msg_arg),0);
                printf(" %d 号顾客从等候室来到沙发上\n",current_id++);
                //沙发上等候的顾客数量增加
                sofa_count++;
            }
            //等候室没人，直接来到沙发上
            else{
                printf(" %d 号顾客(新)来到沙发上\n",id);
                sofa_count++;
            }
            sofa_quest_flg=IPC_NOWAIT;
            //追加新记录到沙发的消息队列中
            msgsnd(sofa_quest_id,&msg_arg,sizeof(msg_arg),sofa_quest_flg);
        }
        //沙发上没位置了，看等候室是否还能坐
        else if(wait_count<13){
            printf("沙发已满,%d 号顾客来到等候室\n",id);
            //记录当前等候室等待最久的一个顾客
            if(wait_count>0){
                current_id=current_id;
            }
            else if(wait_count==0){
                current_id = id;
            }
            wait_quest_flg=IPC_NOWAIT;
            msgsnd(wait_quest_id,&msg_arg,sizeof(msg_arg),wait_quest_flg);
            wait_count++;
        }
        //等候室已满，顾客离开
        else{
            printf("等候室已满, %d 号顾客离开\n",id);
        }
        sofa_respond_flg=IPC_NOWAIT;
        //如果两个消息队列中的一条记录被读出，等候人数按情况分别减1
        if(msgrcv(sofa_respond_id,&msg_arg,sizeof(msg_arg),0,sofa_respond_flg)>0){
            sofa_count--;
        }
        wait_quest_flg=IPC_NOWAIT;
        if(msgrcv(wait_respond_id,&msg_arg,sizeof(msg_arg),0,wait_quest_flg)>0){
            wait_count--;
        }
    }
    return EXIT_SUCCESS;
}
