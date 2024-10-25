
<template>
    <SideBar/>
    <div class="position">
      <a href="javascript:;" @click="gotoInde()">首页</a>
  </div>
  <div class="trial">
    <el-button @click="sayHi"></el-button>
    <div>{{stu.studentNum}}</div>
    <div>{{stu.studentName}}</div>
    <div>{{stu.name}}</div>
    <div>{{stu.sex}}</div>
    <div>{{stu.rel}}</div>
  
    <div>
      用户名：<input type="text" v-model="user.username">
      密码：<input type="text" v-model="user.password">
      用户类型: <input type="text" v-model="user.role">
    </div>
    <button @click="submit">提交</button>
  <div>
    <input type="text" v-model="email.username">
  </div>
    <button @click="send">发邮件</button>
    <div><button @click="getCloud">生成词云</button></div>
    <img :src="this.WordCloudUrl" alt="">
  </div>
  <div class="wordCloud">
    <div>我的词云</div>
    <img :src="this.WordCloudUrl" alt="">
  </div>
  <div class="statistic">
    <div class="tab"> 
      <el-progress type="circle" :percentage="this.rankPer" class="progress" ></el-progress>
      <div class="intro">平均学分绩点淦碎了
        <span class="important">{{this.rankPer}}%</span>
        的人,杀疯了!
      </div>
    </div>
    <div class="tab" style="flex-wrap:nowrap;"> 
      <el-progress type="circle" :percentage="this.bestPer" class="progress" ></el-progress>
      <div class="intro">看来你最擅长 
        <span class="important">{{this.statistic.best}}</span> 
        这门课喔! 击败了
        <span class="important">{{this.bestPer}}%</span>
        的人！
      </div>
    </div>
    <div class="tab"> 
      获奖<span class="important">&nbsp;校级: {{this.statistic.school}}项 &nbsp; &nbsp; 市级: {{this.statistic.city}}项 </span>
      <span class="important">&nbsp; &nbsp;省级:{{this.statistic.province}}项 &nbsp;&nbsp; 国家级:{{this.statistic.country}}项</span>
      <div class="important">&nbsp; &nbsp;&nbsp; &nbsp;世界级:{{this.statistic.world}}项&nbsp; &nbsp;银河系级:{{this.statistic.silverRiver}}项</div>
    </div>
    <div class="tab"> 
      收获
      <div class="important">{{this.statistic.receive}}次互评</div> 
       &nbsp;&nbsp;
      给出
      <div class="important">{{this.statistic.deliver}}次互评</div>
      &nbsp;&nbsp;你就是社交达人!
    </div>
  </div>
</template>


<script>
import SideBar from "@/components/SideBar";
import {sayHello} from "@/service/genServ.js";
import {registerUser,sendEmail} from "@/service/userServ.js";
import {generateWordCloud,generateStatistics} from "@/service/genServ.js";
// import {store} from '@/store/createStore.js'
export default {
  name: "index",
  data() {
    return {
      WordCloudUrl:"",
      rankPer:0.0,
      bestPer:0.0,
      statistic:{
        rank:1,
      total:1,
      best:"",
      minRank:1,
      minTotal:1,
      school:1,
      city:1,
      province:1,
      country:1,
      world:1,
      silverRiver:1,
      deliver:1,
      receive:1,
      },
      
      user:{
        username:'',
        password:'',
        role:['student']
      },
      temp:{
        name:'tempName',
        ID:'tempId'
      },
      stu:{
        id:'',
        studentNum:'',
        studentName:'',
        name:'',
        sex:'',
        rel:''
      },
      email:{
        username:'',
      }
    }
  },
  components: {
    SideBar,
  },
  created() {},
  methods: {
    getCloud(){
      console.log(1);
      generateWordCloud().then(
        (res)=>{
          console.log(res.data);
          this.WordCloudUrl=res.data.data;
        }
      )
    },
    send(){
      sendEmail(this.email.username).then(
        (res)=>{
          console.log(res);
        }
      )
    },
    submit(){
      registerUser(this.user).then(
        (res)=>{
          console.log(res);
        }
      )
    },
    sayHi(){
      console.log(this.temp);
      sayHello(this.temp).then(
        (res)=>{
          alert(res);
          console.log(res);
          console.log(res.data.data[0]);
          console.log(res.data.data[1]);
          this.stu=res.data.data[0];
        }
      )
    },
    gotoIndex(){
      this.$routers.push({
        path:"index",
      })
    }
  },
  mounted(){
    // console.log(this.$store.state);
    generateWordCloud().then(
      (res)=>{
        this.WordCloudUrl=res.data.data;
      }
    )
    generateStatistics().then(
      (res)=>{
        console.log(res.data.data);
        // console.log(res.data.data.rank);
        this.statistic=res.data.data;
        // console.log(this.statistic.rank);
        // console.log(this.statistic.total);
        this.rankPer=(100-this.statistic.rank/this.statistic.total*100).toFixed(1);
        // console.log(this.rankPer);
        this.bestPer=(100-this.statistic.minRank/this.statistic.minTotal*100).toFixed(1);
      }
    )
  }
};

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

.trial{
  display: none;
}

.wordCloud{
  position:relative;
  width: 500px;
  height: 500px;
  left: 10%;
  top: 40%;
  transform: translateY(-50%);

}

.wordCloud div{
  font-size:22px;
  margin-bottom:20px;
  color:#97A8BE;
}

.statistic{
  position: absolute;
  left:50%;
  top: 50%;
  transform:translateY(-50%);
  width:45%;
  height: 75%;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  border:1px solid #97A8BE;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
}

.tab{
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  position: relative;
  margin:32px 20px 20px 20px;
}

.intro{
  margin-left: 20px;;
}

.progress{
  margin-right: 20px;
}

.important{
  font-size:22px;margin-left:5px;margin-right:5px;color:#F56C6C;
}



</style>