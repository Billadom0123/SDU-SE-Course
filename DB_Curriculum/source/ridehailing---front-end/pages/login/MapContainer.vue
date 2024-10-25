<template>
	
</template>

<script>
	</script>

<!-- <template>
	<div id="container" style="width: 100%; height: 600px;"></div>
	
</template>

<script>
	import AMapLoader from '@amap/amap-jsapi-loader';
	import { shallowRef } from '@vue/reactivity';
	export default{
		mounted(){
		  //DOM初始化完成进行地图初始化
		  this.initMap();
		},
		setup(){
		  const map = shallowRef(null);
		  return{
			map,
		  }
		},
		methods:{
		  initMap(){
			  AMapLoader.load({
			  key:'47d0984507cc20a46718c4cbeb32f9fa',  //设置您的key
			  version:"2.0",
			  plugins:['AMap.ToolBar','AMap.Driving'],
			  AMapUI:{
				  version:"1.1",
				  plugins:[],

			  },
			  Loca:{
				  version:"2.0.0"
			  },
		  }).then((AMap)=>{
			  
			  this.map = new AMap.Map('container', {
					resizeEnable: true
				});
			// AMap.plugin('AMap.Geolocation', function() {
			        var geolocation = new AMap.Geolocation({
			            enableHighAccuracy: true,//是否使用高精度定位，默认:true
			            timeout: 10000,          //超过10秒后停止定位，默认：5s
			            buttonPosition:'RB',    //定位按钮的停靠位置
			            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
			            zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点
			
			        });
			        this.map.addControl(geolocation);
			        geolocation.getCurrentPosition(function(status,result){
			            if(status=='complete'){
			                onComplete(result)
			            }else{
			                onError(result)
			            }
			        });
			    // });
			    //解析定位结果
			    function onComplete(data) {
					uni.showToast({
					    icon: 'none',
						position: 'top',
					    title: '定位成功'
					});
					// alert("");
			        //document.getElementById('status').innerHTML='定位成功'
			        var str = [];
			        str.push('定位结果：' + data.position);
			        str.push('定位类别：' + data.location_type);
			        if(data.accuracy){
			             str.push('精度：' + data.accuracy + ' 米');
			        }//如为IP精确定位结果则没有精度信息
			        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
					uni.showToast({
					    icon: 'none',
						position: 'top',
					    title: str.join('<br>')
					});
			        //document.getElementById('result').innerHTML = str.join('<br>');
			    }
			    //解析定位错误信息
			    function onError(data) {
					uni.showToast({
					    icon: 'none',
						position: 'top',
					    title: "定位失败"
					});
			        // document.getElementById('status').innerHTML='定位失败'
			        // document.getElementById('result').innerHTML = '失败原因排查信息:'+data.message;
			    }
			

		  }).catch(e=>{
			  console.log(e);
		  })
			  
			// AMapLoader.load({
			//   key:"a3cf30b9c6a9fd33087296759d3e642d",             // 申请好的Web端开发者Key，首次调用 load 时必填
			//   version:"2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
			//   plugins:[''],       // 需要使用的的插件列表，如比例尺'AMap.Scale'等
			// }).then((AMap)=>{
			//   this.map = new AMap.Map("container",{  //设置地图容器id
			// 	center: [116.397428, 39.90923],
			// 	layers: [//只显示默认图层的时候，layers可以缺省
			// 	new AMap.TileLayer()//高德默认标准图层
			// 	],
			// 	zoom: 13
			//   });
			// }).catch(e=>{
			//   console.log(e);
			// })
			
			
		  },
		  
		}
	}
	
</script>

<style scoped>
#container{
  padding:0px;
  margin: 0px;
  width: 100%;
  height: 800px;
}
</style> -->