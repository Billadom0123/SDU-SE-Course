#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/msg.h>
#include <unistd.h>
#include<signal.h>
#define BUFSZ 256 
int get_ipc_id(char *proc_file,key_t key);
char *set_shm(key_t shm_key,int shm_num,int shm_flag);
int set_msq(key_t msq_key,int msq_flag);
int set_sem(key_t sem_key,int sem_val,int sem_flag);
int down(int sem_id);
int up(int sem_id);
typedef union semuns {
 int val;
} Sem_uns; 
typedef struct msgbuf {
 long mtype;
 int mid;
// char mtext[1];
} Msg_buf; 
// key_t buff_key;
// int buff_num;
// char *buff_ptr;
// key_t pput_key;
// int pput_num;
// int *pput_ptr;
// key_t cget_key;
// int cget_num;
// int *cget_ptr;
// key_t prod_key; 
// key_t pmtx_key;
// int prod_sem;
// int pmtx_sem;
// key_t cons_key; 
// key_t cmtx_key;
// int cons_sem;
// int cmtx_sem;
// int sem_val;
// int sem_flg;
// int shm_flg;
// //消息队列相关参数:等候室的、沙发的
// //信号量相关参数：顾客的、账本的
// int wait_quest_flg;
// key_t wait_quest_key;
// int wait_quest_id;
// int wait_respond_flg;
// key_t wait_respond_key;
// int wait_respond_id;
// int sofa_quest_flg;
// key_t sofa_quest_key;
// int sofa_quest_id;
// int sofa_respond_flg;
// key_t sofa_respond_key;
// int sofa_respond_id; 
// key_t customer_key;
// int customer_sem;
// key_t account_key;
// int account_sem;