<template>
  <Navi />
  <div class="app-container">
    <div class="table_center">
      <table class="content">
        <tr>
          <td
            colspan="6"
            style="font-size: 24px; font-weight: bold; color: #304156"
          >
            {{ formName }}
          </td>
        </tr>
        <tr style="height: 40px">
          <td colspan="1" width="200">文本</td>
          <td colspan="5" style="font-size: 14px">纯文本</td>
        </tr>

        <tr style="height: 40px">
          <td colspan="1" width="200">输入</td>
          <td colspan="5">
            <el-input
              v-model="form.inputA"
              placeholder="请输入"
              style="width: 90%"
            />
          </td>
        </tr>
        <tr style="height: 40px">
          <td colspan="1" width="200">选择</td>
          <td colspan="5">
            <el-select
              v-model="form.selectA"
              placeholder="请选择"
              style="width: 90%"
            >
              <el-option
                v-for="items in list"
                :key="items.value"
                :label="items.label"
                :value="items.value"
              >
              </el-option>
            </el-select>
          </td>
        </tr>
        <tr style="height: 40px">
          <td colspan="1" width="200">日期</td>
          <td colspan="5">
            <el-date-picker
              style="width: 90%"
              v-model="form.dateA"
              type="date"
              placeholder="选择日期时间"
            >
            </el-date-picker>
          </td>
        </tr>
      </table>
    </div>

    <div class="centerButton">
      <el-button size="mini" class="rowButton" @click="doSumit()"
        >提交</el-button
      >
    </div>
  </div>
</template>

<script>
import Navi from "@/components/Navi";
export default {
  name: "BaseForm",
  data() {
    return {
      form: {
        textA: "",
        inputA: "",
        selectA: [{'label': 'a', 'value': 'b'}],
        dateA: "",
      },
      list: [{'label': 'a', 'value': 'b'}],
      formName: "表单名",
    };
  },
  components: {
    Navi,
  },
  created() {},
  methods: {},
};
</script>