<template>
    <SideBarForTeacher/>
    <div class="position">
        <a href="javascript:;" >首页</a>
        /
        <a href="javascript:;" >教师课程</a>
        /
        <a href="javascript:;" >开设课程</a>
    </div>
    <el-dialog title="申报课程" v-model="dialogFormVisible">
        <el-form :model="form">
            <el-row :span="12">
                <el-form-item label="课程名称" style="margin-right:20px;">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item   label="年级">
                    <el-select  v-model="form.grade" placeholder="年级">
                        <el-option label="大一" value="1"></el-option>
                        <el-option label="大二" value="2"></el-option>
                        <el-option label="大三" value="3"></el-option>
                        <el-option label="大四" value="4"></el-option>
                      </el-select>
                    </el-form-item>
            </el-row>
            <el-row :span="12">
                <el-form-item label="学分" style="margin-right:20px;">
                    <el-input v-model="form.credit"></el-input>
                </el-form-item>
                <el-form-item   label="课程性质">
                    <el-select  v-model="form.prop" placeholder="课程性质">
                      <el-option label="必修课" value="必修课"></el-option>
                      <el-option label="限选课" value="限选课"></el-option>
                      <el-option label="任选课" value="任选课"></el-option>
                    </el-select>
                  </el-form-item>
            </el-row>
            <el-row :span="12">
                <el-form-item label="上课地点" style="margin-right:20px;">
                    <el-input v-model="form.place"></el-input>
                </el-form-item>
                <el-form-item label="周期" style="margin-right:20px;">
                    <el-input v-model="form.scope"></el-input>
                </el-form-item>
            </el-row>
            <el-row>
                <el-form-item label="课程容量" style="margin-right:20px;">
                    <el-input v-model="form.upperLimit"></el-input>
                </el-form-item>
            </el-row>
            <el-form-item label="上课时间" style="margin-right:20px;">
                <table id="timetable">
                    <tr>
                        <th class="week">&nbsp;</th>
                        <th class="week">星期一</th>
                        <th class="week">星期二</th>
                        <th class="week">星期三</th>
                        <th class="week">星期四</th>
                        <th class="week">星期五</th>
                        <th class="week">星期六</th>
                        <th class="week">星期日</th>
                    </tr>
                    <tr class="tr0">
                        <th class="time">第一节</th>
                        <td v-for="item in value" :key="item" @click="changeTime(item,0)" v-bind:class="getColor(item,0)"></td>
                    </tr>
                    <tr class="tr1">
                        <th class="time">第二节</th>
                        <td v-for="item in value" :key="item" @click="changeTime(item,1)" v-bind:class="getColor(item,1)"></td>
                    </tr>
                    <tr class="tr2">
                        <th class="time">第三节</th>
                        <td v-for="item in value" :key="item" @click="changeTime(item,2)" v-bind:class="getColor(item,2)"></td>
                    </tr>
                    <tr class="tr3">
                        <th class="time">第四节</th>
                        <td v-for="item in value" :key="item" @click="changeTime(item,3)" v-bind:class="getColor(item,3)"></td>
                    </tr>
                    <tr class="tr4">
                        <th class="time">第五节</th>
                        <td v-for="item in value" :key="item" @click="changeTime(item,4)" v-bind:class="getColor(item,4)"></td>
                    </tr>
                </table>
            </el-form-item>
            <el-form-item label="上传申报表" style="margin-right:20px;">
                <el-upload
                class="upload-demo"
                action="/api/course/uploadCourseZip"
                :headers="authHeader"
                multiple
                :before-upload="beforeUpload"
                :on-success="uploadSuccess">
                <el-button size="small" type="success">上传申报表</el-button>
              </el-upload>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
              <el-button type="warning" @click="remake()">重置</el-button>
              <el-button type="danger" @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="submit()">
                确定
              </el-button>
  
            </span>
          </template>
      </el-dialog>
      
    <div class="tablecontainer">
        <el-button type="warning" @click="showDialog" icon="el-icon-circle-plus-outline" circle style="float:right;margin-bottom:10px;"></el-button>
        <el-table :data="tableData" height="400" style="width: 100%">
            <el-table-column fixed prop="name" label="课程名称" width="300" />
            <el-table-column prop="credit" label="学分" width="200" />
            <el-table-column prop="scope" label="周期" width="200" />
            <el-table-column prop="place" label="地点" width="300" />
            <el-table-column prop="prop" label="课程性质" width="200" />
            <el-table-column prop="grade" label="年级" width="100" />
            <el-table-column prop="time" label="授课时间" width="600" />
            <el-table-column fixed="right" label="Operations" width="200">
                <template #default="scope">
                    <div style="display:flex;">
                        <el-upload
                        action="/api/course/updateCourseZip"
                        :data="{id:scope.row.id}"
                        :headers="authHeader"
                        multiple
                        :before-upload="beforeUpload"
                        :on-success="uploadSuccess"
                        v-if="scope.row.upload=='false'&&this.isOpen.isOpen=='true'">
                        <el-button  type="success" ref="upload">申报</el-button>
                    </el-upload>
                    <el-button type="info" v-if="scope.row.upload=='true'&&this.isOpen.isOpen=='true'" @click="warn()" ref="warn">已申报</el-button>
                    <el-button type="danger" style="margin-left:20px;" @click="withdraw(scope.row.id)" v-if="this.isOpen.isOpen=='true'">撤回</el-button>
                    <el-button type="primary" style="margin-left:20px;" @click="download(scope.row.id)" v-if="this.isOpen.isOpen=='false'">名单</el-button>
                </div>
                    
                  </template>
            </el-table-column>
          </el-table>
    </div>

