<template>
    <Header/>
    <div class="shell">
        <div style="box-shadow:0 0 0 0;block-size:120px"></div>
        <template v-for="image in images" :key="image">
            <div><img :src="image.imageUrl" alt=""></div>
        </template>
        <div style="box-shadow:0 0 0 0;block-size:120px"></div>
    </div> 
</template>


<script>
import Header from "@/components/Header";
import {getUserBackGround,getMemoryImage} from '@/service/genServ.js'
export default{
    name:'photoOfLyh',
    data() {
        return {
            images:[],
        }
    },
    mounted() {
        var index = document.querySelector('.photo');
        index.style.borderBottom='3px solid rgb(251, 114, 153)';
        var sideIndex = document.querySelector('.sidePhoto');
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
        getMemoryImage().then(
            (res)=>{
                this.images=res.data.data;
                console.log(res.data.data);
            }
        )
    },
    components:{
        Header,
    }
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
    display: flex;
    border-radius: 15px;
    flex-direction: column;
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

* {
    padding: 0;
    margin: 0;
}

body {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: rgb(235, 185, 239);
}
.shell{
    transform: translateY(50%);
    /* 设置两边阴影 */
    -webkit-mask-image: linear-gradient(to right,#0000,#000,#000,#0000);
    overflow-x: auto;
    display: flex;
    /* 设置间距 */
    gap: 5rem;
    align-items:flex-start;
    padding: 70px;
}
.shell>div{
    block-size: 320px;
    background-color: transparent;
    /* 设置宽高比 */
    aspect-ratio: 1;
    border-radius: 10px;
    box-shadow: 0 0 5px 5px rgba(173, 173, 173, 0.3);
}
.shell img{
    height: 100%;
    width: 100%;
}

/* 设置滚动条的高度 */
::-webkit-scrollbar{
    height: 16px;
}
/* 设置滚动条的背景色 */
::-webkit-scrollbar-track{
    background-color: #f5f5f5;
}
::-webkit-scrollbar-thumb{
    background-color: rgb(128, 215, 239);
    border-radius: 10px;
}
</style>