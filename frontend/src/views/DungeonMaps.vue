<template>
  <div>
    <div ref="heatmap" class="heatmap" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import h337 from 'heatmap.js';

@Component
export default class DungeonMaps extends Vue {
  mounted() {
    const heatmap = h337.create({
      container: this.$refs.heatmap as HTMLDivElement
    });

    heatmap.addData({
      x: 10,
      y: 10,
      value: 10
    });
  }
}
</script>

<style scoped>
.heatmap {
  width: 1013px;
  height: 858px;
  background-image: url('../assets/missveden.png');
}
</style>
