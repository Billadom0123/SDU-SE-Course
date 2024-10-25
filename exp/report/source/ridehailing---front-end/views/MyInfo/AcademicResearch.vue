<template>
    <SideBarForTeacher/>
    <div class="position">
        <a href="javascript:;" >首页</a>
        /
        <a href="javascript:;" >学术研究</a>
    </div>
    <!-- 主要内容区域 -->
<div class="main" ref="main">


    <!-- 博主模块 -->
    <div class="blogger" ref="blogger">

        <!-- 头像位置 -->
        <div class="avatar">
            <img v-if="imageUrl" :src="imageUrl" alt="" class="avatarimg">
            <img v-else src="../../assets/user.png" alt="" class="avatarimg">
        </div>

        <!-- 用户信息 -->
        <div class="userinfo">
        <!-- 用户名 -->
        <div class="username">
            {{teacherName}}
        </div>
        <!-- 博客信息 -->
        <div class="info">
            
        </div>
        </div>

        <!-- 个人简介 -->
        <div class="introduce" ref="introduce">
            <!-- 个人简介: -->
            <br>
            <br>
            <!-- {{introduce}} -->
        </div>

    </div>

    <!-- 博客文章部分 -->
    <div class="blogs" ref="blogs">
        <br>
        <img src="../../../../icon/research.png" alt="" style="width:50px">
        研究方向
        <span style="float:right;"><el-button type="primary" icon="el-icon-edit" circle @click="showResearchInput()"></el-button></span>
        <br>
        <el-input style="width:80%;font-size:22px;margin-top:20px;" v-if="researchInput" placeholder="输入您的研究方向,回车提交" v-model="newResearch" @keyup.enter="submitResearch()"></el-input>
        <template v-for="(r,i) in researches" :key="i">
            <el-divider style="width:80%"></el-divider>
            <div class="blog"  ref="blog">
                <span>{{r.research}}</span>
                <span style="float:right;"><el-button type="danger" icon="el-icon-delete" circle @click="submitDelete(r.research)"></el-button></span>
            </div>
        </template>
        <br>
        <br>
        <br>
        <br>
        <img src="../../../../icon/paper.png" alt="" style="width:50px">
        论文著作
        <span style="float:right;"><el-button type="primary" icon="el-icon-edit" circle @click="showPaperInput()"></el-button></span>
        <br>
        <el-input style="width:80%;font-size:22px;margin-top:20px;" v-if="paperInput" placeholder="输入您的论文著作,回车提交" v-model="newPaper" @keyup.enter="submitPaper()"></el-input>
        <template v-for="(p,i) in papers" :key="i">
            <el-divider style="width:80%"></el-divider>
            <div class="blog"  ref="blog">
                <span>{{p.paper}}</span>
                <span style="float:right;"><el-button type="danger" icon="el-icon-delete" circle @click="submitDeletePaper(p.paper)"></el-button></span>
            </div>
        </template>
    </div>
</div>
</template>

