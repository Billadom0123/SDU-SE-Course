<template>
	<view>
		<view class="page-body">
			<view class="page-section page-section-gap">
				<map></map>
			</view>
			<view class="uni-btn-v">
				<button type="primary" @tap="chooseLocation">选择位置</button>
				<!-- <button @tap="clear">清空</button> -->
			</view>
		</view>
	</view>
</template>
 
<script>
	let _this;
export default {
	data() {
		return {
			title: 'map',
			latitude: 39.901,
			longitude: 116.395,
			address: '',
			covers:[{
				id:1, // 使用 marker点击事件 需要填写id
				latitude:39.901,
				longitude:116.395,
				iconPath: '../../static/location.png'
			}, 
			{
				id:2, // 使用 marker点击事件 需要填写id
				latitude: 39.90,
				longitude: 116.39,
				iconPath: '../../static/location.png'
			}]
		};
	},
	mounted() {
		_this = this;
		// this.init();
	},
	methods: {
		init(){
			// console.log("已调用");
			uni.getLocation({
				type: 'wgs84', //返回可以用于uni.openLocation的经纬度
				geocode:true,//是否解析地址信息，仅app
				success: function (res) {
					console.log(res)
					_this.latitude = res.latitude;
					_this.longitude = res.longitude;
					uni.openLocation({
						latitude: res.latitude,
						longitude: res.longitude,
						address:res.address,
						success: function () {
							console.log('success');
						},
						fail:function(){
							console.log("error");
						}
					});
				},
				fail: function(res){
					console.log(res)
				},
				// complete: function(res) {
				// 	console.log(res.errMsg)
				// }
			});
			
			// //实时监听位置变化
			// uni.onLocationChange(function (res) {
			// 	console.log('纬度：' + res.latitude);
			// 	console.log('经度：' + res.longitude);
			// });

		},
		// chooseLocation: function () {
		// 	uni.chooseLocation({
		// 		success: (res) => {
		// 			this.hasLocation = true,
		// 			this.location = this.formatLocation(res.longitude, res.latitude),
		// 			this.locationAddress = res.name,
		// 			console.log('位置名称：' + res.name);
		// 			console.log('详细地址：' + res.address);
		// 			console.log('纬度：' + res.latitude);
		// 			console.log('经度：' + res.longitude);
		// 		}
		// 	})
		// },
		
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


