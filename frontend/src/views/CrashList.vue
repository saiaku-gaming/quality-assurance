<template>
  <div>
    <v-data-table :headers="this.headers" :items="this.items"></v-data-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import axios from 'axios';

interface Header {
  text: string;
  align: string;
  sortable: boolean;
  value: string;
}

@Component
export default class CrashList extends Vue {
  private items: object[] = [];

  get headers(): Header[] {
    const headers: string[] =
      this.items.length === 0 ? [] : Object.keys(this.items[0]);
    return headers.map(header => ({
      text: header,
      align: 'start',
      sortable: true,
      value: header
    }));
  }

  mounted() {
    axios.get('/api/crash/list').then(response => (this.items = response.data));
  }
}
</script>
