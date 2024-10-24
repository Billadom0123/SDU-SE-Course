const {Task} = require('./Entity/Task')
const {TaskNode} = require('./Entity/TaskNode')
const {TaskChain} = require('./Entity/TaskChain')
const constant = require('./config/constConfig')

/**
 * 
 * @param {任务列表} tasks 
 * @param {标记} status 
 * @param {桶范围} range 
 */
function binSortByStatus(tasks,status,range){
    //初始化桶
    var bins = new Array()
    for(i=0;i<=range;i++){
        bins.push(new TaskChain(null,0))
    }
    //分情况向桶中分配节点
    //首先是犯下开始之罪的开始
    if(status===1){
        for(i=0;i<tasks.length;i++){
            bins[tasks[i].start[1]].insert(
                tasks[i],0
            )
        }
    }
    //然后是犯下结束之罪的结束
    else if(status===2){
        for(i=0;i<tasks.length;i++){
            bins[tasks[i].end[1]].insert(
                tasks[i],0
            )
        }
    }
    //然后是犯下执行之罪的执行
    else if(status===3){
        for(i=0;i<tasks.length;i++){
            bins[tasks[i].exeTime].insert(
                tasks[i],0
            )
        }
    }
    //最后是犯下权重之罪的权重
    else if(status===4){
        for(i=0;i<tasks.length;i++){
            bins[tasks[i].weight].insert(
                tasks[i],0
            )
        }
    }
    //向链表中收集元素
    var resultChain = new TaskChain(null,0)
    if(status===1||status===2){
        for(j=range;j>=0;j--){
            while(!bins[j].empty()){
                let task = bins[j].get(0)
                resultChain.insert(task,0)
                bins[j].erase(0) 
            }
        }
    }
    else if(status===3||status===4){
        for(j=0;j<=range;j++){
            while(!bins[j].empty()){
                let task = bins[j].get(0)
                resultChain.insert(task,0)
                bins[j].erase(0) 
            }
        }
    }
    tasks=resultChain.copyToArray(tasks)
    return tasks
}

/**
 * 
 * @param {任务列表} tasks 
 */
function radixSortTasks(tasks){
    //先对执行时间排序
    tasks=binSortByStatus(tasks,constant.byExeTime,constant.exeTimeRange)
    //再对优先级进行排序
    tasks=binSortByStatus(tasks,constant.byWeight,constant.weightRange)
    //再对结束时间进行排序
    tasks=binSortByStatus(tasks,constant.byEnd,constant.endRange)
    //最后对开始时间进行排序
    tasks=binSortByStatus(tasks,constant.byStart,constant.startRange)
    return tasks
}

module.exports=
{
    radixSortTasks
}