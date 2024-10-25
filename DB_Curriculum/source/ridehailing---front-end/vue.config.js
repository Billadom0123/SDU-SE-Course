const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  configureWebpack: {
    externals: {
      BMap: 'BMap'
    }
  },
  lintOnSave: false,
  transpileDependencies: true,
  publicPath: './',
  devServer: {
    hot: true,
    historyApiFallback: true,
    allowedHosts: "all",
    https: false,
    proxy: {
      '^/api':
      {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
})
