<template>
<SideBarForAdmin/>
<div class="position">
    <a href="javascript:;" >首页</a>
</div>
</template>

<script>
import SideBarForAdmin from "@/components/SideBarForAdmin";
export default{
    name:'indexForAdmin',
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
</style>