<template>
    <Header/>
    <div class="roughGlass">
        <el-button  type="warning" class="corner" icon="el-icon-picture-outline-round" circle @click="showMemoryForm"></el-button>
        <div class="title">
            The Story Unfolds
            <el-divider style="background-color:#909399;"></el-divider>
        </div>
        <div class="shell">
            <template v-for="image in images" :key="image">
                <div class="box">
                    <img :src="image.imageUrl">
                    <span>{{image.content}}</span>
                    <div><el-button type="danger" icon="el-icon-delete" circle @click="deleteImg(image.id)"></el-button></div>
                </div>
                
            </template>
        </div>
        
    </div>
    <el-dialog v-model="memoryFormVisible" title="添加回忆">
        <el-form :model="memory" label-width="120px">
            <el-form-item label="内容">
              <el-input v-model="memory.content" />
            </el-form-item>
            <el-form-item label="上传图片">
                <el-upload
                :data="{content:this.memory.content}"
                :headers="authHead"
                class="avatar-uploader"
                action="/api/lyh/uploadMemoryImage"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <!-- <img v-if="imageUrl" :src="imageUrl" class="avatar"> -->
                <i  class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="memoryFormVisible = false">取消</el-button>
          </span>
        </template>
    </el-dialog>
</template>

<script>
import Header from "@/components/Header";
import {getAuthHeader,getMemoryImage,deleteMemoryImage,getUserBackGround} from '@/service/genServ.js'
export default{
    name:'memoryOfLyh',
    data() {
        return {
            memoryFormVisible:false,
            images:[],
            memory:{
                content:'',

            },
            authHead:getAuthHeader(),
        }
    },
    mounted() {
        var index = document.querySelector('.memory');
        index.style.borderBottom='3px solid rgb(251, 114, 153)';
        var sideIndex = document.querySelector('.sideMemory');
        sideIndex.style.borderBottom='3px solid rgb(251, 114, 153)';
        getMemoryImage().then(
            (res)=>{
                this.images=res.data.data;
                console.log(res.data.data);
            }
        )
        getUserBackGround().then(
            (res)=>{
                this.currentBG=res.data.data;
                // console.log(res.data.data);
                //若当前壁纸不是空字符串，换
        if(this.currentBG!=''){
            var whole=document.querySelector('.whole');
            console.log(whole);
            whole.style.backgroundImage="url("+this.currentBG+")";
        }
            }
        )
    },
    methods: {
        showMemoryForm(){
            this.memoryFormVisible=!this.memoryFormVisible;
        },
        handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        if(res.data.msg==null){
            this.$message({
            message: '添加成功',
            type: 'success',
        })
        
        this.memory.content='';
        this.memoryFormVisible=false;
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
      },
      deleteImg(id){
        deleteMemoryImage({"id":id}).then(
            (res)=>{
                console.log(res.data.msg);
                if(res.data.msg==null){
                    this.$message({
                    message: '删除成功',
                    type: 'success',
                    })
                }
                location.reload();
            }
        )
      }
    },
    components:{
        Header,
    },
}
</script>

<style scoped>
.corner{
    position: absolute;
    top: 20px;
    right: 20px;
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
.title{
    font-size: 22px;
    margin-top: 20px;
    margin-bottom: 20px;
    width: 80%;
}

.shell{
    width: 90%;
    height: 680px;
    display: flex;
    margin-bottom:20px;
}
.box{
    flex: 1;
    overflow: hidden;
    transition: .5s;
    margin: 0 20px;
    box-shadow: 10px 10px 20px rgba(0, 0, 0, .5);
    border-radius: 20px;
    border: 10px solid #fff;
    background-color: #fff;
}
.box>img{
    width: 200%;
    height: 82%;
    object-fit: cover;
    transition: .5s;
}
.box>span{
    font: 200  '优设标题黑';
    text-align: center;
    height: 11%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.box:hover{
    flex-basis: 40%;
}
.box:hover>img{
    width: 100%;
    height: 82%;
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
    border: 1px dashed #d9d9d9;
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
</style>