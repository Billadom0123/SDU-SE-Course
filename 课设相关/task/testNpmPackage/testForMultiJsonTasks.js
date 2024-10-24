const TaskScheduler = require('equiv-task-scheduler')

var MultiJsonTasks = new Array()
MultiJsonTasks.push
(
    {
        name:"a",
        start:1,
        end:5,
        exeTime:2
    }
)
MultiJsonTasks.push
(
    {
        name:"b",
        start:0,
        end:7,
        exeTime:3
    }
)
MultiJsonTasks.push
(
    {
        name:"c",
        start:2,
        end:3,
        exeTime:1
    }
)
MultiJsonTasks.push
(
    {
        name:"d",
        start:4,
        end:5,
        exeTime:1
    }
)
MultiResult=TaskScheduler.genMultiResult(MultiJsonTasks)
console.log(MultiResult);

// MultiJsonTasks = new Array()
// MultiJsonTasks.push
// (
//     {
//         name:"d",
//         start:4,
//         end:5,
//         exeTime:1
//     }
// )
// MultiResult=TaskScheduler.genMultiResult(MultiJsonTasks)
// console.log(MultiResult);
// console.log(MultiResult.length);
