<template>
  <IntroNavi/>
  <div style="padding: 50px 0 30px;text-align: center;">
    <h2 >My Favourite</h2>
    <h2>我的喜好</h2>
  </div>


  <el-row style="text-aligh:center">

    <el-col :span="6">
      <div class="card">
        <div style="font-size: 30px;font-family: Gabriola">我最喜欢的动漫</div>
        <div>《进击的巨人》</div>
        <div>类型：热血、悬疑、动作、奇幻</div>
        <div>作者：谏山创</div>
        <img src="../../assets/comic.jpg" alt="进击的巨人" style="width:200px;padding-top: 50px">

      </div>
    </el-col>
    <el-col :span="6">
      <div class="card">
        <div style="font-size: 30px;font-family: Gabriola">我最喜欢的游戏</div>
        <div>《明日方舟》</div>
        <div>类型：塔防、剧情、二次元</div>
        <el-link href="https://ak.hypergryph.com/" target="_blank">明日方舟官网</el-link>
        <img src="../../assets/mrfz.webp" alt="明日方舟" style="width:200px;padding-top: 50px">

      </div>
    </el-col>
    <el-col :span="6">
      <div class="card">
        <div style="font-size: 30px;font-family: Gabriola">我最喜欢的小说</div>
        <div>《三体》</div>
        <div>类型：科幻</div>
        <div>作者：刘慈欣</div>
        <img src="../../assets/三体.jpg" alt="三体" style="width:200px;padding-top: 50px">

      </div>
    </el-col>
    <el-col :span="6">
      <div class="card">
        <div style="font-size: 30px;font-family: Gabriola">我最喜欢的歌手</div>
        <div>姓名：周杰伦</div>
        <div>代表作：《晴天》，《七里香》</div>
        <div></div>
        <img src="../../assets/jaychou.jpg" alt="三体" style="width:200px;padding-top: 50px">

      </div>
    </el-col>

  </el-row>
</template>

<script setup>
// import {reactive, ref} from "vue";
import IntroNavi from "../../components/IntroNavi.vue";
// import comic from "../../assets/comic.jpg"

</script>

<style scoped>
.card{
  height:300px;
  border-width:1px ;
  border-style: solid;
  width:250px;
  margin-left:100px;
  text-align: center;
  box-shadow: 8px 8px 5px #888888;
}
</style>