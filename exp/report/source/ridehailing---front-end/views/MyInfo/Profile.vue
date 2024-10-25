<template>
    <SideBar/>
    <div class="position">
        <a href="javascript:;" @click="gotoIndex">首页</a>
        /
        <a href="javascript:;" >我的主页</a>
        /
        <a href="javascript:;" >个人简介</a>
    </div>
 
    <!-- 个人名片 基本信息 -->
    <div class="card">
        <div class="title">
            个人基本信息
        </div>
        <el-divider/>
        
        <div class="avatarcontainer">
            <el-upload
            class="avatar-uploader"
            :headers="authHeader"
            :data="{id:person.id}"
            action="/api/info/uploadAvatar"
            :show-file-list="true"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </div>
        <div class="title">
            <el-divider/>
        </div>
        <div class="substance">
            <div class="imgcontainer">
                <img src="../../../../icon/employee.png" alt="">
                姓名: {{person.perName}} 
            </div>
            
            <el-divider/>
        </div>
        <div class="substance">
            <div class="imgcontainer">
                <img src="../../../../icon/info.png" alt="">
                学工号: {{person.perNum}}
            </div>
            <el-divider/>
        </div>

        <div class="substance">
            <div class="imgcontainer">
                <img src="../../../../icon/edit.png" alt="">
                专业: {{person.major}}
            </div>
            <el-divider/>
        </div>

        <div class="substance" style="margin-bottom:20px">
            <div class="imgcontainer">
                <img src="../../../../icon/group.png" alt="">
                用户类型: {{person.userType}}
            </div>
        </div>
    </div>

    <!-- 可修改信息 -->
    <div class="editable">
        <div class="title">
            修改信息
        </div>
        <el-divider/>
        <div class="substance">
            <div class="inputcontainer">
                <img src="../../../../icon/user.png" alt="">
                <div style="width:60px;">用户名:</div> <el-input style="width:75%;margin-left:20px;" v-model="person.username"></el-input>
            </div>
            <el-divider/>
        </div>
        <div class="substance">
            <div class="inputcontainer">
                <img src="../../../../icon/lock.png" alt="">
                <div style="width:60px;">密码:</div> <el-input type="password" style="width:75%;margin-left:20px;" v-model="person.password"></el-input>
            </div>
            <el-divider/>
        </div>
        <div class="substance">
            <div class="inputcontainer">
                <img src="../../../../icon/youxiang.png" alt="">
                <div style="width:60px;">e-mail:</div> <el-input style="width:75%;margin-left:20px;" v-model="person.email"></el-input>
            </div>
            <el-divider/>
        </div>
        <div class="substance">
            <div class="inputcontainer">
                <img src="../../../../icon/shouji.png" alt="">
                <div style="width:60px;">电话号码:</div> <el-input style="width:75%;margin-left:20px;" v-model="person.phone"></el-input>
            </div>
            <el-divider/>
        </div>
        <div class="substance">
            <div class="inputcontainer">
                <img src="../../../../icon/xingbie.png" alt="">
                <div style="width:60px;">性别:</div>
                <el-radio-group v-model="person.sex" >
                    <el-radio label="男" size="large">男</el-radio>
                    <el-radio label="女" size="large">女</el-radio>
                    <el-radio label="我只是一只狗狗" size="large" disabled>我只是一只狗狗</el-radio>
                  </el-radio-group>
            </div>
            <el-divider/>
            <div class="substance">
                <div class="inputcontainer">
                    <img src="../../../../icon/chushengriqi.png" alt="">
                    <div style="width:60px;">出生年月:</div> 
                    <el-input style="width:10%;margin-left:20px;margin-right:20px" v-model="year"></el-input>
                    _
                    <el-input style="width:10%;margin-left:20px;margin-right:20px" v-model="month"></el-input>
                    _
                    <el-input style="width:10%;margin-left:20px;margin-right:20px" v-model="day"></el-input>
                    <span style="font-weight:300;color:#97A8BE;font-size:10px;vertical-align:middle">(请以YYYY-MM-DD格式填写)</span>
                </div>
                <el-divider/>
            </div>
            <div class="substance">
                <div class="inputcontainer">
                    <img src="../../../../icon/nianling.png" alt="">
                    <div style="width:60px;">年龄:</div> <el-input style="width:25%;margin-left:20px;" v-model="person.age"></el-input>
                </div>
                <el-divider/>
            </div>
            <el-button type="primary" style="margin-bottom:20px;" @click="submit()">提交</el-button>
        </div>
    </div>
