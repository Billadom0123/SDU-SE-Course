<template>
    <el-dialog
    v-model="dialogfirstVisible"
    title="新增任务"
    width="30%"
  >

  <el-form :model="form" label-width="120px">
    <el-form-item label="任务名称">
      <el-input v-model="form.name" />
    </el-form-item>
    
    <el-form-item 
    v-for="(start,index) in starts"
    :label="'开始时间'+index"
    :key="start.key"
    :prop="'starts.'+index+'.value'"
    >
    <!-- v-model不能直接修改数组元素的别名 -->
      <el-select v-model="start.value" placeholder="选择开始时间">
        <el-option v-for="(time,index) in 24" :label="index+':00'" :value="index" />
      </el-select> 
      <el-button @click.prevent="removeStart(start)">删除</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="addStart">新增开始时间</el-button>
    </el-form-item>

    <el-form-item 
    v-for="(end,index) in ends"
    :label="'结束时间'+index"
    :key="end.key"
    :prop="'ends.'+index+'.value'"
    >
        <el-select v-model="end.value" placeholder="选择结束时间">
          <el-option v-for="(time,index) in 24" :label="time+':00'" :value="time" />
        </el-select> 
        <el-button @click.prevent="removeEnd(end)">删除</el-button>
      </el-form-item>
      <el-form-item>
      <el-button @click="addEnd">新增结束时间</el-button>
    </el-form-item>
    <el-form-item label="执行时间">
        <el-select v-model="form.exeTime" placeholder="选择结束时间(默认1)">
          <el-option v-for="(time,index) in 24" :label="time+'小时'" :value="time" />
        </el-select> 
      </el-form-item>
    <el-form-item label="优先级">
        <el-select v-model="form.weight" placeholder="选择结束时间(默认1)">
          <el-option v-for="(weight,index) in 5" :label="weight+'级'" :value="weight" />
        </el-select> 
      </el-form-item>
  </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogfirstVisible = false">取消</el-button>
        <el-button type="primary" @click="addTask">
          新增
        </el-button>
      </span>
    </template>
  </el-dialog>

    <el-dialog
    v-model="resultVisible"
    title="调度结果"
    width="70%"
  >
  <el-table
  :data="result"
  style="width: 100%"
  :row-class-name="tableRowClassName"
>
  <el-table-column prop="time" label="开始结束时间" width="180" />
  <el-table-column prop="name" label="任务名称" width="180" />
  <el-table-column prop="exeTime" label="该任务总执行时间" width="180" />
  <el-table-column prop="weight" label="优先级" width="100" />
</el-table>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="resultVisible = false">取消</el-button>
      </span>
    </template>
  </el-dialog>


    <div class="wrapper">
        <div class="roughGlassWrapper">
            <div class="roughGlass">
                <el-tabs  class="demo-tabs" >
                    <el-tab-pane label="操作区域">
                      <el-button type="warning" @click="this.dialogfirstVisible=true">新增任务</el-button>
                      <el-button type="primary" @click="sendTasks()">提交任务</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="退出登录">
                      <el-button type="danger" @click="logOut()">
                        退出登录
                      </el-button>
                    </el-tab-pane>
                  </el-tabs>
            </div>
        </div>

        <div class="todoWrapper">
            <section id="todoapp">
                <section class="main">
                    <ul class="todo-list">
                        <li class="todo" v-for="(task,index) in tasks">
                            <div class="view" >
                                <span class="index">{{index+1}}.</span>
                                <label>
                                    任务名:{{task.name}}
                                    开始:{{ arrToString(task.start) }}
                                    结束:{{ arrToString(task.end) }}
                                    执行时间:{{ task.exeTime+'h' }}
                                    优先级:{{ task.weight }}
                                </label>
                                <button class="destroy" @click="remove(index)"></button>
                            </div>
                        </li>
                    </ul>
                </section>
                <footer class="footer" v-show="tasks.length!==0">
                    <span class="todo-count" v-if="tasks.length!==0">
                        <strong>{{tasks.length}}</strong> in total
                    </span>
                    <button v-show="tasks.length!==0" class="clear-completed" @click="clear">
                    Clear
                    </button>
                </footer>
            </section>
        </div>
    </div>

</template>

