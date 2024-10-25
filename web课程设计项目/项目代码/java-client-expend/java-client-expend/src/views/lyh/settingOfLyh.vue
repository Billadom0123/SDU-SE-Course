<template>
    <Header/>
    <div class="roughGlass">
        <div class="title">
            设置壁纸
            <el-divider style="background-color:#909399;"></el-divider>
        </div>
        <div class="tabs">
            <el-button-group>
                <el-button type="warning" @click="showDefault" class="first">默认</el-button>
                <el-button type="warning" @click="showPrevious" class="second">曾经选用</el-button>
                <el-button type="warning" @click="showNew" class="third">添加新壁纸</el-button>
              </el-button-group>
        </div>
        <div class="default" ref="default">
            <template v-for="image in defaultImages" :key="image">
                <div class="bgcontainer">
                    <img :src="image.imageUrl" @click="changeBG(image.index,image.suffix)">
                    <div class="selected" v-if="(image.selected=='true')"><i class="el-icon-check"></i></div>
                </div>
            </template>
        </div>
        <div class="previous" ref="previous">
            <template v-for="image in previousImages" :key="image">
                <div class="bgcontainer">
                    <img :src="image.imageUrl" @click="changeBGFromPre(image.index)">
                    <div class="selected" v-if="(image.selected=='true')"><i class="el-icon-check"></i></div>
                </div>
            </template>
        </div>
        <div class="new">
            当前壁纸:
            <div class="bgcontainer">
                <el-upload
                    class="avatar-uploader"
                    :headers="authHead"
                    action="/api/lyh/setUserBackGroundInNew"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload">
                    <img v-if="currentBG" :src="currentBG" class="avatar-uploader-icon">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </div>
        </div>
    </div>
    </template>

<script>
import Header from "@/components/Header";
import {getAuthHeader,getDefaultBackGround,setUserBackGroundInDefault,getUserBackGround,addUserPreviousBackGroundFromDefault,getPreviousBackGround,setUserBackGroundInPrevious} from '@/service/genServ.js'
export default{
    name:'settingOfLyh',
    data() {
        return {
            currentBG:'',
            defaultImages:[],
            previousImages:[],
            authHead:getAuthHeader(),
        }
    },
    mounted() {
        var index = document.querySelector('.setting');
        index.style.borderBottom='3px solid rgb(251, 114, 153)';
        var sideIndex = document.querySelector('.sideSetting');
        sideIndex.style.borderBottom='3px solid rgb(251, 114, 153)';
        this.showDefault();
        getDefaultBackGround().then(
            (res)=>{
                //  console.log(res.data.data);
                this.defaultImages=res.data.data;
            }
        )
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
        getPreviousBackGround().then(
            (res)=>{
                this.previousImages=res.data.data;
            }
        )
        
    },
    methods: {
        showDefault(){
            var de = document.querySelector('.default');
            var pre = document.querySelector('.previous');
            var ne = document.querySelector('.new');
            var first = document.querySelector('.first');
            var second = document.querySelector('.second');
            var third = document.querySelector('.third');
            first.style.backgroundColor="#409EFF";
            second.style.backgroundColor='#E6A23C';
            third.style.backgroundColor='#E6A23C';
            de.style.display='flex';
            pre.style.display='none';
            ne .style.display='none';
        },
        showPrevious(){
            var de = document.querySelector('.default');
            var pre = document.querySelector('.previous');
            var ne = document.querySelector('.new');
            var first = document.querySelector('.first');
            var second = document.querySelector('.second');
            var third = document.querySelector('.third');
            first.style.backgroundColor="#E6A23C";
            second.style.backgroundColor='#409EFF';
            third.style.backgroundColor='#E6A23C';
            de.style.display='none';
            pre.style.display='flex';
            ne .style.display='none';
        },
        showNew(){
            var de = document.querySelector('.default');
            var pre = document.querySelector('.previous');
            var ne = document.querySelector('.new');
            var first = document.querySelector('.first');
            var second = document.querySelector('.second');
            var third = document.querySelector('.third');
            first.style.backgroundColor="#E6A23C";
            second.style.backgroundColor='#E6A23C';
            third.style.backgroundColor='#409EFF';
            de.style.display='none';
            pre.style.display='none';
            ne .style.display='flex';
        },
        changeBG(index,suffix){
            setUserBackGroundInDefault({"index":index,"suffix":suffix}).then(
                (res)=>{
                    // console.log(res);
                    this.currentBG=res.data.data;
                    location.reload();
                }
            )
            addUserPreviousBackGroundFromDefault({"index":index,"suffix":suffix}).then(
                (res)=>{
                    console.log(res.data.data);
                }
            )
        },
        changeBGFromPre(index){
            setUserBackGroundInPrevious({"index":index}).then(
                (res)=>{
                    this.currentBG=res.data.data;
                    location.reload();
                }
            )
        },
        handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        if(res.data.msg==null){
            location.reload();
        }
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
    components:{
        Header,
    },
}
</script>

<style scoped>
div{
    box-sizing: border-box;
}
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

.title{
    transform: translateX(12.5%);
    font-size: 22px;
    margin-top: 20px;
    margin-bottom: 20px;
    width: 80%;
}

.tabs{
    text-align: left !important;
    margin-left: 20px;
}

.default{
    display: flex;
    margin-top: 20px;
    margin-left: 20px;
    margin-bottom: 20px;
    width: 80%;
    background-color: transparent;
    flex-wrap: wrap;
}

.bgcontainer{
    position:relative;
    width:28%;
    aspect-ratio: 1;
    background-color: transparent;
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
}

.bgcontainer img{
    width: 100%;
    height: 100%;
}

.bgcontainer img:hover{
    cursor: pointer;
}

.selected{
    position: absolute;
    right: 0;
    top: 0;
    background-color: #67C23A;
    width: 20px;
    height: 20px;
    border-radius: 10px;
}

.previous{
    display: flex;
    margin-top: 20px;
    margin-left: 20px;
    margin-bottom: 20px;
    width: 80%;
    background-color: transparent;
    flex-wrap: wrap;
}

.new{
    display: flex;
    margin-top: 20px;
    margin-left: 20px;
    margin-bottom: 20px;
    width: 80%;
    background-color: transparent;
    flex-wrap: wrap;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload .avatar-uploader-icon:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    border: 4px dashed #67C23A;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
</style>