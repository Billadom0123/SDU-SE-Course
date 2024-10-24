const {TaskNode} = require('./TaskNode')

class TaskChain{
    firstNode
    size

    constructor(firstNode,size){
        this.firstNode=firstNode
        this.size=size
    }

    //链表判空
    empty() {
        return this.size===0    
    }

    //获取指定索引的元素
    get(index){
        if(index>=this.size){
            return null
        }
        var currentNode = this.firstNode
        for(i=0;i<index;i++){
            currentNode = currentNode.next
        }
        return currentNode.value
    }

    //获取元素首个出现的位置
    indexOf(task){
        var currentNode = this.firstNode
        var index=0
        while(currentNode!==null){
            if(currentNode.value===task){
                return index
            }
            index += 1
            currentNode=currentNode.next
        }
        return -1
    }

    //删除指定索引的元素
    erase(index){
        if(index===0){
            this.firstNode = this.firstNode.next
        }
        else{
            var currentNode = this.firstNode
            for(i=0;i<index-1;i++){
                currentNode.next = currentNode.next.next
            }
        }
        this.size +=(-1)
    }

    //向指定索引插入元素
    insert(ele,index){
        if(index<0 || index>this.size){
            return
        }
        if(index===0){
            this.firstNode = new TaskNode(ele,this.firstNode)
        }
        else{
            var currentNode = this.firstNode
            for(i=0;i<index-1;i++){
                currentNode=currentNode.next
            }
            currentNode.next=new TaskNode(ele,currentNode.next)
        }
        this.size++
    }

    //遍历链表 复制到目标数组中
    copyToArray(tasks){
        tasks=new Array()
        var currentNode = this.firstNode
        while(currentNode!==null){
            tasks.push(currentNode.value)
            currentNode=currentNode.next
        }
        return tasks
    }

}

module.exports=
{
    TaskChain
}