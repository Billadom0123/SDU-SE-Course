<template>
    <div class="whole">
        <div class="header">
            <div class="wrapper">
                <div class="imgcontainer">
                    <img :src="avatarurl" alt="">
                </div>
                <div class="info">
                    <div class="username">
                        {{this.LyhPerson.username}}
                    </div>
                    <div class="introduce">
                        <input type="text" v-model="this.LyhPerson.introduce" placeholder="编辑个性签名">
                    </div>
                </div>
            </div>
        </div>
        <div class="navi">
            <div class="index" @click="gotoIndex">
                <img src="../../../icon/user2.png" alt="">
                主页
            </div>
            <div class="photo" @click="gotoPhoto">
                <img src="../../../icon/image.png" alt="">
                照片墙
            </div>
            <div class="memory" @click="gotoMemory">
                <img src="../../../icon/nianling.png" alt="">
                回忆
            </div>
            <div class="setting" @click="gotoSetting">
                <img src="../../../icon/setting.png" alt="">
                设置壁纸
            </div>
            <div class="weixin" @click="showWXCard()">
                <img src="../../../icon/weixin.png" alt="">
                恰个V吧
            </div>
        </div>
        <el-dialog title="恰个v" v-model="dialogFormVisible" width="53.5%" :append-to-body='true'>
                    <!-- 微信名片 -->
    <div class="wxcard">
        <div class="wxid">
            <a href="javascript:;">脑婆脑婆恰个v</a>
            <h2>WX:我杀了一只猩猩</h2>
        </div>
        <div class="cylinder">
            <!-- --i是用来计算平面圆柱的动效延迟和距离的
            --w则是用来计算平面圆柱的宽度 -->
            <div class="d" style="--i:1;--w:1.5"></div>
            <div class="d" style="--i:2;--w:1.6"></div>
            <div class="d" style="--i:3;--w:1.4"></div>
            <div class="d" style="--i:4;--w:1.7"></div>
            <div class="e" style="--i:1"></div>
        </div>
        <!-- 设置二维码 -->
        <div class="Avatar" @click="changebg()"></div>
    </div>
        </el-dialog>
    </div>
    <div class="sideBar" v-show="showFixedSearch">
        <div class="sideIndex" @click="gotoIndex"><img src="../../../icon/user2.png" alt=""></div>
        <div class="sidePhoto" @click="gotoPhoto"> <img src="../../../icon/image.png" alt=""></div>
        <div class="sideMemory" @click="gotoMemory"><img src="../../../icon/nianling.png" alt=""></div>
        <div class="sideSetting" @click="gotoSetting"><img src="../../../icon/setting.png" alt=""></div>
        <div class="sideWeixin" @click="showWXCard"><img src="../../../icon/weixin.png" alt=""></div>
    </div>
</template>

<script>
import {getPersonImage} from '@/service/genServ.js';
import { getLyhPerson } from '@/service/genServ.js';
export default{
    name:'Header',
    data() {
        return {
            avatarurl:'',
            LyhPerson:{
                username:'',
                introduce:'',
            },
            dialogFormVisible:false,
            showFixedSearch: false
        }
    },
    mounted() {
        // 监听页面滚动事件
        window.addEventListener("scroll", this.showSearch)
        getPersonImage().then(
        (res)=>{
            this.avatarurl=res.data.data;
        },
    )
        getLyhPerson().then(
        (res)=>{
            this.LyhPerson=res.data.data[0];
        }
    )
   
    
    },
    unmounted() { 
        //页面离开后销毁，防止切换路由后上一个页面监听scroll滚动事件会在新页面报错问题
        window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
        showSearch() {
                // 获取当前滚动条向下滚动的距离
                let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                // 当页面滚动到高度300px处时，显示搜索框
                if (scrollTop > 80) {
                    this.showFixedSearch = true;
                } else {
                    this.showFixedSearch = false;
                }
        },
        showWXCard(){
            this.dialogFormVisible=!this.dialogFormVisible;
            // console.log(document.documentElement.scrollTop);
        },
        changebg(){
        //展示的是二维码
        if(this.showQRcode){
            let bg = document.querySelector('.QRcode');
            bg.className="Avatar";
        }
        //展示的是头像
        else{
            let bg = document.querySelector('.Avatar')
            bg.className="QRcode";
        }
        this.showQRcode=!this.showQRcode;
    },
    gotoMemory(){
        this.$router.push({
            path:"memoryOfLyh",
        })
    },
    gotoIndex(){
        this.$router.push({
            path:"indexOfLyh",
        })
    },
    gotoPhoto(){
        this.$router.push({
            path:"photoOfLyh",
        })
    },
    gotoSetting(){
        this.$router.push({
            path:"settingOfLyh",
        })
    }
    },
}
</script>

