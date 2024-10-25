<template>
  <div class="navi">
    <el-menu class="header" mode="horizontal" router>
      <el-menu-item index="/" class="menu-item">Home</el-menu-item>
      <template v-for="(v, i) in listA" :key="i">
        <template v-if="v.children.length > 0">
          <el-submenu :index="i.toString()">
            <template #title>
              <span class="menu-item">{{ v.title }}</span>
            </template>
            <template v-for="(item, j) in v.children" :key="j">
              <el-menu-item class="menu-item-two" :index="'/' + item.name">{{
                item.title
              }}</el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item class="menu-item" :index="'/' + v.name">{{
            v.title
          }}</el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Navi",
  components: {},

  data() {
    return {
      listA: [
        {
          name: "BaseTable",
          title: "表格示例",
          children: [
            {
              name: "BaseTableOne",
              title: "表格示例1",
            },
            {
              name: "BaseTabletwo",
              title: "表格示例2",
            },
          ],
        },
        {
          name: "BaseForm",
          title: "表单示例",
          children: [],
        },
      ],
    };
  },
  methods: {},
};
</script>
<style scoped>
.navi {
  border-bottom: 4px solid #c80201;
}
.header {
  display: flex;
  flex-direction: row;
  justify-content: center;
  background-color: #e8e8e8;
}
.menu-item {
  color: #585858 !important;
  font-size: 18px;
  font-weight: bold;
}
</style>
