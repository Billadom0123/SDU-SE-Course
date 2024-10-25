<template>
	
	<view>
		<view class="page-body">
			<view class="page-section page-section-gap">
				<map :style="mapStyle"  
				:latitude="latitude" 
				:longitude="longitude" 
				:markers="covers"
				:polyline="polyline"
				:controls="controls"
				@controltap="personalCenter"
				>
				</map>
			</view>
			<view>
				<!-- 开始听单 -->
				<button type="primary" @click="startBusiness">{{buttonText}}</button>
			</view>
				<!-- 司机接单列表 -->
			<view >
				<view class="example-button">
					<button type="primary"  @click="showDrawer('showLeft')"><text class="word-btn-white">已经接单</text>
				</button>
				</view>
				
				<uni-drawer ref="showLeft" mode="left" :width="400" @change="change($event,'showLeft')" class="drawer-body">
					 <scroll-view :style="{ height: '100%' }" :scroll-y="true" >
					<view class="close">
						<view >
							<br>
							<br>
							<uni-card v-for="(item, index) in requsetMessage" :key="index"
							:title="item.title"
							:sub-title="item.jsonData.subtitle"
							:extra="item.price"
							>
							  <!-- 根据 item 的属性来动态显示内容 -->
							  <!-- <view>
								  <text>订单{{item.index+1}}</text>
							  </view> -->
							  <view>
								  <text>
									  起点: {{item.jsonData.startName}}<br>
									  终点: {{item.jsonData.desName}}<br>
									  本次里程长度: {{item.jsonData.distance2}}公里
								  </text>
								  <text v-if="item.jsonData.is_reservation"><br>预约时间: {{item.jsonData.appointmentTime}}</text>
							  </view>
							  <view class="button-container">
								  <button class="custom-button" @click="openPassenger(index)"  v-if="!item.jsonData.isReceivePassenger" type="primary">
									  接到乘客 
									</button>
									<uni-popup ref="pass" type="center" :mask-click="false" :is-mask-click="false">
										<view class="example-body">
										<view class="color-sub-text ptb-10">请输入乘客手机尾号后四位</view>
										<view class="ptb-10">
										    <ca-code-input v-model="codeInput1" />
										</view>
										
										<button  @click="receivePassenger(index)">确认</button>
										<button  @click="closePassenger(index)">取消</button>
										</view>
									</uni-popup>
									
								  <button class="custom-button" @click="deleteOrder(index)" v-if="!item.jsonData.isReceivePassenger" type="primary">
									取消订单 
									  </button>
									<br>
									<!-- v-if="!item.jsonData.isReceivePassenger" -->
									<!-- <button class="custom-button" @click="getRoute(index)"  type="primary">
									开始导航 
									  </button> -->
								  <button class="custom-button" @click="commitOrder(index)" v-if="item.jsonData.isReceivePassenger">
									结束订单 
									  </button>
									<button class="custom-button" @click="callCustomer(index)" >
									联系乘客 
									  </button>
									  <!-- 导航 -->
									  <uni-icons type="paperplane-filled" @click="getRoute(index)" size="30"></uni-icons>
									  
							  </view>
							</uni-card>							
						</view>
						<button @click="deleteAllOrders"><text class="word-btn-white">取消所有订单</text></button>
						<button @click="closeDrawer('showLeft')"><text class="word-btn-white">关闭</text></button>
					</view>
					</scroll-view>
				</uni-drawer>
			</view>
			
		</view>
		
	</view>
	
</template>

