<template>
<SideBarForTeacher/>
<div class="position">
    <a href="javascript:;" >首页</a>
    /
    <a href="javascript:;" >教师课程</a>
    /
    <a href="javascript:;" >录入成绩</a>
</div>
        
<div class="tablecontainer">
    <!-- <el-button type="warning" @click="showDialog" icon="el-icon-circle-plus-outline" circle style="float:right;margin-bottom:10px;"></el-button> -->
    <el-table :data="tableData" height="400" style="width: 100%">
        <el-table-column fixed prop="name" label="课程名称" width="300" />
        <el-table-column prop="credit" label="学分" width="200" />
        <el-table-column prop="scope" label="周期" width="200" />
        <el-table-column prop="place" label="地点" width="300" />
        <el-table-column prop="prop" label="课程性质" width="200" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="time" label="授课时间" width="600" />
        <el-table-column fixed="right" label="Operations" width="100">
            <template #default="scope">
                <div style="display:flex;">
                    <el-upload
                    class="upload-demo"
                    action="/api/score/uploadScoreExcel"
                    :data="{change:scope.row.checked,id:scope.row.id}"
                    :headers="authHeader"
                    multiple
                    :before-upload="beforeExcelUpload"
                    :on-success="uploadSuccess">
                    <el-button v-if="(scope.row.checked=='false')" type="success">录入</el-button>
                    <el-button type="warning" v-else >修改</el-button>
                  </el-upload>

            </div>
              </template>
        </el-table-column>
      </el-table>
</div>
    
</template>

<script>
import SideBarForTeacher from "@/components/SideBarForTeacher";
import { showTeacherScore,getAuthHeader } from "@/service/genServ.js"
export default {
    name:'AddScore',
    data() {
        return {
            tableData:[],
            authHeader:getAuthHeader(),
            change:true,
        }
    },
    mounted() {
        showTeacherScore().then(
            (res)=>{
                this.tableData=res.data.data;
            }
        )
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
    SideBarForTeacher,
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

  .tablecontainer{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}
</style>