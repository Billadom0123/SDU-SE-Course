<template>
<SideBar/>
<div class="position">
    <a href="javascript:;" @click="gotoIndex">首页</a>
    /
    <a href="javascript:;" >我的主页</a>
    /
    <a href="javascript:;" >我的博客</a>
</div>

<el-dialog v-model="dialogFormVisible" title="撰写博客">
    <el-form :model="form">
        <el-form-item label="标题" :label-width="formLabelWidth">
            <el-input v-model="form.title" autocomplete="off" placeholder="来个高大上的标题"/>
          </el-form-item>
        <el-form-item label="简介" :label-width="formLabelWidth">
            <el-input
            v-model="form.introduction"
            :rows="2"
            type="textarea"
            placeholder="简介请尽量短"
          />
        </el-form-item>
        <el-form-item label="内容" :label-width="formLabelWidth">
            <el-input
            v-model="form.article"
            :rows="5"
            type="textarea"
            placeholder="编写博客吧"
          />
        </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="edit">
          提交
        </el-button>
      </span>
    </template>
  </el-dialog>

<!-- 主要内容区域 -->
<div class="main" ref="main">

    <!-- 博客头部图片 -->
    <img src="../../assets/blogtop.gif" alt="" class="blogtop">

    <!-- 博主模块 -->
    <div class="blogger" ref="blogger">

        <!-- 头像位置 -->
        <div class="avatar">
            <img :src="this.imageUrl" alt="" class="avatarimg">
        </div>

        <!-- 用户信息 -->
        <div class="userinfo">
        <!-- 用户名 -->
        <div class="username">
            {{blogger.name}}
        </div>
        <!-- 博客信息 -->
        <div class="info">
            {{blogger.blogNum}}&nbsp;篇原创博客
        </div>
        </div>

        <!-- 个人简介 -->
        <div class="introduce" ref="introduce">
            个人简介:
            <br>
            <br>
            {{blogger.introduce}}
        </div>

    </div>

    <!-- 博客文章部分 -->
    <div class="blogs" ref="blogs">
        <br>
        博客
        <el-button type="primary" icon="el-icon-edit" circle class="edit" @click="this.dialogFormVisible=true"></el-button>
        <template v-for="(blog,i) in blogs" :key="i">
            <el-divider></el-divider>
                <div class="blog" @click="gotoArticle(blog.id)" ref="blog">
                    <span >
                        {{blog.title}}
                        
                    </span>
                    <!-- <el-button type="danger" icon="el-icon-delete" circle class="delete"
                        @click="delete(blog.id)"></el-button> -->
                    <br><br>
                    简介:&nbsp;{{blog.introduction}}
                    <br><br>
                    发布于:&nbsp;{{blog.date}}
                </div>
        </template>
    </div>
</div>
</template>

