<template>
  <Navi />
  <div class="app-container">table</div>
</template>

<script>
import Navi from "@/components/Navi";
export default {
  name: "BaseTable",
  data() {
    return {
      page: [],
    };
  },
  components: {
    Navi,
  },
  created() {},
  methods: {},
};
</script>