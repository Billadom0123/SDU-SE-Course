<template>

    <el-upload
  class="avatar-uploader"
  action="https://jsonplaceholder.typicode.com/posts/"
  :show-file-list="false"
  :on-success="handleAvatarSuccess"
  :before-upload="beforeAvatarUpload">
  <img v-if="imageUrl" :src="imageUrl" class="avatar">
  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
</el-upload>
</template>
<script>
    export default {
        data() {
            return {
                showFixedSearch: false,
                imageUrl:'',
            }
        },
        mounted() {
            // 监听页面滚动事件
            window.addEventListener("scroll", this.showSearch)
        },
        methods: {
            showSearch() {
                // 获取当前滚动条向下滚动的距离
                let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                // 当页面滚动到高度300px处时，显示搜索框
                if (scrollTop > 300) {
                    this.showFixedSearch = true;
                } else {
                    this.showFixedSearch = false;
                }
            },
        }
    }
</script>
<style scoped>
    .fixedSearch {
        position: fixed;
        width: 100%;
        z-index: 999;
        background: #f6f6f6;
        height: 120px;
        overflow: hidden;
    }
 
    .searchBox {
        position: absolute;
        top: -100px;
        left: 50%;
        transform: translateX(-50%);
    }
 
    .box100 {
        height: 100px;
        background: #3a8ee6;
        border: 1px solid black;
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
 