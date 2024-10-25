<template>
<SideBarForAdmin/>
<div class="position">
    <a href="javascript:;" >首页</a>
    /
    <a href="javascript:;" >选课管理</a>

</div>
<div class="card">
    <div class="title">选课开放关闭设置</div>
    <el-divider></el-divider>
    <div class="picker">
        <div class="block">
            <span class="demonstration">开始时间&nbsp;</span>
            <el-date-picker
              v-model="startTime"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </div>
          <div class="block">
            <span class="demonstration">结束时间&nbsp;</span>
            <el-date-picker
              v-model="endTime"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </div>
    </div>
    <div class="btn"><el-button type="primary" @click="submit()">提交</el-button></div>
</div>
</template>

<script>
import SideBarForAdmin from "@/components/SideBarForAdmin";
import {    openCourse  } from '@/service/genServ.js'
export default{
    name:'OpenCloseCourse',
    data() {
        return {
            startTime:null,
            endTime:null,
        }
    },
    methods: {
        getBirthTime (val) {
    const newDate = val.getFullYear() + '-' + (val.getMonth() + 1) + '-' + val.getDate() + ' '
                    + val.getHours() + ':' + val.getMinutes() + ':' + val.getSeconds();
    return newDate;
},

        submit(){
            console.log(this.getBirthTime(this.startTime)+"  "+this.getBirthTime(this.endTime));
            openCourse({"startTime":this.getBirthTime(this.startTime),"endTime":this.getBirthTime(this.endTime)}).then(
                (res)=>{
                    // console.log(res.data);
                    if(res.data.msg==null){
                        this.$message({
                        message: '设置成功!',
                        type: 'success',
                    })   
                    }
                    else{
                        this.$message({
                        message: res.data.msg,
                        type: 'error',
                    })   
                    }
                }
            )
        }
    },
    components:{
        SideBarForAdmin,
    },

}
</script>

<style scoped>
.card{
    position: relative;
    width:80%;
    height: 70%;
    top: 40%;
    left: 50%;
    background-color: #fff;
    transform: translate(-50%,-50%);
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    border:1px solid #97A8BE;
    border-radius: 5px;
}

.title{
    margin-top: 15px;
    margin-bottom: 15px;
    font-size: 30px;
    font-weight: 500;
}

.picker{
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    transform:translate(-50%,-50%);
    display:flex;
}

.block{
    flex:1;
}

.btn{
    position: absolute;
    right: 20px;
    bottom: 20px;
}

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
</style>