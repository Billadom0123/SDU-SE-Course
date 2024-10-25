<template v-if="username && username.length> 0">
  <div class="welcome">
    <Navi />
  </div>
</template>

<script>
import Navi from "@/components/Navi.vue";
import { mapState } from "vuex";

export default {
  name: "Welcome",
  components: {
    Navi,
  },
  computed: {
    ...mapState(["username"]),
  },
};
</script>
