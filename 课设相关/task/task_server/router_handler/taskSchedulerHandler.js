const db = require('../db/datasource')
const TaskScheduler = require('equiv-task-scheduler')

//提取返回任务调度结果数组
function resSendResult(result,res){
    if(result===null){
        res.cc('抱歉,对于这样的任务列表不存在一个完全可行的调度方案')
    }
    else{
        res.send
        (
            {
                status:0,
                data:result,
                message:'已为您生成调度方案'
            }
        )
    }
}

/**
 * 前端过来的json数组应该是
 *
 *JsonTasks:
 [
    {
        name:'A',
        start:0,
        end:24,
        exeTime:1,
        weight:1
    }
    ,
    ...
 ]
 */
exports.genWMResultVeryFirst = (req,res)=>{
    const WMJsonTasks = req.body
    console.log(WMJsonTasks);
    var tasks = JSON.parse(WMJsonTasks.data)
    // console.log(tasks);
    const WMResult = TaskScheduler.genWMResultVeryFirst(tasks)
    console.log(WMResult);
    resSendResult(WMResult,res)
}