import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import CrashList from "./components/CrashList";
import Home from "./components/Home";

Vue.config.productionTip = false

Vue.use(VueRouter)

const routes = [
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
]

const router = new VueRouter({routes})

new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
