<template>
<Header/>
<!-- <div class="roughGlass"> -->
    <div class="a">
        <div class="b"><span>
           我杀了一只猩猩
        </span></div>
    </div>
<!-- </div> -->
</template>

<script>

import Header from "@/components/Header";
import {getUserBackGround} from '@/service/genServ.js'
export default{
    name:'indexOfLyh',
    components:{
        Header,
    },
    mounted() {
        var index = document.querySelector('.index');
        index.style.borderBottom='3px solid rgb(251, 114, 153)';
        var sideIndex = document.querySelector('.sideIndex');
        sideIndex.style.borderBottom='3px solid rgb(251, 114, 153)';
        getUserBackGround().then(
            (res)=>{
                this.currentBG=res.data.data;
                // console.log(res.data.data);
                //若当前壁纸不是空字符串，换
        if(this.currentBG!=''){
            var whole=document.querySelector('.whole');
            // console.log(whole);
            whole.style.backgroundImage="url("+this.currentBG+")";
        }
            }
        )
    },
}
</script>

<style scoped>
    .roughGlass{
        z-index:10;
        position: relative;
        left: 50%;
        transform: translateX(-50%);
        top: 300px;
        width: 80%;
        height: 450px;
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
        margin-bottom:20px;
    }
    .a{
        position: relative;
        top:60%;
        left: 50%;
        transform: translate(-50%,-50%);
        width: 500px;
        height: 120px;
        border: solid 10px #fff;
        box-shadow: 0 0 70px rgb(190,40,210);
        display: flex;
        justify-content: center;
        align-items: center;
        /* 设置鼠标移上去时变成小手形状 */
        cursor: pointer;
    }
    .a::after{
        content: '';
        position: absolute;
        width: 500px;
        height: 120px;
        box-shadow: 0 0 5px rgba(190,40,210);
        background-color: rgba(100,30,225,.4);
    }
    .a:hover{
        animation: a 1.5s;
    }
    @keyframes a{
        0%,34%,68%,100%{
            border: solid 10px #fff;
            box-shadow: 0 0 70px rgb(190,40,210);
        }
        17%,51%,85%{
            border: solid 10px rgba(255,0,0,.5);
            box-shadow: 0 0 90px rgba(255,0,0,.8);
        }
    }
    .b,.b::before{
        z-index: 999;
        color: #fff;
        position: absolute;
        font-size: 55px;
        font-weight: 900;
        /* 设置字体间距 */
        letter-spacing: 12px;
    }
    .b::before{
        content: '我杀了一只猩猩';
        text-shadow: -5px -5px 0px rgb(211,250,9),5px 5px 0px rgb(25,10,240);
        /* 使用缩放的方式创建可见显示取余，括号里的四个值分别是top，right，bottom，left */
        clip-path: inset(100% 0px 0px 0px);

    }
    .a:hover .b::before{
        /* steps设置逐帧动画，值越小越卡顿 */
        animation: move 1.25s steps(2);
    }
    /* 这是制造混乱的位置和高宽，可以自行改变，随机的 */
    @keyframes move{
        0%{
            clip-path:inset(80% 0px 0px 0px);
            transform:translate(-20px,-10px)
        }
        10%{
            clip-path:inset(10% 0px 85% 0px);
            transform:translate(10px,10px)
        }
        20%{
            clip-path:inset(80% 0px 0px 0px);
            transform:translate(-10px,10px)
        }
        30%{
            clip-path:inset(10% 0px 85% 0px);
            transform:translate(0px,5px)
        }
        40%{
            clip-path:inset(50% 0px 30% 0px);
            transform:translate(-5px,0px)
        }
        50%{
            clip-path:inset(10% 0px 30% 0px);
            transform:translate(5px,0px)
        }
        60%{
            clip-path:inset(40% 0px 30% 0px);
            transform:translate(5px,10px)
        }
        70%{
            clip-path:inset(50% 0px 30% 0px);
            transform:translate(-10px,10px)
        }
        80%{
            clip-path:inset(80% 0px 5% 0px);
            transform:translate(20px,-10px)
        }
        90%{
            clip-path:inset(80% 0px 0px 0px);
            transform:translate(-10px,0px)
        }
        100%{
            clip-path:inset(80% 0px 0px 0px);
            transform:translate(0px,0px)
        }
    }
</style>