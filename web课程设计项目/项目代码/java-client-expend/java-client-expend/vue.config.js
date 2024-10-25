module.exports = {
  publicPath: './',
  devServer: {
    hot: true,
    disableHostCheck: true,
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
  outputDir:'C:\\web\\java-client-expend\\dist'
}   