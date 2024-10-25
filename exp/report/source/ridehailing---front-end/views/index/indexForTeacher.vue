<template>
    <SideBarForTeacher/>
    <div class="position">
      <a href="javascript:;" >首页</a>
    </div>
    <!-- <el-upload
      class="upload-demo"
      action="/api/score/uploadScoreExcel"
      :headers="authHeader"
      multiple
      :before-upload="beforeExcelUpload"
      :on-success="uploadSuccess">
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload> -->
    <!-- <button @click="download">下载</button> -->
</template>

<script>
import SideBarForTeacher from "@/components/SideBarForTeacher";
import {getAuthHeader} from '@/service/genServ.js'
export default {
  name: "indexForTeacher",
  data() {
    return {
      authHeader:getAuthHeader(),
    }
  },
  components:{
    SideBarForTeacher,
  },
  methods: {
    // download(){
    //   location.href='http://localhost:9090/api/course/downloadExcel?classId=1'
    // }
    // beforeExcelUpload(file) {
    //     const isJPG = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    //     const isLt80M = file.size / 1024 / 1024 < 80;

    //     if (!isJPG) {
    //       this.$message.error('上传文件只能是 xlsx/xls 格式!');
    //     }
    //     if (!isLt80M) {
    //       this.$message.error('上传Excel大小不能超过 2MB!');
    //     }
    //     return isJPG && isLt80M;
    // },
    // uploadSuccess(res){
    //   console.log(res);
    //   if(res.data.msg===null){
    //     this.$message({
    //                         message: '导入成功',
    //                         type: 'success',
    //                         })
    //   }
    // }
  },
}
</script>

<style scoped>
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
</style>