<template>
	<view>
		<view>
			<button type="primary" @click='updateLocation'>更新位置</button>
		</view>
	</view>
</template>

<script>
	let _this;
	import {updateDriverLocation} from '../../util/wsUpdateLocation.js'
	import {androidWSPrefix} from '../../config/systemConfig.js'
	import {formatLocation} from '../../util/formatter.js'
	export default {
		data() {
			return {
				latitude:'',
				longitude:'',
				socketOpen:false
			}
		},
		mounted() {
			_this = this;
			//获取司机当前位置
			uni.getLocation({
				type: 'gcj02',
				geocode:true,
				success: function(res) {
					var position = formatLocation(res.latitude,res.longitude)
					_this.latitude = position.latitude
					_this.longitude = position.longitude
				},
				fail: function(res){
					console.log(res)
				}
			})
			uni.connectSocket({
				url:androidWSPrefix+'/api/DispatchOrderServer/'+uni.getStorageSync("userId")
			})
			uni.connectSocket({
				url:androidWSPrefix+'/api/RealTimeLocationUpdateServer'
			})
			uni.onSocketOpen((res)=>{
				console.log(res)
			})
		},
		methods: {
			updateLocation(){
				var res = updateDriverLocation(this.latitude,this.longitude)
				res.then((res)=>{
					console.log(res)
				})
			}
		}
	}
</script>

<style>

</style>
