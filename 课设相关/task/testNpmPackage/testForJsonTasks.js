const TaskScheduler = require('equiv-task-scheduler')

var Tasks = new Array()
Tasks.push(
    {
        name:'a',
        start:0,
        end:4
    }
)
Tasks.push(
    {
        name:'b',
        start:0,
        end:4
    }
)
Tasks.push(
    {
        name:'c',
        start:1,
        end:2
    }
)
Tasks.push(
    {
        name:'d',
        start:2,
        end:3
    }
)

console.log(TaskScheduler.genResult(Tasks));
