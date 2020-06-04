<template>
  <div>
    Yo, not much to see here right now. The crash list is on its way so take a
    gander
    <router-link to="crashList">here</router-link>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class Home extends Vue {}
</script>
