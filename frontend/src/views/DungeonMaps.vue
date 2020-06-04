<template>
  <div>
    <v-slider v-model="strength" min="1" max="100" label="Strength" />
    {{ this.strength }}
    <div ref="heatmap" class="heatmap" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import h337, { Heatmap } from 'heatmap.js';

interface Replay {
  mapName: string;
  dungeonRuns: DungeonRun[];
}

interface DungeonRun {
  players: ReplayPlayer[];
}

interface ReplayPlayer {
  name: string;
  actions: ReplayAction[];
}

interface ReplayAction {
  action: Action;
  location: Vector;
}

interface Vector {
  x: number;
  y: number;
  z: number;
}

interface DataPoint {
  x: number;
  y: number;
  value: number;
}

type Action = 'MOVE' | 'DEATH';

@Component
export default class DungeonMaps extends Vue {
  private strength = 2;
  private dataPoints: DataPoint[] = [];
  private heatmap: Heatmap<'value', 'x', 'y'> | null = null;

  mounted() {
    this.heatmap = h337.create({
      container: this.$refs.heatmap as HTMLDivElement
    });

    fetch('/api/replay/MissvedenMap')
      .then(response => response.json())
      .then((replays: Replay[]) => {
        this.dataPoints = replays
          .map(replay => {
            return replay.dungeonRuns.map(dungeonRun => {
              return dungeonRun.players.map(player => {
                return player.actions.map(action => {
                  const widthOffset = 8850;
                  const heightOffset = 16910;

                  const widthMax = 24690 + widthOffset;
                  const heightMax = 11460 + heightOffset;

                  const x = Math.round(
                    ((action.location.x + widthOffset) / widthMax) * 1013
                  );
                  const y = Math.round(
                    ((action.location.y + heightOffset) / heightMax) * 858
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
  onStrengthChange() {
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
}
</script>

<style scoped>
.heatmap {
  width: 1013px;
  height: 858px;
  background-image: url('../assets/missveden.png');
}
</style>
