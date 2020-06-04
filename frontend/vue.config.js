module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      '/*': {
        target: 'http://localhost:8080',
        secure: false
      }
    }
  },
  transpileDependencies: ['vuetify']
};