<script>
	let _this=this;
	import {AndroidStartBusiness,
			AndroidconfirmOrder,
			AndroidEndBusiness,
			AndroidReceivePassenger,
			AndroidDeleteOrder,
			AndroidCommitOrder,
			getDrivingRoute} from '../../service/genService.js'
	import {formatLocation,getMapDistance} from '../../util/formatter.js'
	import {androidWSPrefix} from '../../config/systemConfig.js'
	import {updateDriverLocation} from '../../util/wsUpdateLocation.js'
	import {connectToWSServer} from '../../util/websocketConnector.js'
	export default {
		data() {
			return {
				timer: null, // 定时器标识
				interval: 3000, // 定时器间隔，单位为毫秒
				driverIdle:true,//初始司机状态为空闲true，忙碌为false
				codeInput1:'',
				isDrawerVisible:false,
				userId:uni.getStorageSync("userId"),
				showRight: false,
				showLeft: false,
				socketTask: null, // WebSocket 任务对象
				receivedData: '', // 接收到的数据
				latitude:'',
				longitude:'',
				isStartBusiness:false,//是否开始听单
				buttonText:"开始听单",
				covers:[
					{
						id:0, // 使用 marker点击事件 需要填写id
						latitude:'',//司机当前位置
						longitude:'',
						iconPath: '../../static/Info-Point.png'
					},
					{
						label: {
							content: "乘客上车点", //文本内容
							fontSize: 10, //文本字体大小
							x: -20, //label的坐标，原点是 marker 对应的经纬度
							y: 30, //label的坐标，原点是 marker 对应的经纬度
							borderWidth: 12, //边框宽度
							padding: 5, //文本边缘留白
							textAlign: "right", //文本对齐方式。
						 }, 
						id:1, // 使用 marker点击事件 需要填写id
						latitude:'',
						longitude:'',
						iconPath: '../../static/location.png'
					}, 
					{
						label: {
							content: "乘客下车点", //文本内容
							color: "blue", //文本颜色
							bgColor: "#fff", //文本背景色
							fontSize: 10, //文本字体大小
							x: -20, //label的坐标，原点是 marker 对应的经纬度
							y: 30, //label的坐标，原点是 marker 对应的经纬度
							borderWidth: 12, //边框宽度
							borderColor: "pink", //边框颜色
							borderRadius: 20, //边框圆角
							bgColor: "black", //背景色
							padding: 5, //文本边缘留白
							textAlign: "right", //文本对齐方式。
						 }, 
						id:2, // 使用 marker点击事件 需要填写id
						latitude: '',//39.90,
						longitude: '',//116.39,
						iconPath: '../../static/location.png'
					}
				],
				controls: [
					{
					  id: 1,
					  position:{//控件在地图的位置
					  　　left:15,//默认只有左 上
					  　　top:40,
					  　　width:50,
					  　　height:50
				  　　  },    
					  iconPath: '../../static/personalCenter.png',
					  clickable: true,
					},
				],
				requsetMessage:[],
				origin:{
					longitude:117.140113,
					latitude:36.667385
				},
				destination:{
					longitude:117.050418,
					latitude:36.601215
				},
				wayPoints:{
					longitude:117.059985,
					latitude:36.675681
				},
				polyline:[
					{
						points:[
							{}
						],
						color: "#0091ff",
						width: 6,
					}
				]
				
			}
		},
		// onShow:当uni-app启动，或从后台进入前台显示
		// onHide:当uni-app从前台进入后台
		onShow() {
			_this.startUpdateLocation();
		},
		onHide() {
			// 页面隐藏时，清除定时器   开始听单维护，，取消听单就不用维护了
			_this.cancelUpdatePosition();
		},
		computed: {
		    mapStyle() {
		      // 根据弹窗是否可见动态设置样式
		      return {
				width: '100%',
		        height: this.isDrawerVisible ? '0px' : '600px',
		      };
		    },
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
					_this.covers[0].latitude = position.latitude
					_this.covers[0].longitude = position.longitude
				},
				fail: function(res){
					console.log(res)
				},
			})
			
			uni.connectSocket({
				url:androidWSPrefix+'/api/DispatchOrderServer/'+this.userId
			})
			uni.onSocketOpen((res)=>{
				// console.log("ws连接已打开,用户id:"+uni.getStorageSync("userId"))
				console.log("ws连接已打开,用户id:"+this.userId)
			})
			uni.onSocketError((err)=>{
				console.log(err)
			})
			uni.onSocketMessage(function(res){
				console.log(res.data)
				//处理ws订单信息
				_this.dealWSData(res.data);
			})
			
			_this.setRequestList();
			//司机当前时候听单状态
			
			//实时追踪司机位置
			connectToWSServer(androidWSPrefix+'/api/RealTimeLocationUpdateServer');
			

		},
		mounted() {
			// this.getRoute()
		},
		methods: {
			//取消监听
			cancelUpdatePosition(){
				clearInterval(this.timer);
				this.timer = null;
				uni.stopLocationUpdate({
				  success: res => console.log('关闭小程序接收位置消息成功'),
				  fail: err => console.error('关闭小程序接收位置消息失败：', err),
				  complete: msg => console.log('调用关闭小程序接收位置消息 API 完成')
				});
			},
			updatePosition() {
			      // 获取实时位置，更新数据   这个方法会实时更新，不是三s一次
				  uni.onLocationChange(function (res) {
				 
					_this.latitude=res.latitude;
					_this.longitude=res.longitude;
					
					//改指针
					_this.covers[0].latitude=res.latitude;
					_this.covers[0].longitude=res.longitude;
				  });
				 //_this.UpdatePositionWS();
			
			},
			//更新实时位置 ws
			UpdatePositionWS(){
				//下面的是三秒一次
				// console.log(_this.longitude+"            "+_this.latitude);
				var res = updateDriverLocation(_this.longitude,_this.latitude);
				res.then((res)=>{ 
					console.log(res)//不管怎样都是发送成功了吧 {"errMsg":"sendSocketMessage:ok"}
				})
			},
			//开启监听()
			startUpdateLocation(){
				
				uni.startLocationUpdate({
				  success: res => console.log('开启小程序接收位置消息成功'),
				  fail: err => console.error('开启小程序接收位置消息失败：', err),
				  complete: msg => console.log('调用开启小程序接收位置消息 API 完成')
				});
				
			},
			//  	console.log('经度：' + res.longitude);
			// console.log('纬度：' + res.latitude);
			
			//截取字符串
			truncateDecimal(str, maxDecimalPlaces) {
			  const match = str.match(/^\d+\.\d+/);
			  if (match) {
			    const truncated = parseFloat(match[0]).toFixed(maxDecimalPlaces);
			    return truncated;
			  }
			  return str;
			},
			//获取导航路线
			getRoute(index){
				const that = this;
				
				//当前订单
				let currentOrder = that.requsetMessage[index].jsonData;
				that.requsetMessage[index].jsonData.isGetRoute=true;
				//origin 是司机的当前位置 
				this.origin.latitude=that.latitude;
				this.origin.longitude=that.longitude;
				//waypoint是乘客起点
				this.wayPoints.longitude=this.truncateDecimal(currentOrder.startX,6);
				this.wayPoints.latitude=this.truncateDecimal(currentOrder.startY,6);
				//destination是终点
				this.destination.longitude=this.truncateDecimal(currentOrder.desX,6);
				this.destination.latitude=this.truncateDecimal(currentOrder.desY,6);
				
				//乘客上下车label  还要有清空的方法   为什么不显示啊（因为写反了呃呃呃）
				this.covers[1].longitude=currentOrder.startX;
				this.covers[1].latitude=currentOrder.startY;
				this.covers[2].longitude=currentOrder.desX;
				this.covers[2].latitude=currentOrder.desY;
				
				console.log(this.origin+" "+this.wayPoints+" "+this.destination);
				// console.log(currentOrder);
				getDrivingRoute(this.origin,this.destination,this.wayPoints).then((res)=>{
					const data = res.data.route
					console.log(res.data);
					var pois = []
					if (data.paths && data.paths[0] && data.paths[0].steps) {
						var steps = data.paths[0].steps;//选总和考虑的策略
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
					that.polyline[0].points=pois;
					console.log(pois);
					console.log("导航成功");
					console.log(that.polyline[0].points);
					uni.showToast({
						title:'开始为您导航'
					})
					
				})
			},
			//联系乘客
			callCustomer(index){
				uni.makePhoneCall({
					phoneNumber:_this.requsetMessage[index].jsonData.phone,//手机号还没传入捏
					success: (res) => {
						console.log('调用成功!')	
						},
					// 失败回调
					fail: (res) => {
						console.log('调用失败!')
						}
				})
			},
			//drawer需要get
			getRequestList(){
				uni.getStorage({
					key:_this.userId+'orderList',
					success: function (res) {
						console.log(res.data);
						return res.data;
					},
					fail: function(res){
						console.log(res);
						return res;
					}
				})
			},
			//删除订单，接到乘客，接到订单都要set
			setRequestList(){
				uni.setStorage({
					key: _this.userId+'orderList',
					data: _this.requsetMessage,
					success: function (res) {
						// console.log('已存入success');
					}
				})
			},
			
			dealWSData(resData){
				if(resData.substring(0,5)==='订单已取消'){
					let cancelReqId=resData.substring(5);
					let index = _this.requsetMessage.findIndex(item=>item.jsonData.id==cancelReqId)
					console.log(index);
					if(index!=-1){
						uni.showToast({
							title:`乘客取消订单${index+1}`
						})
						_this.clean(index);
						_this.requsetMessage.splice(index,1);
						_this.setRequestList();
						//应该取消当前request标记的
					}
					else{
						console.log(`id ${cancelReqId} 未在列表中找到`);
					}
					return;
				}
				// this.receivedData 
				let jsonData= JSON.parse(resData); // 解析 JSON 数据
				
				// console.log(jsonData.request.customerId);
				console.log(jsonData.id);//requestId
				console.log(jsonData);
				//计算起点距司机多远
				let lon1=jsonData.startX;
				let lat1=jsonData.startY;
				let distance=getMapDistance(lat1,lon1,_this.latitude,_this.longitude);
				//计算订单路程长度
				let lon2=jsonData.desX;
				let lat2=jsonData.desY;
				let distance2=getMapDistance(lat1,lon1,lat2,lon2);
				// console.log(distance);
				
				//判断是否为实时单 ，预约单
				var str
				if(jsonData.appointmentTime===''){
					jsonData.is_reservation=false;
					str='距您' + distance + '公里\n' +
					   '起点：' + jsonData.startName + '\n' +
					   '终点：' + jsonData.desName+'\n' +
					   '本次行程预计'+distance2 + '公里';
				}
				else{
					jsonData.is_reservation=true;
					str='距您' + distance + '公里\n' +
					   '起点：' + jsonData.startName + '\n' +
					   '终点：' + jsonData.desName+'\n' +
					   '本次行程预计'+distance2 + '公里'+'\n' +
					   '预约时间：'+jsonData.appointmentTime;
				}
				
				//接到订单后
				uni.hideLoading();
				uni.showModal({
					title:'已为您接到订单',
					content:str,
					cancelText:'取消接单',
					confirmText:'确认接单',
					success: function (res) {
						var accept;//是否接单
						if (res.confirm) {
							console.log('用户点击确定');
							//确定 加入订单列表
							let index = _this.requsetMessage.length;//当前数组第几条消息
							console.log(index);
							jsonData.distance2=distance2;
							jsonData.distance=distance;
							jsonData.isReceivePassenger=false;
							jsonData.isGetRoute=false;
							if(jsonData.is_reservation){
								jsonData.subtitle="预约单";
							}
							else jsonData.subtitle="实时单";
							_this.requsetMessage.push({index,jsonData}); // 假设 JSON 数据中有一个名为 message 的字段
							_this.requsetMessage[index].title="订单"+index;
							_this.requsetMessage[index].price='';
							accept=true;
							//
							// _this.setRequestList();
							
						} else if (res.cancel) {
							console.log('用户点击取消');
							accept=false;
						}
						AndroidconfirmOrder(_this.userId,jsonData.id,accept).then((res)=>{
							if(res.data.success){
								var titleString;
								if(accept===true){
									titleString="接单成功"
								}
								else{
									titleString="取消成功"
								}
								uni.showToast({
									title:titleString
								})
							}
							else{
								uni.showToast({
									icon:'error',
									title:"请重试",
									// title:res.errMsg
								})
								console.log(res.data.errorMsg)
							}
						})
						console.log(_this.requsetMessage)
					}
				});
			},
			//显示抽屉窗口
			showDrawer(e) {
				//先预处理 我担心删除的时候必须要重复刷新，不然出不来
				// _this.requsetMessage=_this.getRequestList;
				// console.log(_this.requsetMessage);
				this.isDrawerVisible=true;
				this.$refs[e].open()
			},
			// 关闭抽屉窗口
			closeDrawer(e) {
				this.$refs[e].close()
				this.isDrawerVisible=false;
			},
			// 抽屉状态发生变化触发
			change(e, type) {
				// this.isDrawerVisible=!this.isDrawerVisible
				// console.log((type === 'showLeft' ? '左窗口' : '右窗口') + (e ? '打开' : '关闭'));
				this[type] = e
			},
			
			
			//去个人中心
			personalCenter(){
				uni.navigateTo({
					url: './cy-my',
					success: res => {},
					fail: (res) => {
						console.log(res)
					},
					complete: () => {}
				});
			},
			
			//司机开始听单
			startBusiness(){
				if(!this.isStartBusiness){
					// let uid = uni.getStorageSync("userId")
					AndroidStartBusiness(
						_this.userId,
						this.latitude,
						this.longitude
					).then((res)=>{
						console.log(res);
						if(res.data.success){
							uni.showLoading({
								title: '正在接单',
								
							});
							setTimeout(function () {
								uni.hideLoading();
							}, 2000);
							_this.isStartBusiness=true;
							//存入司机当前是否接单
							uni.setStorageSync(_this.userId+'isStartBusiness',true);
							_this.buttonText="取消听单"
							
							// 启动定时器，在一定时间间隔内执行 updatePosition 函数
							_this.startUpdateLocation();
							_this.timer = setInterval(_this.UpdatePositionWS, _this.interval);	
						}
						else{
							uni.showModal({
								content:res.data.errorMsg
							})
						}
					})
				}
				else{
					 this.endBusiness();
				}
			},
			//结束听单
			endBusiness(){
				AndroidEndBusiness(_this.userId).then((res)=>{
					if(res.data.success){
						uni.showToast({
							title:'您已取消听单'
						})
						uni.setStorageSync(_this.userId+'isStartBusiness',false);
						this.isStartBusiness=false;
						uni.hideLoading();//隐藏loading框
						this.buttonText="开始接单"
						_this.cancelUpdatePosition();
					}
					else{
						uni.showToast({
							icon:'error',
							title:"请重试"
							
						})
						console.log(res.data.errorMsg);
					}
				})
			},
			//接到乘客 get=>下一步就要开导航路线规划了
			openPassenger(index){
				this.$refs['pass'][0+index].open('center');
			},
			//司机误触可以直接关闭弹窗
			closePassenger(index){
				this.$refs['pass'][0+index].close('');
			},
			receivePassenger:function(orderIndex){
				
				let phone=_this.requsetMessage[orderIndex].jsonData.phone;
				// console.log(phone);
				
				let last4=phone.slice(-4);
				// console.log(last4+"  "+this.codeInput1);
				// console.log(orderIndex);
				// var 
				//先比对手机号
				if(this.codeInput1!=last4){
					uni.showToast({
						icon:'error',
						title:"不正确，请重试",
						duration:2000
					})
					this.codeInput1='';
					console.log("last "+last4);
					console.log("codeInput1 "+this.codeInput1);
					return;
				}
				//手机号正确的话
				//再关闭弹窗
				this.$refs['pass'][0+orderIndex].close('');
				
				AndroidReceivePassenger(this.userId,_this.requsetMessage[orderIndex].jsonData.id).then((res)=>{
					if(res.data.success){
						//接到乘客后
						_this.requsetMessage[orderIndex].jsonData.isReceivePassenger=true;
						_this.setRequestList();
					}
					else{
						uni.showToast({
							icon:'error',
							title:"请重试"
						})
					}
				})
				this.codeInput1=''
				//导航
				//先处理起始点，途径点，目标点
				this.origin.latitude=this.latitude
				this.origin.longitude=this.longitude
				const targetOrder = this.requestMessage[orderIndex].jsonData
				this.destination.latitude=targetOrder.desY;
				this.destination.longitude=targetOrder.desX;
				this.wayPoints.latitude=targetOrder.startY;
				this.wayPoints.longitude=targetOrder.startX;
				this.getDriverRoute()
				console.log(this.polyline)
			},
			//删除订单 post
			deleteOrder:function(orderIndex){
				//怎么判断司机当前是否
				let deleteOrderList=[];
				deleteOrderList.push(_this.requsetMessage[orderIndex].jsonData.id);
				AndroidDeleteOrder(_this.userId,deleteOrderList).then((res)=>{
						if(res.data.success){
							uni.showToast({
									title:"删除成功"
								})
							_this.requsetMessage.splice(orderIndex,1);//删除从orderIndex开始的第一个
							console.log(_this.requsetMessage);
							_this.setRequestList();
						}
						else {
							uni.showToast({
								title:"请重试"
							})
							console.log(res.data.errorMsg);
						}
				})
			},
			//完成订单 提交订单 post
			commitOrder:function(orderIndex){
				AndroidCommitOrder(_this.userId,_this.requsetMessage[orderIndex].jsonData.id,_this.longitude,_this.latitude).then((res)=>{
					if(res.data.success){
						uni.showToast({
							title:"订单已完成"
						})
						console.log(res.data.data);//收费
						_this.clean(orderIndex);
						_this.requsetMessage.splice(orderIndex,1);
						_this.setRequestList();
					}
					else{
						uni.showToast({
							title:"请重试"
						})
						console.log(res.data.errorMsg);
					}
				})
			},
			//结束订单时  清除当前地图标记
			clean(index){
				
				if(_this.requsetMessage[index].jsonData.isGetRoute==true){
					//判断当前导航的是不是这个订单
					this.covers[1].latitude='';
					this.covers[1].longitude='';
					this.covers[2].latitude='';
					this.covers[2].longitude='';
					
					this.polyline[0].points='';
					_this.requsetMessage[index].jsonData.isGetRoute=false;
				}
				
			},
			//取消所有订单（分情况，如果有正在执行的，就不能取消？）  post
			deleteAllOrders(){
				let deleteOrderList=[];
				for(let i=0;i<_this.requsetMessage.length;i++){
					deleteOrderList.push(_this.requsetMessage[i].jsonData.id);
				}
				AndroidDeleteOrder(_this.userId,deleteOrderList).then((res)=>{
						if(res.data.success){
							uni.showToast({
									title:"删除成功"
								})
							_this.requsetMessage=[];//删除从orderIndex开始的第一个
							_this.setRequestList();
						}
						else {
							uni.showModal({
								content:res.data.errorMsg
							})
							console.log(res.data.errorMsg);
						}
				})
			}
			
			
		}
	}
</script>

<style>
.example-button {
	padding-top: 10px;
}
.drawer-body {
	/* padding: 30px; */
	width: 100%;
}
.scroll-view {
	/* #ifndef APP-NVUE */
	width: 100%;
	height: 100%;
	/* #endif */
	flex:1
}
/* // 处理抽屉内容滚动 */
.scroll-view-box {
	flex: 1;
	position: absolute;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
}
.info {
	padding: 15px;
	color: #666;
}

.info-text {
	font-size: 14px;
	color: #666;
}
.info-content {
	padding: 5px 15px;
}
.close {
	padding: 10px;
	padding-top: 30rpx;
}

.button-container {
  display: flex;
  justify-content: space-between; /* 将按钮平均分配容器的可用空间 */
}

.custom-button {
  width: 145px; /* 自定义按钮的宽度 */
  height: 35px; /* 自定义按钮的高度 */
  font-size: inherit; 
  /* font-size:  1.5em; /* 使用vw单位设置字体大小 */ 
}



.ptb-10{
  padding-top: 10rpx;
  padding-bottom: 10rpx;
}
.color-sub-text{
  color: #666;
}
</style>
