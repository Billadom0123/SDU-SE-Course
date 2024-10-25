<template>
    <SideBar/>
    <div class="position">
        <a href="javascript:;" @click="gotoIndex">首页</a>
        /
        <a href="javascript:;" >我的主页</a>
        /
        <a href="javascript:;" >博客</a>
        /
        <a href="javascript:;">{{blogs.title}}</a>
    </div>
    <!-- 主要内容区域 -->
    <div class="main" ref="main">
        <!-- 作者信息与文章简介区域 -->
        <div class="blogger" ref="blogger">
            <div class="user" ref="user">
                <!-- 头像区域 -->
                <div class="avatar">
                    <img src="../../assets/blogger.jpg" alt="" class="avatarimg">
                </div>
                <!-- 作者信息 -->
                <div class="userinfo">
                    {{blogger}}
                </div>
            </div>
            
            <!-- 文章简介 -->
            <div class="introduction" ref="introduction">
                文章简介:
                <br>
                {{blogs.introduction}}
            </div>
                    <!-- 其他博客 -->
        <div class="other" ref="other">
            他的其他博客:
            <el-divider/>
            <template v-for="(other,i) in others" :key="i">
                <div class="otherlink" @click="gotoArticle(other.id)">《{{other.title}}》</div>
                <br>
            </template>
        </div>
        </div>

        <!-- 正文内容 -->
        <div class="article" ref="article">
            <el-button type="danger" icon="el-icon-delete" circle class="delete"
                        @click="del"></el-button>
            <div style="font-weight:700;font-size:22px">
                {{blogs.title}}
            </div>
            <br>
            <div style="font-weight:100px;font-size:12px;color:#97A8BE">
                发布于:{{blogs.date}}
            </div>
             
            <el-divider/>
            &nbsp;&nbsp;&nbsp;&nbsp;{{blogs.article}}
        </div>
    </div>
</template>

<script>
import SideBar from '@/components/SideBar';
import { showBlog,showOther,fetchBlogId, deleteBlog} from '../../service/genServ.js';
export default{
    name:'Blogs',
    data() {
        return {
            blogger:'lyh',
            blogs:{
                title:'',
                introduction:'',
                article:'',
                date:''
            },
            others:[
            ]
            }
        
    },
    methods: {
        gotoArticle(BlogId){
            fetchBlogId({"id":BlogId});
            location.reload();
        },
        del(){
            deleteBlog({"id":this.blogs.id}).then(
                (res)=>{
                    if(res.data.msg==null){
                        this.$message({
                        message: '删除成功',
                        type: 'success',
                        })
                        this.$router.push({
                            path:"MyBlog"
                        })
                    }
                }
            )
        }
    },
    mounted() {
        showBlog().then(
            (res)=>{
                this.blogs=res.data.data[0];
                console.log(this.blogs);
            }
        )
        showOther().then(
            (res)=>{
                this.others=res.data.data;
                console.log(res.data.data);
                var main=this.$refs.main;
                main.style.height=this.others.length*this.$refs.other.offsetHeight+this.$refs.introduction.offsetHeight+'px';
                console.log(this.others.length*this.$refs.other.offsetHeight+this.$refs.introduction.offsetHeight+this.$refs.blogger.offsetHeight);
                console.log(main.offsetHeight);
            }
        )
         var article = this.$refs.article;
         //article高度不能太小
         if(article.offsetHeight<700){
             article.style.height=700+'px';
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

.delete{
    position:absolute;
    right: 20px;
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
    min-height: 750px;
    background: url(../../assets/blogbg.gif);
}
.blogger{
    margin-top: 30px;
    position: absolute;
    left: 10%;
    width: 25%;
    background-color: white;
    border-radius: 5px;
}
.user{
    display: flex;
    flex-direction: row;
}
.avatar{
    margin-top: 10px;
    margin-left: 10px;
    width: 100px;
    height: 100px;
    border-radius: 50px;
    background: pink;
}
.avatarimg{
    width: 100px;
    height: 100px;
    border-radius: 50px;
}
.userinfo{
    margin-left: 20px;
    margin-right: 20px;
    transform: translateY(50px);
    font-weight: 700;
    font-size: 20px;
}
.introduction{
    color: #97A8BE;
    
    width:90%;
    text-align: left !important;
    margin-left: 20px;
    margin-right: 20px;
    
}
.other{
    position: absolute;
    margin-top: 10px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 20px;
    padding-right: 20px;
    width: 100%;
    background-color: #fff;
    border-radius: 5px;
    text-align: left;
}
.article{
    position: absolute;
    padding-top: 20px;
    margin-top: 30px;
    margin-left: 10px;
    padding-bottom: 30px;
    left: 35%;
    width: 60%;
    background-color: white;
    border-radius: 5px;
    padding-left: 20px;
    padding-right: 20px;
    text-align: left !important;
}
.otherlink{
    color: #97A8BE;
}
.otherlink:hover{
    cursor: pointer;
    color: rgb(252, 82, 49);
}
</style>