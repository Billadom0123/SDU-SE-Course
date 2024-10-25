<template>
	<view>
		<button type="primary" @click='updateLocation'>更新位置</button>
	</view>
</template>

<script>
	let _this
	import {updateDriverLocation} from '../../util/wsUpdateLocation.js'
	import {connectToWSServer} from '../../util/websocketConnector.js'
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
			let uid = uni.getStorageSync("userId")
			connectToWSServer(androidWSPrefix+'/api/RealTimeLocationUpdateServer')
		},
		methods: {
			updateLocation(){
				var res = updateDriverLocation(this.longitude,this.latitude)
				res.then((res)=>{
					console.log(res)
				})
			}
		}
	}
</script>

<style>

</style>
