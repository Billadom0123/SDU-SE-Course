<template>
	<!-- <router-view/> -->
</template>
<script>
	import routingIntercept from './routerGuard/permission.js'
	export default {
	
		created() {
			// #ifdef APP-PLUS
			plus.navigator.closeSplashscreen(); 
			// #endif 
		},
		onLaunch: function() {
			routingIntercept();
			// console.log('App Launch')
		},
		onShow: function() {
			// console.log('App 开启')
		},
		onHide: function() {
			// console.log('App 关闭')
		},
		onUnload: function(){
			console.log('页面卸载')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
	/* #ifndef APP-PLUS-NVUE */
	@import './common/uni.css';
	
	
	@import "static/colorui/icon.css";
	@import "static/colorui/main.css";
	/* #endif */
	body{
		background: #FFFFFF !important;
	}
</style>
