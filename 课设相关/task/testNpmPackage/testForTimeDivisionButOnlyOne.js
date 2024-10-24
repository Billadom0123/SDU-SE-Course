const TaskScheduler = require('equiv-task-scheduler')
var WMJsonTasks = new Array()
WMJsonTasks.push
(
    {
        name:'A',
        start:[null,2],
        end:[null,12],
        exeTime:3,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'B',
        start:[null,3],
        end:[null,21],
        exeTime:1,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'C',
        start:[null,13],
        end:[null,16],
        exeTime:3,
        weight:2
    }
)

WMJsonTasks.push
(
    {
        name:'D',
        start:[null,18],
        end:[null,20],
        exeTime:1,
        weight:4
    }
)

WMJsonTasks.push
(
    {
        name:'E',
        start:[null,16],
        end:[null,23],
        exeTime:2,
        weight:5
    }
)

WMJsonTasks.push
(
    {
        name:'F',
        start:[null,18],
        end:[null,22],
        exeTime:1,
        weight:3
    }
)
var WMResult = TaskScheduler.genWMResultVeryFirst(WMJsonTasks)
console.log(WMResult);