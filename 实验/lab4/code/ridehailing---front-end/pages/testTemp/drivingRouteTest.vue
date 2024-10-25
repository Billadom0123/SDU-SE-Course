<template>
	<view>
		<map 
		:latitude="latitude"
		:longitude="longitude"
		:polyline="polyline"></map>
	</view>
</template>

<script>
	let _this;
	import {getDrivingRoute} from '../../service/genService.js'
	import {formatLocation} from '../../util/formatter.js'
	export default {
		data() {
			return {
				latitude:'',
				longitude:'',
				origin:{
					longitude:117.065168,
					latitude:36.680733
				},
				destination:{
					longitude:117.140113,
					latitude:36.667385
				},
				wayPoints:{
					longitude:117.065168,
					latitude:36.680733
				},
				polyline:[
					{
						points:[
							{
								longitude:117.065168,
								latitude:36.680733
							},
							{
								longitude:117.140113,
								latitude:36.667385
							},
							{
								longitude:117.065168,
								latitude:36.680733
							}
						],
						color: "#000000",
						width: 6,
					}
				],
			}
		},
		onLoad() {
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
				},
			})
		},
		mounted() {
			this.getRoute();
			console.log(this.polyline)
		},
		methods: {
			getRoute(){
				const that = this
				getDrivingRoute(this.origin,this.destination,this.wayPoints).then((res)=>{
					  that.res = res;
					  const data = res.data.route
					  var pois = []
					  if (data.paths && data.paths[0] && data.paths[0].steps) {
						var steps = data.paths[0].steps;
						for (var i = 0; i < steps.length; i++) {
						  //将每一步的数据放到points数组中
						  var poLen = steps[i].polyline.split(";")
						  for (var j = 0; j < poLen.length; j++) {
							pois.push({
							  longitude: parseFloat(poLen[j].split(",")[0]),
							  latitude: parseFloat(poLen[j].split(",")[1]),
							})
						  }
						}
					  }
					  // console.log(that.polyline[0].width)
					  that.polyline[0].points=pois
				})
			}
		}
	}
</script>

<style>

</style>
