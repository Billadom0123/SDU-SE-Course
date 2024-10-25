<template>
    <SideBar/>
    <div class="position">
        <a href="javascript:;" @click="gotoIndex">首页</a>
        /
        <a href="javascript:;" >我的主页</a>
        /
        <a href="javascript:;" >学生互评</a>
    </div>
    <el-dialog v-model="dialogFormVisible" >
        <div style="margin-bottom:20px;font-weight:700;font-size:18px;">{{this.name}}</div>
        <template v-for="tag in dynamicTags" :key="tag">
                <el-tag
                closable
                :disable-transitions="false"  
                @close="handleClose(tag)"
                v-bind:type="changeTagColor(tag)"
                @click="addTags(tag)"
                >
                {{tag}}
                </el-tag>
            
        </template>
        <!-- <el-tag
            type="danger"
            :key="tag"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{tag}}
        </el-tag> -->

        <el-input
        class="input-new-tag"
        v-if="inputVisible"
        v-model="inputValue"
        ref="saveTagInput"
        size="small"
        @keyup.enter="handleInputConfirm"
        @blur="handleInputConfirm"
        >
        </el-input>

        <el-button v-else class="button-new-tag" size="small" @click="showInput"> +点我新增</el-button>

        <el-form>
            <el-form-item>
                <el-input v-model="tags" readonly:true placeholder="不要尝试输入,请使用标签"></el-input>
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

    <!-- 给别人发互评 -->
    <div class="deliver">
        <div class="searchcontainer">
            <input type="text" class="search" placeholder="输入TA的名字或学号吧" v-model="this.numname">
            <button @click="search"><img src="../../../../icon/search.png" alt=""></button>
        </div>
        <!-- 搜索结果 -->
        <el-table :data="tabledata" height="200" style="width: 100%">
            <el-table-column prop="num" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column fixed="right" label="互评" width="100">
                <template #default="scope">
                    <el-button type="primary" size="small" style="border:none;" @click="fetchId(scope.row)"><img src="../../../../icon/add.png" alt="" style="width:20px"></el-button>
                  </template>
            </el-table-column>
        </el-table>
        <!-- 最近评价过我的 -->
        <div class="recently">最近评价过我</div>
        <el-table :data="recentdeliver" height="150" style="width: 100%">
            <el-table-column prop="num" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column fixed="right" label="互评" width="100">
                <template #default="scope2">
                    <el-button type="primary" size="small" style="border:none;"><img src="../../../../icon/add.png" alt="" style="width:20px" @click="fetchId(scope2.row)"></el-button>
                  </template>
            </el-table-column>
        </el-table>
        <!-- 最近我评价过TA -->
        <div class="recently">最近我评价过</div>
        <el-table :data="recentreceiver" height="150" style="width: 100%">
            <el-table-column prop="num" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column fixed="right" label="互评" width="100">
                <template #default="scope3">
                    <el-button type="primary" size="small" style="border:none;"><img src="../../../../icon/add.png" alt="" style="width:20px" @click="fetchId(scope3.row)"></el-button>
                  </template>
            </el-table-column>
        </el-table>
    </div>
    <!-- 互评消息记录 -->
    <div class="record">
        <div style="margin-top:20px;font-size:22px;font-weight:700;margin-bottom:30px;">叮!你有一条来自TA的评价:</div>
        <div style="text-align:left;color:black;font-size:20px;" v-if="records.length==0">还没有人评价过你哦。先送朋友们一个互评吧!</div>
        <template v-for="(record,i) in  records" :key="i">
            <div style="display:flex">
                <li></li>
                <div style="text-align:left;"> <span style="color:black;font-size:20px;">{{record.name}}</span>&nbsp;评价了你:&nbsp;<span style="color:black;font-size:20px;vertical-align:center;">{{record.assessment}}</span></div>
                <img src="../../../../icon/-xinxiaoxi.png" alt="" style="height:20px;margin-left:auto;" v-if="record.checked==='false'">
            </div>
            
            <el-divider/>
        </template>
    </div>
    <!-- 特制词云 -->
    <div class="wordcloud">
        <el-carousel :interval="10000" type="card" height="200px">
            <el-carousel-item v-for="(assessment,i) in rank3" :key="i">
                <img src="../../../../icon/first.png" alt="" v-if="i===0" style="width:50px;">
                <img src="../../../../icon/second.png" alt="" v-if="i===1" style="width:50px;">
                <img src="../../../../icon/third.png" alt="" v-if="i===2" style="width:50px;">
                <div style="margin-top:20px;" v-if="assessment.names!=''&&assessment.assessment!=''"><span style="color:black;font-size:20px;">{{assessment.names}}</span>评价了你: &nbsp; 
                    <br>
                    <span style="color:black;font-size:20px;">{{assessment.assessment}}</span>
                </div>
                <div v-else style="margin-top:20px;color:black;font-size:20px;">评价太少啦,快邀请好友评价你吧!</div>
            </el-carousel-item>
          </el-carousel>
    </div>
