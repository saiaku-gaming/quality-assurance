import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '@/views/Home.vue';
import CrashList from '@/views/CrashList.vue';
import DungeonMaps from '@/views/DungeonMaps.vue';

Vue.use(VueRouter);

interface QARouteConfig extends RouteConfig {
  icon?: string;
}

const routes: Array<QARouteConfig> = [
  {
    path: '/',
    component: Home,
    name: 'Home',
    icon: 'mdi-home'
  },
  {
    path: '/crashList',
    component: CrashList,
    name: 'Crash List',
    icon: 'mdi-heart-broken'
  },
  {
    path: '/dungeonMaps',
    component: DungeonMaps,
    name: 'Dungeon Maps',
    icon: 'mdi-map'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
