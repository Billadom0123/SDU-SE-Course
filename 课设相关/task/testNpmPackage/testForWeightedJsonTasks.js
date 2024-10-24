const TaskScheduler = require('equiv-task-scheduler')

var WeightedJsonTasks = new Array()
WeightedJsonTasks.push
(
    {
        name:'A',
        start:0,
        end:5,
        weight:5
    }
)

WeightedJsonTasks.push
(
    {
        name:'C',
        start:2,
        end:7,
        weight:2
    }
)

WeightedJsonTasks.push
(
    {
        name:'D',
        start:0,
        end:7,
        weight:1
    }
)

WeightedJsonTasks.push
(
    {
        name:'E',
        start:2,
        end:6,
        weight:5
    }
)

WeightedJsonTasks.push
(
    {
        name:'F',
        start:2,
        end:4,
        weight:4
    }
)

var result = TaskScheduler.genWeightedResult(WeightedJsonTasks)
console.log(result);
