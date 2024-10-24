<template>
  <div class="wrapper">
    <header>
      <a href="#" class="logo">ETS</a>
      <ul>
        <!-- <li><a href="#" class="active">Home</a></li> -->
        <!-- <li><a href="#">About</a></li>
        <li><a href="#">Work</a></li>
        <li><a href="#">Contact</a></li> -->
      </ul>
    </header>
    <section>
      <img src="../assets/stars.png" id="stars">
      <img src="../assets/moon.png" id="moon">
      <img src="../assets/mountains_behind.png" id="mountains_behind">
      <h2 id="text">TaskScheduler</h2>
      <a href="#" id="btn" @click="showLoginBox">ETS Let's Go!</a>
      <img src="../assets/mountains_front.png" id="mountains_front">
    </section>
    <div class="sec" id="sec">
      <h2>Equivalence-based Task Scheduler</h2>
      <h3>基于等价类实现的任务调度助手</h3>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;
        假想一下,现在的你接到了一堆任务,它们中的每一个都有一个最早开始的时间和最晚结束的时间,同时每个任务也有执行时间和优先级。
        你想把精力放在高效完成任务上,而不是纠结到底应该怎样安排任务的执行时段。山东大学软件学院准确把握住了这一痛点,快速开发了一款
        可以帮助你实现任务调度的网站,你只要输入你的任务队列的信息,网站就会给出对应的调度方案!!
      </p>
    </div>

    <!-- 点击ETS Let's Go! 出现登录框 -->
    <div class="Login" v-if="loginBoxVisible">
      <div class="close" @click="closeLoginBox">×</div>
      <div class="box">
          <p class="table">Login</p>
          <br>
          <input type="text" placeholder="账号" v-model="username">
          <input type="password" placeholder="密码" v-model="password">
          <br>
          <a href="#" class="register" @click="showRegister">No account?Register!</a>
          <div class="go" @click="login">GO</div>
      </div>
  </div>

  <!-- 点击No account?Register!出现注册框,关闭登录框 -->
  <div class="Register" v-if="registerBoxVisible">
    <div class="close" @click="closeLoginBox">×</div>
    <div class="box">
        <p class="table">Register</p>
        <br>
        <input type="text" placeholder="账号" v-model="username">
        <input type="password" placeholder="密码" v-model="password">
        <br>
        <div class="go" @click="register">Register</div>
    </div>
</div>
  </div>

</template>

<script>
import {registerReq} from '../service/userService'
export default {
  name:"login",
  data() {
    return {
      loginBoxVisible:false,
      registerBoxVisible:false,
      username:"",
      password:""
    }
  },
  mounted() {
    let stars = document.getElementById('stars')
    let moon = document.getElementById('moon')
    let mountains_behind = document.getElementById('mountains_behind')
    let text = document.getElementById('text')
    let btn = document.getElementById('btn')
    let mountains_front = document.getElementById('mountains_front')
    let header = document.querySelector('header')

    window.addEventListener('scroll', function(){
      let value = window.scrollY;
      stars.style.left = value * 0.25 + 'px'
      moon.style.top = value * 1.05 + 'px'
      mountains_behind.style.top = value * 0.5 + 'px'
      mountains_front.style.top = value * 0 + 'px'
      text.style.marginRight = value * 4 + 'px'
      text.style.marginTop = value * 1.5 + 'px'
      btn.style.marginTop = value * 1.5 + 'px'
      header.style.top = value * 0.5 + 'px'
    })
  },
  methods:{
    showLoginBox(){
      this.loginBoxVisible=true
    },
    closeLoginBox(){
      this.loginBoxVisible=false
      this.registerBoxVisible=false
    },
    showRegister(){
      this.loginBoxVisible=false
      this.registerBoxVisible=true
    },
    login(){
      if(this.username&&this.password){
        this.$store.dispatch("loginCommit",{username:this.username,password:this.password})
        .catch(()=>{
          this.$message({
              message: " 用户名或密码错误",
              type: "error",
            });
        })
      }
    },
    register(){
      registerReq(this.username,this.password)
    }
  }
}
</script>

