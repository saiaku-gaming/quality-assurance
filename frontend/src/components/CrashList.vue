<template>
  <div>
    <v-data-table
            :headers="this.headers"
            :items="this.items"
    ></v-data-table>

  </div>
</template>

<script>
  import axios from "axios";

  export default {
    name: 'CrashList',
    data: ()=> ({
      items: []
    }),
    props: {
      msg: String
    },
    computed: {
      headers: function () {
        const headers = this.items.length === 0 ? [] : Object.keys(this.items[0]);
        return headers.map(header => ({
          text: header,
          align: 'start',
          sortable: true,
          value: header
        }));
      }
    },
    mounted() {
      axios
              .get('/crash/list')
              .then(response => (this.items = response.data))
    }
  }
</script>
