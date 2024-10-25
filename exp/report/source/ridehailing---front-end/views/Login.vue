<template>
  <!-- <div class="login">
    <h2>Login</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">用户名</label>
        <input
          type="text"
          v-model="username"
          name="username"
          class="form-control"
          :class="{ 'is-invalid': !username }"
        />
        <div v-show="!username" class="invalid-feedback">请输入用户名</div>
      </div>
      <div class="form-group">
        <label htmlFor="password">口令</label>
        <input
          type="password"
          v-model="password"
          name="password"
          class="form-control"
          :class="{ 'is-invalid': !password }"
        />
        <div v-show="!password" class="invalid-feedback">请输入口令</div>
      </div>
      <div class="form-group">
        <button class="btn btn-primary">登录</button>
      </div>
    </form>
  </div> -->
  <div class="login-container">
    <el-form class="login-form" autocomplete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">Welcome</h3>
      </div>

      <el-form-item>
        <span class="svg-container">
          <i class="el-icon-user-solid"></i>
        </span>
        <el-input
          ref="username"
          v-model="username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
        />
      </el-form-item>

      <el-tooltip>
        <el-form-item>
          <span class="svg-container">
            <i class="el-icon-key"></i>
          </span>
          <el-input
            v-model="password"
            type="password"
            placeholder="Password"
            name="password"
          />
          <span class="show-pwd">
            <svg-icon icon-class="password" />
          </span>
        </el-form-item>
      </el-tooltip>
      <div style="margin-bottom:20px;"><div class="forget" @click="dialogVisible=true"><i class="el-icon-key"></i>忘记密码？</div></div>
      <el-dialog
      v-model="dialogVisible"
      title="重置密码"
      width="30%"
      :before-close="handleClose"
    >
      用户名:<input type="text" v-model="this.uname">
      <br>
      验证码:<input type="text" v-model="this.checkCodeString">
      <div style="margin-top:10px;display:flex;flex-direction:column;">点击图片刷新<img  class="checkCodeImg" src="/api/auth/generateCheckCode" alt="" style="width:100%;height:100%;" @click="changeCheckCode"></div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="send">
            重置
          </el-button>
        </span>
      </template>
    </el-dialog>
      <el-button
        type="primary"
        style="width: 100%; margin-bottom: 30px"
        @click="handleSubmit"
        >登录捏</el-button
      >
      
    </el-form>
  </div>
</template>

<script>
import {sendEmail} from "@/service/userServ.js";
import {generateDirectories} from "@/service/genServ.js";
export default {
  name: "Login",
  data() {
    return {
      username: "",
      password: "",
      dialogVisible:false,
      uname:'',
      checkCodeString:'',
    };
  },
  beforeMount() {
    this.$store.commit("logout");
  },
  mounted() {
    // console.log(document.cookie);
    generateDirectories().then(
    )
  },
  methods: {
    send(){
        sendEmail(this.uname,this.checkCodeString).then(
          (res)=>{
            if(res.data.msg==null){
              this.$message({
              message: "重置密码成功",
              type: "success",
            });
            this.dialogVisible=false;
            this.uname='';this.checkCodeString='';
            }
            else if(res.data.msg=="验证码错误"){
              this.$message({
              message: "验证码错误",
              type: "danger",
            });
            }
            else if(res.data.msg=="该账号不存在"){
              this.$message({
              message: "该账号不存在",
              type: "danger",
            });
            }
            else{
              this.$message({
              message: "该账号没有绑定邮箱!",
              type: "danger",
            });
            }
          }
        )
    },
    changeCheckCode(){
      console.log(document.cookie);
      // var list = session.getAttribute("CheckCode");
      // console.log(list);
      var checkCode=document.querySelector('.checkCodeImg');
      // console.log(checkCode);
      checkCode.src='/api/auth/generateCheckCode?'+new Date().getMilliseconds();
      // console.log(this.uname+' '+this.checkCodeString);
    },
    handleSubmit() {
      const { username, password } = this;
      if (username && password) {
        this.$store
          .dispatch("login", {
            username: username,
            password: password,
          })
          .catch(() => {
            this.$message({
              message: " 用户名或密码错误",
              type: "warnning",
            });
          });
      }
    },
  },
};

</script>


<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  background: url(../assets/balloon3.jpg) no-repeat;
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100% !important;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .forget:hover{
    cursor: pointer;
    color:#F56C6C;
  }

  input{
    margin-top: 10px;
    border:none;
    outline: none;
    border-bottom:1px solid #97A8BE;
    font-size:22px;
    color:grey;
    font-weight: 100;
    font-style:italic;
  }

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>
