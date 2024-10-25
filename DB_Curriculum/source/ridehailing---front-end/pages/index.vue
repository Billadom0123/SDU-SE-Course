<template>
	<br>
	<view class="margin-xs">
		<view class="bg-white radius-lg" style="min-height: 90vh;">
			<view class="padding-tb padding-lr">
				<text class="text-xxl text-black">欢迎使用</text>
				<text class="text-xxl text-black text-bold"> 滴答网约车</text>
				<view class="text-df padding-top-sm">便携出行，安全经济</view>
			</view>
			<view class="bg-white radius-lg margin-tb-xl">
				<view class="bg-img padding-xs margin-xs radius-lg"
					style="background-image: url('/static/images/design06.png');height: 600rpx;">
				</view>
			</view>
			<view class="padding-bottom-xxl padding-top padding-lr">
				<view class="flex flex-column margin-sm">
					<button class="cu-btn xxl bg-gradual-blue round" @click="login">
						<text class="icon-wechat"
							:decode="true">&nbsp;</text>登录</button>
				</view>
				<view class="footer">
					<!-- 使用的是navgitor导航栏跳转，具体设置在page.json中 -->
					<navigator url="login/forget" open-type="navigate">找回密码</navigator>
					<text>|</text>
					<navigator url="login/register" open-type="navigate">注册账号 </navigator>
					<!-- <text>|</text> -->
					<!-- <navigator url="login/maps" open-type="navigate">地图1</navigator> -->
					<!-- <text>|</text> -->
					<!-- <navigator url="login/maps2" open-type="navigate">地图2</navigator> -->
				</view>
			</view>
		</view>
	</view>
<!-- 	<navigator url="customer/selectPosition" open-type="navigate">地图-乘客</navigator>
	<navigator url="driver/startBusiness" open-type="navigate">地图-司机</navigator>
	<navigator url="testTemp/drivingRouteTest" open-type="navigate">路线规划</navigator> -->
	<!-- <navigator url="customer/select" open-type="navigate">地图-乘客nvue</navigator> -->

</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			login(){
				// this.$router.push('/login')
				uni.navigateTo({
					url: 'login/login',
				});
				// uni.navigateBack({
				// 	delta: 1,
				// });
			}
		}
	}
</script>

<style>
	@import url("../components/watch-login/css/icon.css");
	@import url("./login/css/main.css");
</style>