</template>

<script>
import SideBar from '@/components/SideBar';
import {getAuthHeader,showPerson,getPersonImage,infoEdit,changeToken} from '@/service/genServ.js'
// import {store} from '@/store/createStore.js'
export default{
    name:'Profile',
    data() {
        return {
            person:{
                id:'',
                email:'3054117680@qq.com',
                phone:'17349742869',
                perName:'lyh',
                perNum:'202100300063',
                major:'软件工程',
                birthday:'2002-12-09',
                sex:'男',
                age:'11',
                userType:'学生',
                username:'ohyeah',
                password:'',
            },
            year:'',
            month:'',
            day:'',
            authHeader:getAuthHeader(),
            imageUrl:require("../../assets/blogger.jpg"),
        }
    },
    methods: {
    submit(){
        // store.state.username=this.person.username;
        // console.log(store.state.username);
        // this.$store.commit('changeUserName',this.person.username);
        // var uName = this.$store.state.username;
        console.log(this.$store);
        // var uPasswd = this.$store.state.password;
        var uPasswd = this.person.password;
        
        // this.$store.commit('login',this.$store.state,{uName,uPasswd});

        console.log(this.$store.state);
        // console.log(this.person.password);
        //年龄字符串转数字
        this.person.age = parseInt(this.person.age);
        //先把生日拼起来
        this.person.birthday=this.year+'-'+this.month+'-'+this.day;
        //console.log(this.person.birthday);
        infoEdit(this.person).then(
            (res)=>{
                console.log(res.data);
                if(res.data.msg=='success'){
                    this.$message({
                        message: '修改成功!',
                        type: 'success',
                    })            
                }
                else if(res.data.msg==='邮件地址不合法'){
                    this.$message({
                        message: '邮件地址不合法',
                        type: 'danger',
                    })    
                }
                else if(res.data.msg==='密码长度不合法，需要是6到12位'){
                    this.$message({
                        message: '密码长度不合法，需要是6到12位',
                        type: 'danger',
                    })    
                }
                changeToken({username:this.person.username,password:uPasswd}).then(
            (res)=>{
                console.log("changeToken");
                console.log(res);
                var username = res.username;
                var jwtToken = res.accessToken;
                var password = uPasswd;
                this.$store.commit('login',{username,jwtToken,password});
            }
        )
        console.log(this.$store);
            }
        )

    },
    gotoIndex(){
      this.$routers.push({
        path:"index",
      })
    },
    handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
    }
    
    },
    mounted() {
        // console.log(this.$store.state);
        showPerson().then(
            (res)=>{
                // console.log(res.data.data);
                this.person=res.data.data[0];
                var arr=this.person.birthday.split('-');
                this.year=arr[0];
                this.month=arr[1];
                this.day=arr[2];
                this.person.password=this.$store.state.password;
            }
        )
        getPersonImage().then(
            (res)=>{
                //console.log(res.data.data);
                this.imageUrl=res.data.data;
            }
        )
        

    },
    components:{
        SideBar,
    }
}
</script>

<style scoped>
div{
    box-sizing: border-box;
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
.card{
    position: absolute;
    left: 15%;
    margin-top: 30px;
    border:1px solid #97A8BE;
    border-radius: 5px;
    width: 20%;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}

.title{
    text-align: left;
    margin-top: 15px;
    margin-left: 15px;
    margin-right: 15px;
    font-size: 18px;
    font-weight: 500;
}

.substance{
    text-align: left;
    margin-top: 10px;
    margin-left: 10px;
    margin-right: 10px;
    font-size: 14px;
    font-weight: 300;
}

.imgcontainer{
    display: flex;
}

.imgcontainer img{
    width: 25px;
    height: 25px;
    margin-right: 10px;
}

.avatarcontainer{
    margin-bottom: 20px;
    position: relative;
    height: 150px;
    width:150px;
    border-radius: 75px;
    left: 50%;
    transform: translateX(-50%);
    border: 1px solid #97A8BE;
    overflow: hidden;
}
.avatarcontainer:hover{
    border:5px solid skyblue
}

.avatarcontainer img{
    width: 100%;
    height: 100%;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  .editable{
    position: absolute;
    width: 50%;
    left: 38%;
    margin-top: 30px;
    border:1px solid #97A8BE;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  }

  .inputcontainer{
    display: flex;
  }
  .inputcontainer img{
    width: 25px;
    height: 25px;
    margin-right: 10px;
  }
</style>