<script>
import {radixSortTasks} from '../util/sortUtil'
import {genWMResultVeryFirst} from '../service/genService'
import {minHeap} from '../util/Entity/minHeap'
export default {
    data() {
        return {
            startHeap:null,
            endHeap:null,
            starts:[
              {
                value:null
              }
            ],
            ends:[
              {
                value:null
              }
            ],
            form:{
                name:'',
                start:null,
                end:null,
                exeTime:1,
                weight:1
            },
            dialogfirstVisible:false,
            resultVisible:false,
    tasks:
    [
    {
        name:'A',
        start:[null,2,13],
        end:[null,6,16],
        exeTime:3,
        weight:1 
    },
    {
        name:'B',
        start:[null,13,19],
        end:[null,17,24],
        exeTime:2,
        weight:4 
    },
    {
        name:'C',
        start:[null,5,14],
        end:[null,8,16],
        exeTime:4,
        weight:5 
    },
    {
        name:'D',
        start:[null,9,18],
        end:[null,15,20],
        exeTime:3,
        weight:5 
    },
    {
        name:'E',
        start:[null,10,17],
        end:[null,12,19],
        exeTime:1,
        weight:4 
    },
    {
        name:'F',
        start:[null,4,20],
        end:[null,8,24],
        exeTime:1,
        weight:1 
    }

    // {
    //     name:'A',
    //     start:[null,2],
    //     end:[null,12],
    //     exeTime:3,
    //     weight:5
    // },
    // {
    //     name:'B',
    //     start:[null,3],
    //     end:[null,21],
    //     exeTime:1,
    //     weight:5
    // },
    // {
    //     name:'C',
    //     start:[null,13],
    //     end:[null,16],
    //     exeTime:3,
    //     weight:2
    // },
    // {
    //     name:'D',
    //     start:[null,18],
    //     end:[null,20],
    //     exeTime:1,
    //     weight:4
    // },
    // {
    //     name:'E',
    //     start:[null,16],
    //     end:[null,23],
    //     exeTime:2,
    //     weight:5
    // },
    // {
    //     name:'F',
    //     start:[null,18],
    //     end:[null,22],
    //     exeTime:1,
    //     weight:3
    // }

            ],

        result:[
        ]

        }
    },
    mounted() {
    },
    methods: {
      //给时间数组的转换字符串函数
      arrToString(arr){
        var str=''
        for(let i=1;i<arr.length;i++){
          str+=arr[i]+':00'+' '
        }
        return str
      },
      removeEnd(end){
        var index = this.ends.indexOf(end)
        if(end!== -1){
          this.ends.splice(index,1)
        }
      },
      addEnd(){
        this.ends.push(
          {
            value:null,
            key: Date.now()
          }
        )
      },
      removeStart(start){
        var index = this.starts.indexOf(start)
        if(start!== -1){
          this.starts.splice(index,1)
        }
      },
      addStart(){
        this.starts.push(
          {
            value:null,
            key: Date.now()
          }
        )
      },
      sendTasks(){
            genWMResultVeryFirst(this.tasks).then(
              (res)=>{
                // console.log(res);
                if(res.status===0){
                  let result = res.data
                  result=this.preProcessResult(result)
                  this.result=result
                  this.resultVisible=true
                }
                else if(res.status===1){
                  this.$message(
                    {
                      message:res.message,
                      type:'error'
                    }
                  )
                }
              }
            )
            
      },
      //用堆来维护输入
      // timeDivision(){
      //   this.startHeap=new minHeap()
      //   this.endHeap = new minHeap()
      //   for(let i=0;i<this.starts.length;i++){
      //     console.log(i);
      //     //先把开始时间放入开始堆
      //     if(i===0){
      //       this.startHeap.push(this.starts[i].value)
      //     }
      //     else{
      //       //多个时段不允许有交集
      //       if(this.starts[i].value<this.endHeap.top()&&this.ends[i].value>this.startHeap.top()){
      //         return false
      //       }
      //       else{
      //         this.startHeap.push(this.starts[i].value)
      //       }
      //     }
      //     //再把结束时间放入结束堆
      //     //结束时间要比开始时间晚
      //     if(this.ends[i].value<=this.startHeap.top()){
      //       return false
      //     }
      //     else{
      //       this.endHeap.push(this.ends[i].value)
      //     }
      //   }
      //   return true
      // },
      //上面函数有bug，弃用了。凭什么跟堆顶没交集就跟所有的没交集？
        timeDivision(){
          this.startHeap = new minHeap()
          this.endHeap = new minHeap()
          var flag = true
          for(let i=0;i<this.starts.length;i++){
            if(i===0){
              this.startHeap.push(this.starts[i].value)
            }
            else{
              //检查是否有交集
              let currentHeapLength = this.startHeap.heap.length
              for(let j = 1;j < currentHeapLength;j++){
                if(this.starts[i].value<this.endHeap.heap[j]&&this.ends[i].value>this.startHeap.heap[j]){
                  return false
                }
              }
              this.startHeap.push(this.starts[i].value)
            }
            if(this.ends[i].value<=this.startHeap.top()){
              return false
            }
            else{
              this.endHeap.push(this.ends[i].value)
            }
          }
          return true
        },
        addTask(){
          if(!this.timeDivision()){
            console.log(this.startHeap);
            console.log(this.endHeap);
            this.$message(
              {
                message:'请检查输入，多个时段不允许相交，且每个时段的结束时间应大于开始时间',
                type:'error'
              }
            )
            return
          }
          //更新表单数据
          this.form.start=this.startHeap.heap
          this.form.end=this.endHeap.heap
            this.tasks.push
            (
                {
                    name:this.form.name,
                    start:this.form.start,
                    end:this.form.end,
                    exeTime:this.form.exeTime,
                    weight:this.form.weight
                }
            )
            this.tasks = radixSortTasks(this.tasks)
            this.form.name=''
        },
        preProcessResult(result){
          var newResult = new Array()
          let i=1
          for(i;i<result.length;i++){
            let task = result[i]
            if(task.name!==''&&task.name!==null&&task.name!==undefined){
              newResult.push
              (
                {
                  time:(i-1)+':00'+'-->'+(i)+':00',
                  name:task.name,
                  exeTime:task.exeTime,
                  weight:task.weight
                }
              )
            }
          }
          return newResult
        },
        addExe(result){
          let i = 0
          for(i;i<result.length;i++){
            result[i].exeTime=1
          }
          return result
        },
        addWeight(result){
          let i = 0
          for(i;i<result.length;i++){
            result[i].weight=1
          }
          return result
        },
        clear(){
          this.tasks=[]
        },
        remove(index){
          this.tasks.splice(index,1)
        },
        tableRowClassName({row, rowIndex}) {
        if (rowIndex === 1) {
          return 'warning-row';
        } else if (rowIndex === 3) {
          return 'success-row';
        }
        return '';
      },
        logOut(){
          this.$store.dispatch("logOutCommit")
          this.$router.push('/login')
        }
    },

}
</script>

