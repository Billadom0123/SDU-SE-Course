<template>
	<view>
		<view class="uni-padding-wrap">
			<view style="background:#FFFFFF; padding:40rpx;">
				<view class="uni-hello-text uni-center">当前位置信息</view>
				<block v-if="hasLocation === false">
					<view class="">未选择位置</view>
				</block>
				<block v-if="hasLocation === true">
					<view class="uni-hello-text uni-center" style="margin-top:10px;">
						{{locationAddress}}
					</view>
					<view class="uni-h2 uni-center uni-common-mt">
						<text>{{location.longitude[0]}}°{{location.longitude[1]}}′</text>
						<text>\n{{location.latitude[0]}}°{{location.latitude[1]}}′</text>
					</view>
				</block>
				<view class="">
					
				</view>
			</view>
			<view class="uni-btn-v">
				<button type="primary" @tap="chooseLocation">选择位置</button>
				<button @tap="clear">清空</button>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				hasLocation: false,
				location: {},
				locationAddress: ''
			}
		},
		methods: {
			chooseLocation: function () {
				// console.log("choose-before");
				uni.chooseLocation({
					success: (res) => {
						this.hasLocation = true,
							this.location = this.formatLocation(res.longitude, res.latitude),
							this.locationAddress = res.name
					}
				})
			},
			clear: function () {
				this.hasLocation = false
			},
			formatLocation(longitude, latitude) {
				if (typeof longitude === 'string' && typeof latitude === 'string') {
					longitude = parseFloat(longitude)
					latitude = parseFloat(latitude)
				}
				// longitude = longitude.toFixed(2)
				// latitude = latitude.toFixed(2)
				return {
					longitude: longitude.toString().split('.'),
					latitude: latitude.toString().split('.')
				}
			}
		}
	}
</script>
 
<style lang="scss">
	
</style>

<!-- <template>
	<map id="myMap" style="width:100% height:780px;"
		:latitude="latitude"
		:longitude="longitude"
		:markers="markers" 
		:polyline="polyline"
		show-location="true"
		scale="20">
		<cover-image class="returnBack" src="../../static/back.png" @click="goBack()"></cover-image>
		</map>
</template>

<script>
	export default {
		data() {
			return {
				longitude: 121.44820869,
				latitude: 37.48330837,
				scale: 16,
				polyline: [{
					points: [],
					color:"#00aa00",
					arrowLine: true,
					width: 20,
					dottedLine: true,
				}],
				markers: [{
						id: 0,
						longitude: 121.44577861
						latitude: 37.48205268,
						iconPath:'../../static/locaton.png',
					},
					{
						id: 1,
						longitude: 121.44828869.
						latitude: 37.48338837,
						iconPath:'../../static/locaton.png',
					}
				]
			},
		},
		run(){
			this.timer=setInterval(()=>{
				this.
			})
		}
		methods:{
			//位置授权
			checkOpenGPsServiceByAndroid() {
				this.location = false;
				let system = uni.getSystemInfoAsync(); // 获取系统信息
				if (system.platform === 'android') {
					var context = plus.android.importclass( 'android.content.Context');
					var locationManager = plus.android.importclass('android.location.LocationManager')
					var main = plus.android.runtimeMainActivity();
					var mainSvr = main.getSystemService(context.LOCATION SERVICE);
					if (!mainSvr.isProviderEnabled(locationManager.GPS PROVIDER)){}
						uni.showModal({
						title:"提示',
						content:'请打开系统定位功能',
						showCancel: false,// 不显示取消按钮
						success() {
							if (!mainsvr,isProviderEnabled(locationManager.GPS PROVIDER)){
								var Intent = plus.android.importclass('android,content,Intent');
								var Settings = plus.android.importclass('android.provider.Settings');
								var intent = new IntentSettings.ACTION LOCATION SOURCE SETTINGS);
								main.startActivity(intent); // 打开系统设置GPS服务页面
							} else{
								console.log('GPS功能已开启');
							}
						}
					});
				} else {
					this.location = true
				}
			}
		},
	}
</script>
 -->