</template>

<script>
import SideBarForTeacher from "@/components/SideBarForTeacher";
import {showTeacherCourse,getAuthHeader,getSimpleTeacherCourse,addCourse,deleteCourse,checkIsOpen} from "@/service/genServ.js"
export default {
  name: "AddCourse",
  data() {
    return {
        lesson:[],
        tableData:[],
        form:{
            name:'',
            credit:null,
            place:'',
            prop:'',
            scope:'',
            grade:null,
            upperLimit:null,
            time:[],

        },
        dialogFormVisible:false,
        value:[0,1,2,3,4,5,6],
        authHeader:getAuthHeader(),
        isOpen:"",
    }
  },
  mounted() {
    checkIsOpen().then(
        (res)=>{
          console.log(res.data.data);
          this.isOpen=res.data.data;
          console.log(this.isOpen);
        }
      )
    getSimpleTeacherCourse().then(
        (res)=>{
            this.lesson=res.data.data;
            console.log(this.lesson);
        }
    )
    showTeacherCourse().then(
        (res)=>{
            this.tableData=res.data.data;
        }
    )
  },
  methods: {
    download(id){
        location.href="http://localhost:9090/api/course/downloadExcel?courseId="+id;
    },
    getColor(index,offset){
        var position = index*5+offset;
        var check = this.lesson[position];
        if(check.prop==='必修课'){
            return 'req';
        }
        else if(check.prop=='任选课'){
            return 'opt';
        }
        else if(check.prop=='限选课'){
            return 'lim';
        }
        return '';
    },
    //课表选择时间
    changeTime(index,offset){
        var value = index*5+offset;
        var str = '.tr'+offset;
        var tr=document.querySelector(str);
        var td=tr.children[index+1];
        if(td.className==''){
            td.className='green';
            this.form.time[this.form.time.length]=value;
        }else if(td.className=='green'){
            //取消选择
            td.className='';
            var arr=new Array();
            for(var i=0;i<this.form.time.length;i++){
                //相同跳过，不同置入arr中
                if(this.form.time[i]==value){
                    continue;
                }
                else{
                    arr[arr.length]=this.form.time[i];
                }
            }
            this.form.time=arr;
        }
        //已经有课了
        else{
            this.$message.error('课程时间冲突,请检查已申请课程');
        }
        console.log(this.form.time);
    },
    showDialog(){
        this.dialogFormVisible=!this.dialogFormVisible;
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
    },
    uploadSuccess(res){
      console.log(res);
      if(res.data.msg===null){
        this.$message({
            message: '添加成功',
            type: 'success',
        })
      }
    },
    remake(){
        var greens = document.querySelectorAll('.green');
        console.log(greens);
        for(var i=0;i<greens.length;i++){
            greens[i].className='';
        }
        this.form.name='';
        this.form.credit=null;
        this.form.place='';
        this.form.prop='';
        this.form.scope='';
        this.form.grade=null;
        this.form.time=new Array();
    },
    submit(){
        //安全性校验
        if(this.form.name==''||this.form.credit==null||this.form.place==''||this.form.prop==''||this.form.scope==''||this.form.grade==null||this.form.time.length==0){
            this.$message.error('表单项不允许有空!');
            return;
        }
        //格式化time
        var temp = '';
        for(var i=0;i<this.form.time.length;i++){
            if(i==0){
                temp+=this.form.time[i];
            }
            else{
                temp+=' '+this.form.time[i];
            }
        }
        this.form.time=temp;
        console.log(temp);
        addCourse(this.form).then(
            (res)=>{
                if(res.data.msg=='success!'){
                    this.$message({
                        message: '添加成功',
                        type: 'success',
                    })
                }
            }
        )
        this.dialogFormVisible=!this.dialogFormVisible;
        location.reload();
    },
    warn(){
        this.$message({
            message: '该项目书已经申报过了',
            type: 'info',
        })  
    },
    withdraw(id){
        this.$confirm('是否撤回?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            deleteCourse({"id":id}).then(
                (res)=>{
                    if(res.data.msg=="success!"){
                        this.$message({
            type: 'success',
            message: '删除成功!'
          });
                    }
                    location.reload();
                }
            )
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
    }
  },
  
  components:{
    SideBarForTeacher,
  },
}
</script>

<style scoped>

.green{
    background-color: #67C23A;
}

.req{
    background-color: pink;
}
.opt{
    background-color: rgb(190,237,242);
}
.lim{
    background-color: rgb(205,221,252);
}
.tablecontainer{
    position: absolute;
    left:50%;
    top:50%;
    width:80%;
    transform:translate(-50%,-50%);
}
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

  #timetable{
    border-collapse: collapse;
    color: #757575;
    background-color: #fff;
    border: 1px solid #757575;
}

#timetable td{
    border: 1px solid #757575;
}

#timetable td:hover{
    cursor: pointer;
}

#timetable th{
    text-align: center;
    padding-left: 10px;
    padding-right: 10px;
    padding-bottom: 10px;
    padding-top: 10px;
    border: 1px solid #757575;
}
</style>