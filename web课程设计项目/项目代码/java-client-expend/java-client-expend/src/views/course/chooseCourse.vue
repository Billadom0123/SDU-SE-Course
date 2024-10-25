<template>
    <Sidebar/>
    <div class="position">
        <a href="javascript:;" @click="gotoIndex">首页</a>
        /
        <a href="javascript:;" >课程中心</a>
        /
        <a href="javascript:;" >选课管理</a>
    </div>

    <!-- 一个是开放选课时的盒子 -->
    <div class="open" ref="open">
            <el-form :inline="true" :model="conditions" class="conditions">
              <el-form-item label="课程名称">
                <el-input v-model="conditions.name" placeholder="课程名称" />
              </el-form-item>
              <el-form-item label="学分">
                <el-input v-model="conditions.credit" placeholder="学分" />
              </el-form-item>
              <br>
              <el-form-item label="授课老师">
                <el-input v-model="conditions.teacher" placeholder="授课老师" />
              </el-form-item>
              <el-form-item label="课程性质">
                <el-select v-model="conditions.prop" placeholder="课程性质(必选项)">
                  <el-option label="必修课" value="必修课" />
                  <el-option label="限选课" value="限选课" />
                  <el-option label="任选课" value="任选课" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="query()" plain><img src="../../../../icon/search.png" alt="" style="width:20px;height:20px;"></el-button>
              </el-form-item>
            </el-form>
        <el-table :data="tableData" height="400" style="width: 100%">
            <el-table-column fixed prop="name" label="课程名称" width="300" />
            <el-table-column prop="currentSize/upperLimit" label="选课情况" width="200" />
            <el-table-column prop="credit" label="学分" width="200" />
            <el-table-column prop="teacher" label="授课教师" width="200" />
            <el-table-column prop="scope" label="周期" width="200" />
            <el-table-column prop="place" label="地点" width="300" />
            <el-table-column prop="prop" label="课程性质" width="200" />
            <el-table-column prop="time" label="授课时间" width="600" />

            <el-table-column fixed="right" label="Operations" width="200">
                <template #default="scope">
                    <el-button  type="primary"  @click="choose(scope.$index, scope.row)" plain 
                    v-if="scope.row.selected=='false'">选课</el-button
                    >
                    <el-button  type="danger"  @click="reject(scope.row)"  plain 
                    v-if="scope.row.selected=='true'">退选</el-button>
                  </template>
            </el-table-column>
          </el-table>
          <div class="progress">
            本学期学分:{{creditsum}}/35.0
            <el-progress :percentage=this.percentage />
          </div>

    </div>

    <div class="close" ref="close">
        抱歉,选课系统已经关闭。请联系管理员。
    </div>
</template>

<script>
import Sidebar from "@/components/SideBar"
import {computeCredit,showSelectList,chooseCourse,rejectCourse,courseQuery,checkIsOpen} from "@/service/genServ.js"
export default{
    name:"chooseCourse",
    components:{
        Sidebar,
    },
    data() {
        return {
            isOpen:'',
            creditsum:0,
            percentage:0,
            conditions:{
                name:'',
                credit:'',
                teacher:'',
                prop:'',
            },
            tableData:[]
        }
    },
    mounted(){
      checkIsOpen().then(
        (res)=>{
          console.log(res.data.data);
          this.isOpen=res.data.data;
          console.log(this.isOpen);
          if(this.isOpen.isOpen=='true'){
            this.$refs.open.style.display='block';
            this.$refs.close.style.display='none';
        }else{
            this.$refs.open.style.display='none';
            this.$refs.close.style.display='block';
        }
        }
      )
       
        
        //展示可以选的课
        showSelectList().then(
          (res)=>{
            //console.log(res.data.data);
            this.tableData=res.data.data;
            //var arr = res.data.data;
            //for(var i=0;i<arr.length;i++){
              //if(arr[i].selected=="false"){
                // this.$refs.choose.style.display="block";
                // this.$refs.reject.style.display="none";
              //}else{
                // this.$refs.choose.style.display="block";
                // this.$refs.reject.style.display="block";
              //}
            //}
          }
        )
        //计算已选择的学分
        computeCredit().then(
          (res)=>{
            //console.log(res);
            this.creditsum=res.data.data[0].result;
            var percentage=this.creditsum/35.0*100;
        this.percentage=percentage.toFixed(1);
          }
        )


    },
    methods:{
    //去首页
    gotoIndex: function(){
        this.$router.push({
            path:'index',
        })
        return false;
    },
    //选课
    choose(id, row){
        console.log(id + ", " + row.name + ", " +this.percentage);
        if(row.credit+this.creditsum>35){
          this.$message({
              message: '已选学分超过35分！',
              type: 'warning',
            })
            return;            
        }
        chooseCourse(row).then(
          (res)=>{
            console.log(res);
            if(res.code=="0"){
              this.$message({
              message: '选课成功!',
              type: 'success',
            })            
            }
          }
        );
        //选完刷新以下，让用户知道自己选上了
        location.reload();
    },
    //退选
    reject(row){
        rejectCourse(row).then(
          (res)=>{
            if(res.code=='0'){
              this.$message({
              message: '退选成功!',
              type: 'success',
            })        
            }
          }
        )
        location.reload();
    },
    query(){
      // if(this.conditions.prop==''){
      //   this.$message({
      //         message: '请选择课程性质!',
      //         type: 'warning',
      //       })
      //       return;       
      // }
        courseQuery(this.conditions).then(
          (res)=>{
            console.log(res);
            this.tableData=res.data.data;
          }
        )
    }
    }
}
</script>

<style scoped>
.position{
    color: #97A8BE;
    position:absolute;
    left: 0;
    top: 0;
    transform:translate(60px,12px);
}
.position a{
    color: #97A8BE !important;
    text-decoration: none;
}
.open{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}

.close{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}

.progress{
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    width: 50%;
    margin-top: 50px;
}

.conditions{
    width: 1200px !important;
}
</style>