<script>
import SideBar from '@/components/SideBar';
import { getBlogger,getBlogs,fetchBlogId,getPersonImage,addBlog } from '../../service/genServ.js';
export default{
    name: "MyBlog",
    data() {
        return {
            form:{
                title:"",
                introduction:"",
                article:"",
            },
            blogger:{
                name:'lyh',
                blogNum:1,
                introduce:'山大软院在读,纯小白;',
            },
            blogs:[
                {
                    id:1,
                    title:'上台阶的两种解决方式',
                    introduction:'第二种方式，递归。每一次可以从1到最大步长中选择一个步长，在本题条件下即从上1级和上2级中选取一个。随后重复这个动作直至恰好走完规定的总级数，即20级。由此可以选用递归。注意两点，第一点是要保证“恰好”走20级，因此当最大步长大于剩余级数时，需要更新最大步长。第二点是我采用的递归方法导致的，我采用了数组来存储每一步走过的步长，由于这是个给定的台阶数20，因此顶多走20次就到了，开个长度20的数组即可。但每次递归后会在数组中残留以往递归的数据。例如第一次递归结束后数组中有20个1，随后更改第19个1为2，这样总和就变成了21，直接爆20了，这是因为没有清空最后一个1导致的。由是需要加入一个clear()方法清除以往递归产生的数据，让数组回到之前的分支中。',
                    date:'2022-10-26'
                },
            ],
            imageUrl:'',
            dialogFormVisible:false,
        };
    },
    methods: {
        gotoIndex(){
      this.$routers.push({
        path:"index",
      })
    },
    gotoArticle(BlogId){
            //console.log(BlogId);
            //console.log(this.blogs[0].id);
            fetchBlogId({"id":BlogId});
            this.$router.push({
                path:'Blogs',
            })
    },
    edit(){
        this.dialogFormVisible=!this.dialogFormVisible;
        addBlog(this.form).then(
            (res)=>{
                if(res.data.msg==null){
                    this.$message({
                    message: '添加成功',
                    type: 'success',
                    })
                }
                location.reload();
            }
        )
    },
    // delete(id){
    //     console.log(111);
    //     deleteBlog({"id":id}).then(
    //         (res)=>{
    //             if(res.data.msg==null){
    //                 this.$message({
    //                 message: '添加成功',
    //                 type: 'success',
    //                 })
    //                 location.reload();
    //             }
    //         }
    //     )
    // }

    },
    mounted() {
        getPersonImage().then(
            (res)=>{
                this.imageUrl=res.data.data;
            }
        )
        //显示博主信息
        getBlogger().then(
            (res)=>{
                this.blogger=res.data.data[0];
            }
        )
        //显示博客信息
        getBlogs().then(
            (res)=>{
                //console.log(res.data.data);
                this.blogs=res.data.data;
                var size = this.blogs.length;
                var main = this.$refs.main;
                main.style.height=size*this.$refs.blogs.offsetHeight+'px';
            }
        )
        //让blogger的高度自适应
        var blogger=this.$refs.blogger;
        var introduce = this.$refs.introduce;
        if(blogger.offsetHeight<=introduce.offsetHeight){
            blogger.style.height=introduce.offsetHeight+50+'px';
        }
        //让blogs的位置自适应
        var blogs=this.$refs.blogs;
        var distance = blogger.offsetHeight+170+'px';
        blogs.style.top=distance;
        //让main的高度自适应
        // var main = this.$refs.main;
        // main.style.height=document.body.scrollHeight+document.body.scrollTop+30+'px';
    },
    component: {
        SideBar,
    },
    components: { SideBar }
}
</script>

<style scoped>

.main{
    background:url("../../assets/blogbg.gif");
}

.edit{
    position:absolute;
    right: 20px;
}

.delete{
    position:absolute;
    right:20px;
}

div{
    box-sizing: border-box;
}
a{
    text-decoration: none;
    color: black;
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

.main{
    width: 100%;
    height: 100%;
}

.blogger{
    position: absolute;
    width: 90%;
    left: 50%;
    transform: translate(-50%,100px);
    background-color: white;
    border-radius: 5px;
}
.blogtop{
    position:absolute;
    display: block;
    width: 135%;
    left: 50%;
    transform: translateX(-50%);
}
.avatar{
    float: left;
    width: 100px;
    height: 100px;
    border-radius: 50px;
    background-color: pink;
    margin-left: 30px;
    transform: translateY(-10px);
}
.avatarimg{
    width: 100px;
    height: 100px;
    border-radius: 50px;
}
.userinfo{
    float: left;
    display: flex;
    flex-direction: column;
    margin-bottom: 50px;
}
.username{
    margin-left: 10px;
    margin-top: 10px;
    float: left;
    height: 50px;
    line-height: 50px;
    font-weight: 700;
    font-size:22px;
    text-align: left;
}
.info{
    float: left;
    margin-left: 10px;
    line-height: 50px;
    font-size:18px;
    height: 50px;
}
.introduce{
    margin-top: 30px;
    position: absolute;
    right:200px;
    color:#555666;
    width: 300px;
    text-align: left !important;
    margin-bottom: 20px;
}
.blogs{
    position: absolute;
    width: 90%;
    left: 50%;
    transform: translateX(-50%);
    background-color: white;
    color: black;
    font-size: 20px;
    font-weight: 400;
    text-align: left;
    padding-left: 20px;
    padding-right: 20px;
    padding-bottom: 30px;
    border-radius: 5px;
    margin-bottom:15px;
}
.blog{
    font-size:14px;
    color:#555666;
}
.blog span{
    font-size:20px;
}
.blog:hover{
    cursor: pointer;
    color: rgb(252, 82, 49);
}

.blog span:hover{
    cursor: pointer;
    color: rgb(252, 82, 49);
}

</style>