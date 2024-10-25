<template>
    <SideBar/>
    <div class="position">
        <a href="javascript:;" @click.prevent="gotoIndex">首页</a>
        /
        <a href="javascript:;" >课程中心</a>
        /
        <a href="javascript:;" >成绩管理</a>
    </div>
    <div class="score">
        <el-form :inline="true" :model="conditions" class="conditions">
            <el-form-item label="开课学期">
                <el-select v-model="conditions.term" placeholder="开课学期">
                    <el-option v-for="item in terms" :key="item.value" :label="item.term" :value="item.term" />
                    <!-- <template v-for="(v,i) in terms" :key="i">
                        <el-option label=this.v.term value={{v.term}} />
                    </template>
                  <el-option label="2022-2" value="2022-2" />
                  <el-option label="2022-1" value="2022-1" />
                  <el-option label="2021-2" value="2021-2" />
                  <el-option label="2021-1" value="2021-1" /> -->
                </el-select>
              </el-form-item>
            <el-form-item label="课程名称">
              <el-input v-model="conditions.name" placeholder="课程名称" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="query()">查询</el-button>
            </el-form-item>
          </el-form>
        <el-table :data="tableData" height="400" style="width: 100%">
            <el-table-column fixed prop="term" label="开课学期" width="200" />
            <el-table-column prop="name" label="课程名称" width="200" />
            <el-table-column prop="regular" label="平时成绩" width="100" />
            <el-table-column prop="exam" label="考试成绩" width="100" />
            <el-table-column prop="total" label="总成绩" width="100" />
            <el-table-column prop="rank" label="班内排名" width="200" />
            <el-table-column prop="top" label="最高分" width="100" />
            <el-table-column prop="average" label="班级平均分" width="100" />
            <!-- <el-table-column fixed="right" label="Operations" width="200">
                <template #default>
                    <el-button  type="primary"  @click="check()" plain>查看</el-button>
                  </template>
            </el-table-column> -->
          </el-table>
          <div ref="myTotal"></div>
    </div>
</template>

<script>
import SideBar from "@/components/SideBar";
import {showScore,generateElOption,scoreQuery,computeAverageGPA} from "@/service/genServ.js";
export default{
    name:"ScoreMangement",
    data() {
        return {
            terms:{

            },
            conditions:{
                term:'',
                name:'',
            },
            tableData:[
  {
    term:'2022-2',
    name: 'WEB技术',
    regular:'70',
    exam: '80',
    total:'76',
    rank: '48/121',
    top:'99',
    average:'75.4',
  },
]
        }
    },
    mounted() {
        showScore().then(
            (res)=>{
                //console.log(res.data.data);
                this.tableData=res.data.data;
            }
        )
        generateElOption().then(
            (res)=>{
                this.terms=res.data.data;
                // console.log(this.terms);
            }
        )
    },
    methods: {
        gotoIndex: function(){
        this.$router.push({
            path:'index',
        })
        return false;
    },
    // check(){
    //     alert('查看');
    // }
    query(){
        scoreQuery(this.conditions).then(
            (res)=>{
                this.tableData=res.data.data;
            }
        )
        if(this.conditions.term!=""&&this.conditions.name==""){
            computeAverageGPA(this.conditions).then(
                (res)=>{
                    var averageGPA = res.data.data[0];
                    console.log(averageGPA);
                    this.$refs.myTotal.innerHTML='您于'+this.conditions.term+'学期的平均学分绩点为:'+averageGPA.averageGPA;
                }
            )
        }
        
    }
    },
    components:{
        SideBar,
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
.score{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}
.conditions{
    width: 1200px !important;
}
</style>