<style scoped>
/* 这是引入了一些字体 */
@import url('https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap'); 

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
  /* 当用户手动导航或者 CSSOM scrolling API 触发滚动操作时 */
  scroll-behavior: smooth; 
}

.wrapper{
  min-height: 100vh;
  overflow-x: hidden;
  background: linear-gradient(#2b1055,#7597de);
}



header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  padding: 30px 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 10000;
}

header .logo {
  color: #fff;
  font-weight: 700;
  text-decoration: none;
  font-size: 2em;
  text-transform: uppercase; /* 大写 */
  letter-spacing: 2px; /* 字符间距 */
}

header ul {
  display: flex;
  justify-content: center;
  align-items: center;
}

header ul li {
  list-style: none;
  margin-left: 20px;
}

header ul li a {
  text-decoration: none;
  padding: 6px 15px;
  color: #fff;
  border-radius: 20px;
}

header ul li a:hover,
header ul li a.active {
  background: #fff;
  color: #2b1055;
}

section {
  position: relative;
  width: 100%;
  height: 100vh;
  padding: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

section::before {
  content: '';
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 100px;
  background: linear-gradient(to top, #1c0522, transparent);
  z-index: 1000;
}

section img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* object-fit CSS 属性指定可替换元素（例如：<img> 或 <video>）的内容应该如何适应到其使用高度和宽度确定的框。 */
  object-fit: cover;
  pointer-events: none;
}

section img#moon {
  /* mix-blend-mode CSS 属性描述了元素的内容应该与元素的直系父元素的内容和元素的背景如何混合。 */ 
  mix-blend-mode: screen;
}

section img#mountains_front {
  z-index: 10;
}

#text {
  position: absolute;
  right: -350px;
  color: #fff;
  white-space: nowrap;
  font-size: 7.25vw;
  z-index: 9;
}

#btn {
  text-decoration: none;
  display: inline-block;
  padding: 8px 30px;
  border-radius: 40px;
  background: #fff;
  color: #2b1055;
  font-size: 1.5em;
  z-index: 9;
  transform: translateY(100px);
}

#btn:hover{
  background-color:#DCDFE6;
}

.sec {
  position: relative;
  padding: 100px;
  background: #1c0522;
}

.sec h2 {
  font-size: 3.5em;
  margin-bottom: 10px;
  color: #fff;
}

.sec h3 {
  font-size: 2.5em;
  margin-bottom: 10px;
  color: #fff;
}

.sec p {
  font-size: 1em;
  color: #fff;
  text-align:left
}

.Login{
  z-index:999;
  position: absolute;
  width: 550px;
  height: 400px;
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
  left:50%;
  top:50%;
  transform: translate(-50%,-50%);
}
.Register{
  z-index:999;
  position: absolute;
  width: 550px;
  height: 400px;
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
  left:50%;
  top:50%;
  transform: translate(-50%,-50%);
}
.close{
  position: absolute;
  top: 20px;
  right:20px;
  font-size:40px
}

.close:hover{
  cursor: pointer;
}
.table{
  font: 900 40px '';
  text-align: center;
  letter-spacing: 5px;
  color: #3d3d3d;
}
.box{
  overflow: hidden;
}

.box input{
  width: 100%;
  margin-bottom: 20px;
  outline: none;
  border: 0;
  padding: 10px;
  border-bottom: 3px solid rgb(150, 150, 240);
  background-color: transparent;
  font: 900 16px '';
}
.go{
  text-decoration: none;
  text-align: center;
  display: block;
  height: 48px;
  padding: 12px;
  font: 900 20px '';
  border-radius: 10px;
  margin-top: 20px;
  color: #fff;
  letter-spacing: 3px;
  background-image: linear-gradient(to left, #fd79a8, #a29bfe);
}
.go:hover{
  cursor:pointer
}
.register{
  text-decoration: none;
}
</style>