<script>
import SideBarForTeacher from '../../components/SideBarForTeacher.vue';
import { getPersonImage,getTeacher,getResearch,getPaper,addResearch,deleteResearch,addPaper,deletePaper } from '../../service/genServ.js';
export default {
  name: "AcademicResearch",
  components:{
    SideBarForTeacher,
  },
  data() {
        return {
            teacherName:'wangzheng',
            papers:["《Advanced Security and Privacy for RFID Technologies》","《计算机网络安全的理论与实践（第3版）》","《信息安全技术 密码应用标识规范》"],
            researches:[
                {
                    research:'',
                }
            ],
            imageUrl:'',
            newResearch:'',
            newPaper:'',
            researchInput:false,
            paperInput:false,
        };
    },
    methods: {
        showResearchInput(){
            this.researchInput=!this.researchInput;
        },
        showPaperInput(){
            this.paperInput=!this.paperInput;
        },
        submitResearch(){
            addResearch({"newResearch":this.newResearch}).then(
                (res)=>{
                    if(res.data.msg==='success'){
                        this.$message({
                        message: '提交成功!',
                        type: 'success',
                        })
                        location.reload();
                    }
                }
            )
            this.researchInput=false;
            this.newResearch='';
        },
        submitDelete(research){
            console.log(research);
            this.$confirm('确定要删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            deleteResearch({"deleteResearch":research}).then(
                (res)=>{
                    if(res.data.msg==='success'){
                        this.$message({
            type: 'success',
            message: '删除成功!'
          });
          location.reload();
                    }
                }
            )
          
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
        },
        submitPaper(){
            addPaper({"newPaper":this.newPaper}).then(
                (res)=>{
                    if(res.data.msg==='success'){
                        this.$message({
                        message: '提交成功!',
                        type: 'success',
                        })
                        location.reload();
                    }
                }
            )
            this.paperInput=false;
            this.newPaper='';
        },
        submitDeletePaper(research){
            this.$confirm('确定要删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            deletePaper({"deletePaper":research}).then(
                (res)=>{
                    if(res.data.msg==='success'){
                        this.$message({
            type: 'success',
            message: '删除成功!'
          });
          location.reload();
                    }
                }
            )
          
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
        },

    },
    mounted() {
        getPersonImage().then(
            (res)=>{
                //console.log(res.data.data);
                this.imageUrl=res.data.data;
            }
        )
        getTeacher().then(
            (res)=>{
                // console.log(res.data.data); 
                this.teacherName=res.data.data.teacherName;
            }
        )
        getResearch().then(
            (res)=>{
                // console.log(res.data.data);
                this.researches=res.data.data;
                // console.log(this.researches);
            }
        )
        getPaper().then((res)=>{
                this.papers=res.data.data;
            })
            
        
        //让blogger的高度自适应
        var blogger=this.$refs.blogger;
        var introduce = this.$refs.introduce;
        if(blogger.offsetHeight<=introduce.offsetHeight){
            blogger.style.height=introduce.offsetHeight+50+'px';
        }
        //让blogs的位置自适应
        var blogs=this.$refs.blogs;
        var distance = blogger.offsetHeight+170+'px';
        blogs.style.top=distance;
        //让main的高度自适应
        // var main = this.$refs.main;
        // main.style.height=document.body.scrollHeight+document.body.scrollTop+30+'px';
    },
}
</script>

<style scope>


div{
    box-sizing: border-box;
}
a{
    text-decoration: none;
    color: black;
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

.main{
    width: 100%;
    height: 100%;
}

.blogger{
    position: absolute;
    width: 90%;
    left: 50%;
    transform: translate(-50%,100px);
    background-color: white;
    border:1px solid #97A8BE;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}
.blogtop{
    position:absolute;
    display: block;
    width: 135%;
    left: 50%;
    transform: translateX(-50%);
}
.avatar{
    float: left;
    width: 100px;
    height: 100px;
    border-radius: 50px;
    background-color: pink;
    margin-left: 30px;
    transform: translateY(-10px);
}
.avatarimg{
    width: 100px;
    height: 100px;
    border-radius: 50px;
}
.userinfo{
    float: left;
    display: flex;
    flex-direction: column;
    margin-bottom: 50px;
}
.username{
    margin-left: 10px;
    margin-top: 10px;
    float: left;
    height: 50px;
    line-height: 50px;
    font-weight: 700;
    font-size:22px;
    text-align: left;
}
.info{
    float: left;
    margin-left: 10px;
    line-height: 50px;
    font-size:18px;
    height: 50px;
}
.introduce{
    margin-top: 30px;
    position: absolute;
    right:200px;
    color:#555666;
    width: 300px;
    text-align: left !important;
    margin-bottom: 20px;
}
.blogs{
    position: absolute;
    width: 90%;
    left: 50%;
    transform: translateX(-50%);
    background-color: white;
    color: black;
    font-size: 20px;
    font-weight: 400;
    text-align: left;
    padding-left: 20px;
    padding-right: 20px;
    padding-bottom: 30px;
    border-radius: 5px;
    margin-bottom:15px;
}
.blog{
    position:relative;
    font-size:14px;
    color:#555666;
}
.blog span{
    font-size:20px;
}
.blog:hover{
    cursor: pointer;
    color: rgb(252, 82, 49);
}

.blog span:hover{
    cursor: pointer;
    color: rgb(252, 82, 49);
}
</style>