<template>
  <div>
    <div ref="heatmap" class="heatmap" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import h337 from 'heatmap.js';

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

type Action = 'MOVE' | 'DEATH';

@Component
export default class DungeonMaps extends Vue {
  mounted() {
    const heatmap = h337.create({
      container: this.$refs.heatmap as HTMLDivElement
    });

    fetch('/api/replay/MissvedenMap')
      .then(response => response.json())
      .then((replays: Replay[]) => {
        const things = replays
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

                  return { x, y, value: 2 };
                });
              });
            });
          })
          .flat()
          .flat()
          .flat();
        heatmap.setData({
          max: things.length,
          min: 0,
          data: things
        });
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
