const TaskScheduler = require('equiv-task-scheduler')
var tasks = new Array()
tasks.push
(
    {
        name:'A',
        start:[null,2,13],
        end:[null,6,16],
        exeTime:3,
        weight:1 
    }
)

tasks.push
(
    {
        name:'B',
        start:[null,13,19],
        end:[null,17,24],
        exeTime:2,
        weight:4 
    }
)

tasks.push
(
    {
        name:'C',
        start:[null,5,14],
        end:[null,8,16],
        exeTime:4,
        weight:5 
    }
)

tasks.push
(
    {
        name:'D',
        start:[null,9,18],
        end:[null,15,20],
        exeTime:3,
        weight:5 
    }
)

tasks.push
(
    {
        name:'E',
        start:[null,10,17],
        end:[null,12,19],
        exeTime:1,
        weight:4 
    }
)

tasks.push
(
    {
        name:'F',
        start:[null,4,20],
        end:[null,8,24],
        exeTime:1,
        weight:1 
    }
)

var result = TaskScheduler.genWMResultVeryFirst(tasks)
console.log(result);