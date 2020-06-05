<template>
  <div>
    <v-select
      :items="maps.map(map => ({ text: map.name, value: map }))"
      label="Map"
      v-model="selectedMap"
      outlined
    />
    <v-slider v-model="strength" min="1" max="100" label="Strength" />
    {{ this.strength }}
    <div ref="heatmap" :class="selectedMap.className" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import h337, { Heatmap } from 'heatmap.js';
import { Replay, DataPoint, Map } from '@/models/DungeonMaps';

@Component
export default class DungeonMaps extends Vue {
  private strength = 2;
  private dataPoints: DataPoint[] = [];
  private heatmap: Heatmap<'value', 'x', 'y'> | null = null;
  private maps: Map[] = [
    {
      name: 'MissvedenMap',
      className: 'missveden-map',
      xOffset: 8850,
      yOffset: 16910,
      xMax: 24690 + 8850,
      yMax: 11460 + 16910,
      width: 1013,
      height: 858
    },
    {
      name: 'fredstorpMap',
      className: 'fredstorp-map',
      xOffset: 10370,
      yOffset: 10370,
      xMax: 14360 + 10370,
      yMax: 7060 + 10370,
      width: 1220,
      height: 860
    },
    {
      name: 'HjuoMap',
      className: 'hjuo-map',
      xOffset: 20390,
      yOffset: 32430,
      xMax: 14110 + 20390,
      yMax: 21950 + 32430,
      width: 599,
      height: 944
    }
  ];
  private selectedMap: Map = this.maps[0];

  mounted() {
    this.heatmap = h337.create({
      container: this.$refs.heatmap as HTMLDivElement
    });

    this.fetchReplays();
  }

  private fetchReplays() {
    const {
      xOffset,
      yOffset,
      xMax,
      yMax,
      width,
      height,
      name
    } = this.selectedMap;

    fetch(`/api/replay/${name}`)
      .then(response => response.json())
      .then((replays: Replay[]) => {
        this.dataPoints = replays
          .map(replay => {
            return replay.dungeonRuns.map(dungeonRun => {
              return dungeonRun.players.map(player => {
                return player.actions.map(action => {
                  const x = Math.round(
                    ((action.location.x + xOffset) / xMax) * width
                  );
                  const y = Math.round(
                    ((action.location.y + yOffset) / yMax) * height
                  );

                  return { x, y, value: this.strength };
                });
              });
            });
          })
          .flat()
          .flat()
          .flat();
        this.heatmap?.setData({
          max: this.dataPoints.length,
          min: 0,
          data: this.dataPoints
        });
      });
  }

  @Watch('strength')
  private onStrengthChange() {
    this.dataPoints = this.dataPoints.map(dataPoint => {
      return {
        ...dataPoint,
        value: this.strength
      };
    });

    this.heatmap?.setData({
      max: this.dataPoints.length,
      min: 0,
      data: this.dataPoints
    });
  }

  @Watch('selectedMap')
  private onSelectedMapChagne() {
    this.heatmap?.setData({
      max: 0,
      min: 0,
      data: []
    });
    this.fetchReplays();
  }
}
</script>

<style scoped>
.missveden-map {
  width: 1013px;
  height: 858px;
  background-image: url('../assets/missveden.png');
}

.fredstorp-map {
  width: 1220px;
  height: 860px;
  background-image: url('../assets/fredstorp.png');
}

.hjuo-map {
  width: 599px;
  height: 944px;
  background-image: url('../assets/hjuo.png');
}
</style>