</template>

<script>
import SideBar from "@/components/SideBar";
import {showWordCloud,showAssessment,changeChecked,showRecentDeliver,showRecentReceive,searchStudent,addAssessment} from "@/service/genServ.js";
export default{
    name:"MyAssessment",
    data() {
        return {
            tabledata:[],
            recentdeliver:[],
            recentreceiver:[],
            records:[],
            rank3:[{},{},{}],
            numname:'',
            dialogFormVisible:false,
            id:1,
            name:'',
            dynamicTags: ['学霸', '土豪', '运动健将','游戏狂','数码迷','美食家','文青','海王','歌姬','下头男','普信男'],
            inputVisible: false,
            inputValue: '',
            tags:'',
            Tags:[['学霸', '土豪', '运动健将','游戏狂','数码迷','美食家','文青','海王','歌姬','下头男','普信男']]
        }
    },
    mounted(){
        showAssessment().then(
            (res)=>{
                //console.log(res.data.data);
                this.records=res.data.data;
            }
        )
        showWordCloud().then(
            (res)=>{
                this.rank3=res.data.data;
            }
        )
        changeChecked();
        showRecentDeliver().then(
            (res)=>{
                this.recentdeliver=res.data.data;
            }
        )
        showRecentReceive().then(
            (res)=>{
                this.recentreceiver=res.data.data;
            }
        )   
        
    },
    methods: {
        search(){
            searchStudent({"numname":this.numname}).then(
                (res)=>{
                    this.tabledata=res.data.data;
                }
            )
        },
        fetchId(row){
            this.id=row.id;
            this.name="给"+row.name+"一个评价吧!";
            console.log(row.id);
            this.dialogFormVisible=true;
        },

        submit(){
            if(this.tags===''){
                this.$message({
                message: '选择至少一个评价吧!',
              type: 'warning',
            })
            return;
            }
            else{
                addAssessment({"id":this.id,"tags":this.tags}).then(
                    (res)=>{
                        console.log(res.data);
                        if(res.data.data==='success'){
                            this.$message({
                            message: '评价成功',
                            type: 'success',
                            })
                        }
                    }
                )
            }
            this.tags='';
            this.dynamicTags=this.Tags[0];
            this.dialogFormVisible=false;
        },

        handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
        },

        showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
            console.log(_);
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },

      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      changeTagColor(tag){
        if(tag==='海王'||tag==='下头男'||tag==='普信男'){
            return 'danger';
        }
        else if(tag==='土豪'||tag==='数码迷'||tag==='运动健将'||tag==='歌姬'){
            return 'warning';
        }
        else if(tag==='美食家'||tag==='学霸'||tag==='文青'||tag==='游戏狂'){
            return 'success';
        }
        return 'red';
      },
      addTags(tag){
        if(this.tags!=''){
            this.tags+='/';
        }
        this.tags+=tag;
        this.handleClose(tag);
      },
      remake(){
        console.log(this.Tags);
        this.dynamicTags=this.Tags[0];
        this.tags='';
      }
    },
    components:{
        SideBar,
    }
}
</script>

<style scoped>

div{
    box-sizing: border-box;
}
.deliver{
    position: absolute;
    left: 10%;
    margin-top: 30px;
    width: 25%;
    background-color: white;
    border:1px solid #97A8BE;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    padding:10px 10px 10px 10px;
}
.searchcontainer button{
    border: none;
    background-color: rgb(64, 158, 255);
    border-radius: 5px;
}
.searchcontainer button:hover{
    cursor: pointer;
}
.searchcontainer img{
    width: 40px;
    vertical-align: center;
}
.search{
    margin-top: 10px;
    border:none;
    outline: none;
    border-bottom:1px solid #97A8BE;
    font-size:22px;
    color:grey;
    font-weight: 100;
    font-style:italic;
}

.recently{
    margin-top: 20px;
    text-align: left !important;
    font-weight: 700;
    font-size: 20px;
    color:black;
}

.record{
    position:absolute;
    width:40%;
    margin-top:30px;
    left:40%;
    border:1px solid #97A8BE;
    border-radius: 5px;
    max-height: 300px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    padding:10px 10px 10px 10px;
    overflow: auto;
}

.wordcloud{
    position: absolute;
    width: 50%;
    height: 300px;
    margin-top: 400px;
    left: 40%;
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

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
  }
  


  .el-carousel__item:nth-child(2n+1) {
    background-color: gold;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: silver;
  }

  .el-carousel__item:last-child {
    background-color: #B5A642;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
    margin-bottom: 10px;
  }

  .el-tag:hover{
    cursor:pointer;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>