<style scoped>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
.roughGlassWrapper{
    width: 100%;
    padding-top: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.roughGlass{
    position: relative;
    width: 300px;
    height: 200px;
    display: flex;
    border-radius: 15px;
    justify-content: center;
    align-items: center;
    background: linear-gradient(
        to right bottom,
        rgba(255,255,255,.7),
        rgba(255,255,255,.5),
        rgba(255,255,255,.4)
    );
    /* 使背景模糊化 */
    backdrop-filter: blur(10px);
    box-shadow: 0 0 20px #a29bfe;
}

.demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }
.wrapper{
    min-height: 100vh;
    width: 100%;
    background: url('../assets/background.png') repeat-y;
    background-size:cover
}

.todoWrapper{
    position: absolute;
    display:flex;
    align-items: center;
    justify-content: center;
    width:100%;
    top: 65%;
    transform: translateY(-50%);
}

:focus {
  outline: 0;
}

#todoapp {
  background: #DCDFE6;
  position: relative;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.2), 0 25px 50px 0 rgba(0, 0, 0, 0.1);
  width: 80%;
}

#todoapp input::-webkit-input-placeholder {
  font-style: italic;
  font-weight: 300;
  color: #e6e6e6;
}

#todoapp input::-moz-placeholder {
  font-style: italic;
  font-weight: 300;
  color: #e6e6e6;
}

#todoapp input::input-placeholder {
  font-style: italic;
  font-weight: 300;
  color: gray;
}

#todoapp h1 {
  position: absolute;
  top: -160px;
  width: 100%;
  font-size: 60px;
  font-weight: 100;
  text-align: center;
  color: rgba(175, 47, 47, .8);
  -webkit-text-rendering: optimizeLegibility;
  -moz-text-rendering: optimizeLegibility;
  text-rendering: optimizeLegibility;
}

