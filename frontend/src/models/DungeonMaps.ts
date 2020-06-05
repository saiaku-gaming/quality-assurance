export interface Replay {
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

export interface DataPoint {
  x: number;
  y: number;
  value: number;
}

export interface Map {
  name: string;
  className: string;
  xOffset: number;
  yOffset: number;
  xMax: number;
  yMax: number;
  width: number;
  height: number;
}
