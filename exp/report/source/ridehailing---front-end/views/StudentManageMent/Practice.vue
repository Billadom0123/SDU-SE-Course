<template>
    <SideBarVue/>
    <div class="position">
        <a href="javascript:;" @click.prevent="gotoIndex">首页</a>
        /
        <a href="javascript:;" >学工管理</a>
        /
        <a href="javascript:;" >社会实践</a>
    </div>
    <el-dialog v-model="dialogFormVisible" title="申报项目">
        <el-form :inline="true" :model="formData">
            <el-form-item label="学期">
                <el-select v-model="formData.term" placeholder="学期">
                    <el-option v-for="item in terms" :key="item.value" :label="item.term" :value="item.term" />
                </el-select>
              </el-form-item>
            <el-form-item label="项目级别">
                <el-select v-model="formData.level" placeholder="项目级别">
                    <el-option :label="'校级'" :value="'校级'"></el-option>
                    <el-option :label="'市级'" :value="'市级'"></el-option>
                    <el-option :label="'省级'" :value="'省级'"></el-option>
                    <el-option :label="'国家级'" :value="'国家级'"></el-option>
                    <el-option :label="'世界级'" :value="'世界级'"></el-option>
                    <el-option :label="'银河系级'" :value="'银河系级'"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="项目内容描述">
                <el-input v-model="formData.content" placeholder="请简述项目名称"></el-input>
              </el-form-item>
              <el-form-item label="获奖情况">
                <el-input v-model="formData.award" placeholder="请简述获奖情况"></el-input>
              </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button type="danger" @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="declare()">
              提交
            </el-button>
          </span>
        </template>
      </el-dialog>
    <div class="practice">
        <el-form :inline="true" :model="conditions" class="conditions">
            <el-form-item label="学期">
                <el-select v-model="conditions.term" placeholder="学期">
                    <el-option v-for="item in terms" :key="item.value" :label="item.term" :value="item.term" />
                </el-select>
              </el-form-item>
              <el-form-item label="项目级别">
                <el-select v-model="conditions.level" placeholder="项目级别">
                    <el-option :label="'校级'" :value="'校级'"></el-option>
                    <el-option :label="'市级'" :value="'市级'"></el-option>
                    <el-option :label="'省级'" :value="'省级'"></el-option>
                    <el-option :label="'国家级'" :value="'国家级'"></el-option>
                    <el-option :label="'世界级'" :value="'世界级'"></el-option>
                    <el-option :label="'银河系级'" :value="'银河系级'"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="审核状态">
                <el-select v-model="conditions.status" placeholder="审核状态">
                    <el-option :label="'已通过'" :value="'已通过'"></el-option>
                    <el-option :label="'未通过'" :value="'未通过'"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="" label="日期">
                <el-input placeholder="年" style="width:10%;margin-right:10px;margin-left:10px;" v-model="this.year"></el-input> -- <el-input placeholder="月"  style="width:10%;margin-right:10px;margin-left:10px;" v-model="this.month"></el-input> -- <el-input placeholder="日" style="width:10%;margin-right:10px;margin-left:10px;" v-model="this.day"></el-input>
                (请以YYYY-MM-DD的形式填写)
              </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="query()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="warning" @click="showForm()">申报项目</el-button>
              </el-form-item>
          </el-form>
        <el-table :data="tableData" height="400" style="width: 100%">
            <el-table-column fixed prop="term" label="学期"  width='125'/>
            <el-table-column prop="level" label="项目级别" />
            <el-table-column prop="content" label="项目内容大致概述"  width='300'/>
            <el-table-column prop="award" label="奖励"  width='300'/>
            <el-table-column prop="date" label="日期" width='150' />
            <el-table-column prop="status" label="审核状态"/>
            <el-table-column fixed="right" label="Operations" width="200">
                <template #default="scope">
                    <el-upload
                        class="upload-demo"
                        :headers="authHeader"
                        action="/api/studentManagement/uploadProof"
                        multiple
                        :data="{id:scope.row.id}"
                        :limit="3"
                        :before-upload="beforeUpload"
                        v-if="scope.row.status=='未通过'"
                        >
                    <el-button size="small" type="primary" >上传凭证资料</el-button>
                    </el-upload>
                    <div v-else>已通过审阅，无需上传凭证</div>
                  </template>
            </el-table-column>
          </el-table>
    </div>

</template>

<script>
import SideBarVue from '../../components/SideBar.vue';
import { showPractice,generateElOption,practiceQuery,addPractice,getAuthHeader } from '../../service/genServ';
export default{
    name:'Practice',
    components:{
        SideBarVue,
    },
    data() {
        return {
            authHeader:getAuthHeader(),
            dialogFormVisible:false,
            terms:{},
            year:"",
            month:"",
            day:"",
            tableData:[
                {
                    id:1,
                    term:'2022-2',
                    level:'校级',
                    content:'一次校级社会实践',
                    award:'奖金500元',
                    date:'2022-08-14',
                    status:'已通过'
                }
            ],
            formData:{
                term:'',
                level:'',
                content:'',
                award:'',
            },
            conditions:{
                term:"",
                level:"",
                status:"",
                date:"",
            }
        }
    },
    mounted() {
        generateElOption().then(
            (res)=>{
                this.terms=res.data.data;
            }
        )
        showPractice().then(
            (res)=>{
                this.tableData=res.data.data;
            }
        )

    },
    methods: {
        query(){
            if(this.year!=''){
                this.conditions.date+=this.year+'-';
                if(this.month!=''){
                    this.conditions.date+=this.month+'-';
                }
                this.conditions.date+=this.day;
            }
            else{
                if(this.month!=''){
                    this.conditions.date+=this.month+'-';
                }
                this.conditions.date+=this.day;
            }
            console.log(this.conditions.date);
            practiceQuery(this.conditions).then(
                (res)=>{
                    this.tableData=res.data.data;
                }
            )
            this.conditions.date='';
        },
        showForm(){
            this.dialogFormVisible=true;
        },
        declare(){
            if(this.formData.term===''||this.formData.level===''||this.formData.content===''||this.formData.award===''){
                this.$message({
              message: '表单不允许有空项!',
              type: 'warning',
            })
            return;
            }
            addPractice(this.formData).then(
                (res)=>{
                    console.log(res.data);
                    if(res.data.msg=="success"){
                        
                        this.$message({
                        message: '申报成功!',
                        type: 'success',
                        })
                    }
                }
            )
            this.dialogFormVisible=false;
        },
        beforeUpload(file){
            console.log(file.type);
            const isZip = file.type==='application/x-zip-compressed';
            const isLt80M = file.size/1024/1024<80;
            if(!isZip){
                this.$message.error('上传凭证只能是zip压缩包');
            }
            if(!isLt80M){
                this.$message.error('上传凭证大小不能超过80MB!');
            }
            return isZip&&isLt80M;
        }
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

.practice{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}

.conditions{
    width: 1200px !important;
}

</style>
