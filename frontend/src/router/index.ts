import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../components/Home.vue';
import CrashList from '../components/CrashList.vue';

Vue.config.productionTip = false;

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
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
