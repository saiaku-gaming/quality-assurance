<template>
  <v-app id="inspire">
    <v-app id="inspire">
      <v-navigation-drawer
              v-model="drawer"
              :clipped="$vuetify.breakpoint.lgAndUp"
              app
      >
        <v-list dense>
          <v-list-item link
                       v-for="route in this.$router.options.routes"
                       :key="route.path"
                       @click="goTo(route.name)"
          >
            <v-list-item-action>
              <v-icon>{{route.icon}}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>{{route.name}}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>

      <v-app-bar
              :clipped-left="$vuetify.breakpoint.lgAndUp"
              app
              color="indigo"
              dark
      >
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
        <v-toolbar-title>Valhalla Quality Assurance</v-toolbar-title>
      </v-app-bar>

      <v-content>
        <v-container
                class="fill-height"
                fluid
        >
          <router-view></router-view>
        </v-container>
      </v-content>
      <v-footer
              color="indigo"
              app
      >

      </v-footer>
    </v-app>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  methods: {
    goTo(route){
      this.$router.push({ name: route});
    }
  },
  data() {
    return {
      drawer: null
    }
  }
};
</script>
