<template>
    <!-- <div class="sidebtn"><div class="container"><span></span><span></span><span></span></div></div> -->
    
    <!-- 上侧导航栏 -->
    <div class="nav">
        <div class="sidebtn" @click="change" ref="sidebtn"><div class="container"><span></span><span></span><span></span></div></div>
        <span class="user" @click="gotoUser()">
            <img v-if="imageurl" :src="imageurl" alt="">
            <img v-else src="../../../icon/user.png" alt="">
            <!-- <div class="arrowdown"></div> -->
        </span>
    </div>
    
    <div class="sidebar"  ref="sidebar">
        <div class="header">
            <div class="logocontainer">
                <img src="../assets/logo.png" alt="" class="logo">
            </div>
            <div style="color:#fff;width:60%;height:100%;line-height:80px">欢迎使用xx管理系统</div>
        </div>

        
        <el-menu class="menu" router style="flex-direction:column">
            <!-- 首页导航栏 -->
            <el-menu-item index="/indexForAdmin" class="menu-item" id="index">首页</el-menu-item>

                    <!-- 遍历一级菜单 -->
        <template v-for="(v,i) in listA" :key="i">

            <!-- 判断是否有二级菜单 -->
            <template v-if="v.children.length>0">
                
                <!-- 如果有 展示一级和二级菜单 -->
                <el-submenu :index="i.toString()">
                    
                    <!-- 展示一级菜单 -->
                    <template #title>
                        <div class="menu-item">{{v.title}}</div>
                    </template>
                    <!-- 展示二级菜单 -->
                    <template v-for="(v,j) in v.children" :key="j">
                        <el-menu-item class="menu-item-two" :index="'/' + v.name">{{
                            v.title
                          }}</el-menu-item>
                    </template>
                </el-submenu>

            </template>

            <!-- 没有二级直接展示 -->
            <template v-else>
                <el-menu-item class="menu-item" :index="'/' + v.name">
                    <!-- <img src="../../../icon/app.png" alt=""> -->
                    {{
                  v.title
                }}</el-menu-item>
              </template>

        </template>
        </el-menu>


    </div>
</template>

<script>
import {getPersonImage} from '@/service/genServ.js';
export default {
  name: "indexaForAdmin",
  components: {},

  data() {
    return {

      listA: [
        {
            name:"PersonRegistration",
            title:'人员登记',
            children:[
            ]
        },
        {
            name:"TeacherRegistration",
            title:'教师登记',
            children:[]
        },
        {
            name:"ChildrenRegistration",
            title:'学生登记',
            children:[]
        },
        {
            name:"CourseChannel",
            title:'选课管理',
            children:[],
        }
        
        
      
    ],
    imageurl:'',
    };
  },
  mounted() {
    getPersonImage().then(
        (res)=>{
            this.imageurl=res.data.data;
        }
    )

    //找出侧边栏，让他的左值为负的自己的宽度
    var sidebar = document.querySelector('.sidebar');
    var sidebtn = document.querySelector('.sidebtn'); //侧边栏拉出按钮
    var body = document.querySelector('body');


    body.style.backgroundColor='#fff';

    var distance = sidebar.offsetWidth;
    sidebar.style.left = -distance+'px'; //先让侧导航栏跑出去 点击按钮了再跑回来

    //点击按钮让sidebar跑出来
    sidebtn.addEventListener('click',function(){
        body.style.backgroundColor = 'rgb(179, 179, 179)';
        animate(sidebar,0);
    });

    //如果鼠标或者手指点在了sidebar之外 那么就缩回去
    body.addEventListener('click',function(e){
        var x = e.pageX; // 如果横坐标大于了sidebar的宽度
        if(x>distance){
            body.style.backgroundColor='#fff';
            animate(sidebar,-Math.ceil(distance));
        }
    });


//移速衰减滑动动画
function animate(obj, target, callback) {
        // console.log(callback);  callback = function() {}  调用的时候 callback()
    
        // 先清除以前的定时器，只保留当前的一个定时器执行
        clearInterval(obj.timer);
        obj.timer = setInterval(function() {
            // 步长值写到定时器的里面
            // 把我们步长值改为整数 不要出现小数的问题
            // var step = Math.ceil((target - obj.offsetLeft) / 10);
            var step = (target - obj.offsetLeft) / 10;
            step = step > 0 ? Math.ceil(step) : Math.floor(step);
            if (obj.offsetLeft == target) {
                // 停止动画 本质是停止定时器
                clearInterval(obj.timer);
                // 回调函数写到定时器结束里面
                // if (callback) {
                //     // 调用函数
                //     callback();
                // }
                callback && callback();
            }
            // 把每次加1 这个步长值改为一个慢慢变小的值  步长公式：(目标值 - 现在的位置) / 10
            obj.style.left = obj.offsetLeft + step + 'px';
    
        }, 10);
    }


  },
  methods: {
    gotoUser(){
        this.$router.push({
            path:"AdminInfo",
        })
    }
  },
};
</script>

<style scoped>
.nav{
    display: flex;
    position: relative;
    height: 50px;
    width: 100%;
    box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
}
.user{
    display: flex;
    position: absolute;
    right: 10px;
    height: 40px;
    margin-top: 5px;
    cursor: pointer;
}

.user img{
    border-radius: 6px;
    height: 100%;
    width: auto;
    margin-right: 5px;
}
.sidebtn{
    position: absolute;
    width: 50px;
    height: 50px;
    background-color: #fff;
    cursor: pointer;
}
.sidebtn:hover{
    background-color: #ccc;
}
.container{
    position: absolute;
    width: 25px;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
}
.sidebtn .container span{
    display: block;
    width: 25px;
    height: 8px;
    border-top: 1px solid rgb(65, 158, 255);
}
.sidebar{
    position: fixed;
    left: -20%;
    width: 20%;
    height: 100%;
    background-color: rgb(48, 65, 86);
    transform: translateY(-50px);
    z-index: 999;
}
.header{
    display:flex;
    width: 100%;
    height: 80px;
}
.logocontainer{
    display: flex;
    width: 20%;
    height: 65%;
    margin-bottom: 10px;
    margin-right: 10px;
    margin-left: 10px;
    padding-top: 10px;
}
.logo{
    width: 100%;
    height: 100%;
    left: 10px;
    top: 10px;
}
.menu{
    display: flex;
    justify-content: center;
    background-color: rgb(48, 65, 86);
}
.menu-item{
    width: 100%;
    background-color: rgb(48, 65, 86);
    color: rgb(191, 203, 217);
}

.menu-item::before{
    background: url(../../../icon/app.png);
}

.menu-item:hover{
    background-color: rgb(31, 45, 61);
}

.menu-item-two{
    width: 100%;
    background-color: rgb(31, 45, 61);
    color: rgb(191, 203, 217);
}

.menu-item-two:hover{
    background-color: rgb(0, 21, 40);
}

.el-submenu:hover{
    background-color: rgb(31, 45, 61) !important;
}
</style>