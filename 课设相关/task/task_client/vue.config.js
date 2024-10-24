// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
module.exports = {
  publicPath: './',
  devServer: {
    hot: true,
    historyApiFallback: true,
    allowedHosts: "all",
    https: false,
    proxy: {
      '^/api':
      {
        target: 'http://localhost:9090',
        changeOrigin: true
      },

    },
  },
  //outputDir: 'E:\\NewTeachFrame\\java-client\\dist',
  //  outputDir: 'C:\\teach\\web\\java-server\\src\\main\\resources\\public'
  outputDir:'C:\\workplace\\task\\task_client\\dist'
}   