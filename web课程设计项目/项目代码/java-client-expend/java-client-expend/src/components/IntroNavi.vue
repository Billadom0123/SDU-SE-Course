<template>
  <el-row class="tac">
    <el-col >
      <el-menu
          :default-active="$route.name"
          class="el-menu-vertical-demo"
          :router="true"
          @open="handleOpen"
          @close="handleClose"
          mode="horizontal"

      >

          <!-- <div style="justify-content: center;display:flex;margin:auto 30px">马亿祥个人介绍</div> -->

          <el-menu-item index="Home" route="/Home">
            <i class="el-icon-discover"></i>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="Skill" route="/Skill">
            <i class="el-icon-document"></i>
            <span>我的技能</span>
          </el-menu-item>
          <el-menu-item        index="Contact"  route="/Contact">
            <i class="el-icon-setting"></i>
            <span>联系我</span>
          </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
  <div class="header_pic">
    <img src="../assets/Intro_2.webp" width="100%">
  </div>
</template>

<!-- <script setup>
// import {reactive, ref} from "vue";
</script> -->

<!-- <script src="//cdn.jsdelivr.net/npm/@element-plus/icons-vue"></script> -->

<style scoped>
.header_pic{
  width:100%;
}
.header_pic img{
width:100%;
  height:300px;
}
</style>