.main {
  position: relative;
  z-index: 2;
  border-top: 1px solid #e6e6e6;
}

.todo-list {
  margin: 0;
  padding: 0;
  list-style: none;
  max-height: 420px;
  overflow: auto;
}

.todo-list li {
  position: relative;
  font-size: 24px;
  border-bottom: 1px solid #ededed;
  height: 60px;
  box-sizing: border-box;

}

.todo-list li:last-child {
  border-bottom: none;
}

.todo-list .view .index {
  position: absolute;
  color: gray;
  left: 10px;
  top: 20px;
  font-size: 16px;

}

.todo-list li .toggle {
  text-align: center;
  width: 40px;
  /* auto, since non-WebKit browsers doesn't support input styling */
  height: auto;
  position: absolute;
  top: 0;
  bottom: 0;
  margin: auto 0;
  border: none; /* Mobile Safari */
  -webkit-appearance: none;
  appearance: none;
}

.todo-list li .toggle {
  opacity: 0;
}

.todo-list li .toggle + label {
  /*
		Firefox requires `#` to be escaped - https://bugzilla.mozilla.org/show_bug.cgi?id=922433
		IE and Edge requires *everything* to be escaped to render, so we do that instead of just the `#` - https://developer.microsoft.com/en-us/microsoft-edge/platform/issues/7157459/
	*/
  background-image: url("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center left;
}

.todo-list li .toggle:checked + label {
  background-image: url("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23bddad5%22%20stroke-width%3D%223%22/%3E%3Cpath%20fill%3D%22%235dc2af%22%20d%3D%22M72%2025L42%2071%2027%2056l-4%204%2020%2020%2034-52z%22/%3E%3C/svg%3E");
}

.todo-list li label {
  word-break: break-all;
  padding: 15px 15px 15px 60px;
  display: block;
  line-height: 1.2;
  transition: color 0.4s;
}

.todo-list li.completed label {
  color: #d9d9d9;
  text-decoration: line-through;
}

.todo-list li .destroy {
    margin: 0;
    padding: 0;
    border: 0;
    background: none;
    font-size: 100%;
    vertical-align: baseline;
    font-family: inherit;
    font-weight: inherit;
    color: inherit;
    -webkit-appearance: none;
    appearance: none;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  display: none;
  position: absolute;
  top: 0;
  right: 10px;
  bottom: 0;
  width: 40px;
  height: 40px;
  margin: auto 0;
  font-size: 30px;
  color: #cc9a9a;
  margin-bottom: 11px;
  transition: color 0.2s ease-out;
}

.todo-list li .destroy:hover {
  color: #af5b5e;
}

.todo-list li .destroy:after {
  content: "×";
}

.todo-list li:hover .destroy {
  display: block;
}

.todo-list li .edit {
  display: none;
}

.todo-list li.editing:last-child {
  margin-bottom: -1px;
}

.footer {
  color: #777;
  padding: 10px 15px;
  height: 20px;
  text-align: center;
  border-top: 1px solid #e6e6e6;
}

.footer:before {
  content: "";
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
  height: 50px;
  overflow: hidden;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2), 0 8px 0 -3px #f6f6f6,
    0 9px 1px -3px rgba(0, 0, 0, 0.2), 0 16px 0 -6px #f6f6f6,
    0 17px 2px -6px rgba(0, 0, 0, 0.2);
}

.todo-count {
  float: left;
  text-align: left;
}

.todo-count strong {
  font-weight: 300;
}

.clear-completed,
html .clear-completed:active {
    margin: 0;
    padding: 0;
    border: 0;
    background: none;
    font-size: 100%;
    vertical-align: baseline;
    font-family: inherit;
    font-weight: inherit;
    color: inherit;
    -webkit-appearance: none;
    appearance: none;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  float: right;
  position: relative;
  line-height: 20px;
  text-decoration: none;
  cursor: pointer;
}

.clear-completed:hover {
  text-decoration: underline;
}

@media screen and (-webkit-min-device-pixel-ratio: 0) {
  .toggle-all,
  .todo-list li .toggle {
    background: none;
  }

  .todo-list li .toggle {
    height: 40px;
  }
}

@media (max-width: 430px) {
  .footer {
    height: 50px;
  }

}

</style>