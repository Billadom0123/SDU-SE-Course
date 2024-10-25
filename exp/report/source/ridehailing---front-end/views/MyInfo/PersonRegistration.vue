<template>
<SideBarForAdmin/>
<div class="position">
    <a href="javascript:;" >首页</a>
    /
    <a href="javascript:;" >人员登记</a>
</div>
<div class="card">
    <div class="in">
        <el-upload
        class="upload-demo"
        action="/api/info/uploadPerson"
        :headers="authHeader"
        multiple
        :before-upload="beforeExcelUpload"
        :on-success="uploadSuccess">
        <el-button type="success">人员录入</el-button>
        </el-upload>
    </div>
    <div class="out">
        <el-upload
        class="upload-demo"
        action="/api/score/uploadScoreExcel"
        :headers="authHeader"
        multiple
        :before-upload="beforeExcelUpload"
        :on-success="uploadSuccess">
        <el-button type="danger">人员移除</el-button>
        </el-upload>
    </div>
</div>
</template>

<script>
import SideBarForAdmin from "@/components/SideBarForAdmin";
import { getAuthHeader } from "@/service/genServ.js"
export default{
    name:'AddPerson',
    data() {
        return {
            authHeader:getAuthHeader(),
        }
    },
    mounted() {
        
    },
    methods: {
        beforeExcelUpload(file) {
        const isJPG = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        const isLt80M = file.size / 1024 / 1024 < 80;

        if (!isJPG) {
          this.$message.error('上传文件只能是 xlsx/xls 格式!');
        }
        if (!isLt80M) {
          this.$message.error('上传Excel大小不能超过 2MB!');
        }
        return isJPG && isLt80M;
    },
    uploadSuccess(res){
      console.log(res);
      if(res.data.msg===null){
        this.$message({
            message: '导入成功',
            type: 'success',
        })
        location.reload();
      }
    }
    },
    components:{
        SideBarForAdmin,
    }
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

  .card{
    position: relative;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    border:1px solid #97A8BE;
    border-radius: 5px;
    width: 80%;
    height: 60%;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}

.in{
    position:absolute;
    left: 50%;
    top: 50%;
}

.out{
    display:none;
    position: absolute;
    right: 20%;
    top: 50%;
}
</style>