<style scoped>
.sideIndex {
    margin-top: 20px;
    width: 80%;
    height: auto;
    margin-bottom: 20px;
}

.sidePhoto {
    width: 80%;
    height: auto;
    margin-bottom: 20px;
}

.sideMemory{
    width: 80%;
    height: auto;
    margin-bottom: 20px;
}

.sideSetting{
    width: 80%;
    height: auto;
    margin-bottom: 20px;
}

.sideWeixin{
    width: 80%;
    height: auto;
    margin-bottom: 20px;
}

.sideIndex:hover {
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.sidePhoto:hover {
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.sideMemory:hover {
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.sideSetting:hover {
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.sideWeixin:hover {
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}


.sideIndex img {
    width: 100%;
    height: auto;
}

.sidePhoto img {
    width: 100%;
    height: auto;
}

.sideMemory img{
    width: 100%;
    height: auto;
}

.sideSetting img{
    width: 100%;
    height: auto;
}

.sideWeixin img{
    width: 100%;
    height: auto;
}



.whole{
    position: fixed;
    width: 100%;
    height: 100%;
    background-image: url(../assets/background.png);
    background-repeat: repeat-y;
    background-size: cover;
}
.header{
    background-image: url(../assets/cb1c3ef50e22b6096fde67febe863494caefebad.png@2560w_400h_100q_1o.webp);
    position:fixed;
    width: 80%;
    height: 200px;
    background-color: pink;
    left: 50%;
    top: 0;
    transform: translateX(-50%);
}

.wrapper{
    position: absolute;
    bottom: 0;
    display: flex;
}

.imgcontainer{
    height: 75px;
    width: 75px;
    border-radius: 37.5px;
    margin:20px 20px 20px 20px;
}

.imgcontainer img{
    width: 100%;
    height: 100%;
    overflow: hidden;
    border-radius: 50px;
}

.info{
    display:flex;
    flex-direction: column;
    margin:20px 20px 20px 20px;

}

.username{
    display: flex;
    flex:1;
    align-items: center;
    text-align: left;
    font-weight: 700;
    font-size: 22px;
    color: white;
}

.introduce{
    display: flex;
    flex:1;
    align-items: center;
}

.introduce input{
    background: transparent;
    border-radius: 4px;
    border: none;
    box-shadow: none;
    color: hsla(0,0%,100%,.8);
    font-size: 12px;
    font-family: Microsoft Yahei;
    line-height: 26px;
    height: 26px;
    margin-left: -5px;
    padding: 0 5px;
    position: relative;
    top: -1px;
    width: 730px;
    transition: all .3s ease;
    vertical-align: top;
}

.introduce input:focus{
background-color: white;
color: black;
}

.navi{
    display: flex;
    position: fixed;
    width: 80%;
    height: 70px;
    background-color: white;
    left: 50%;
    transform: translateX(-50%);
    top: 200px;
    align-items: center;
}

.index{
    display: flex;
    align-items: center;
    width: 100px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
}

.index img{

    width: 30px;
    height: 30px;
    vertical-align: middle;
}

.photo{
    display: flex;
    align-items: center;
    width: 100px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
}

.photo img{
    width: 30px;
    height: 30px;
    vertical-align: middle;
}

.memory{
    display: flex;
    align-items: center;
    width: 100px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
}

.memory img{
    width: 30px;
    height: 30px;
    vertical-align: middle;
}

.setting{
    display: flex;
    align-items: center;
    width: 100px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
}


.setting img{
    width: 30px;
    height: 30px;
    vertical-align: middle;
}

.weixin{
    display: flex;
    align-items: center;
    width: 100px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
}

.weixin img{
    width: 30px;
    height: 30px;
    vertical-align: middle;
}

.index:hover{
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.photo:hover{
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.setting:hover{
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.weixin:hover{
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

.memory:hover{
    border-bottom: 3px solid rgb(0, 161, 214);
    cursor: pointer;
    transition: all .1s ease;
}

body{
    background-color: rgb(204, 212, 230);
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}
.wxcard{
    position: relative;
    width: 700px;
    height: 400px;
    border: #fff 10px solid;
    background-color: rgb(120,140,200);
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.445);
    filter: drop-shadow(4px 4px 5px rgba(0, 0, 0, 0.164));
}
.wxid{
    position: absolute;
    width: 200px;
    height: 300px;
    left: 0;
    margin: 75px 50px;
    transition: 1s;
}
.wxid a{
    text-decoration: none;
    color: #fff;
    font: 900 28px '';
}
.wxid h2{
    /* 鼠标放开时的动画，第一个值是动画的过渡时间
    第二个值是延迟一秒后执行动画 */
    transition: .5s 1s;
    opacity: 0;
    color: rgb(30,210,200);
    font-size: 18px;
}
.wxid span{
    transition: .5s 1s;
    color: #fff;
    font: 500 15px '';
    position: absolute;
    top: 70px;
}
.cylinder{
    position: absolute;
    top: -130px;
    right: -240px;
}
.d,.e{
    position: absolute;
    right: calc(var(--i)*100px);
    width: calc(var(--w)*40px);
    height: 500px;
    overflow: hidden;
    border-radius: 100px;
    transform: rotateZ(220deg) translate(0,0);
    background: rgb(240,220,150);
    transition: .5s .5s;
}
.d:nth-child(2){
    background: rgb(240,190,230);
}
.e{
    left: -470px;
    top: -140px;
    width: 70px;
    background-color: rgb(90,220,150);
}
.wxcard:hover .cylinder div{
    /* 设置延迟动画 */
    transition: .5s calc(var(--i)*.1s);
    /* 移动的轨迹 */
    transform: rotateZ(220deg) translate(-200px,400px);
}
.wxcard:hover .wxid{
    transition: 1s .5s;
    left: 370px;
}
.wxcard:hover .wxid span{
    transition: 1s .5s;
    top: 105px;
}
.wxcard:hover .wxid h2{
    transition: 1s .5s;
    opacity: 1;
}
.QRcode{
    width: 250px;
    height: 250px;
    position: absolute;
    background-image: url("../assets/QRCode2.jpg");
    background-size: cover;
    margin: 70px;
    opacity: 0;

}
.Avatar{
    width: 250px;
    height: 250px;
    position: absolute;
    background-image: url("../assets/blogger.jpg");
    background-size: cover;
    margin: 70px;
    opacity: 0;
}
.wxcard:hover .Avatar{
    transition: 1s 1.3s;
    opacity: 1;
} 

.wxcard:hover .QRcode{
    transition: 1s 1.3s;
    opacity: 1;
} 

.QRcode:hover{
    cursor:pointer;
}

.Avatar:hover{
    cursor:pointer
}

.sideBar{
    transition: 1s .5s;
    display: flex;
    position:fixed;
    flex-direction: column;
    align-items: center;
    width: 3%;
    background-color: #fff;
    top: 50%;
    transform: translateY(-50%);
}
</style>