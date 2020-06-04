<template>
  <div>
    <v-data-table
      :disable-pagination="true"
      :headers="this.headers"
      :items="this.items"
    >
      <template v-slot:item.diagnostics="item">
        <div v-if="!item.item.diagnostics">
          No diagnostics found :(
        </div>
        <v-dialog v-else v-model="dialog">
          <template v-slot:activator="{ on }">
            <v-btn color="primary" small v-on="on">Click me!</v-btn>
          </template>
          <v-card>
            <v-card-title class="headline grey lighten-2" primary-title>
              Diagnostics
            </v-card-title>

            <v-card-text>
              <pre>{{ item.item.diagnostics }}</pre>
            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn @click="dialog = false" color="primary" text>
                I accept
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
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
  private dialog = false;

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
