
<template>
  <Navi />
  <div
    class="home"
    style="
      margin-top: 50px;
      display: flex;
      justify-content: center;
      align-items: center;
    "
  >
    <el-button size="primary" class="deleteButton" @click="logout()"
      >退出登录</el-button
    >
  </div>
</template>

<script>
import Navi from "@/components/Navi";
import router from "@/router/index.js";
import { downloadPost } from "@/service/genServ.js";
export default {
  name: "Home",
  components: {
    Navi,
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      router.push("/login");
    },
    downPDF() {
      downloadPost(
        "http://localhost:9090/api/teach/printStudentIntroduce",
        "个人简历"
      );
    },
  },
};
</script>
