const TaskScheduler = require('equiv-task-scheduler')
var WMJsonTasks = new Array()
WMJsonTasks.push
(
    {
        name:'A',
        start:2,
        end:12,
        exeTime:3,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'B',
        start:3,
        end:21,
        exeTime:1,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'C',
        start:13,
        end:16,
        exeTime:3,
        weight:2
    }
)

WMJsonTasks.push
(
    {
        name:'D',
        start:18,
        end:20,
        exeTime:1,
        weight:4
    }
)

WMJsonTasks.push
(
    {
        name:'E',
        start:16,
        end:23,
        exeTime:2,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'F',
        start:18,
        end:22,
        exeTime:1,
        weight:3
    }
)



var WMResult = TaskScheduler.genWMResult(WMJsonTasks)
console.log(WMResult);