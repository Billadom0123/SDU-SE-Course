<template>
	<view>
		<view class="page-body">
			<view class="page-section page-section-gap">
				<!-- :polyline="polyline" -->
				<map :style="mapStyle"  :latitude="latitude" :longitude="longitude" 
				:markers="covers"
				:controls="controls"
				@controltap="personalCenter">
				</map>
			</view>
			<!-- style="width: 100%; height: 600px;" -->
			<uni-list>
				<uni-list-item 
				 thumb="../../static/pointer.png"  thumbSize="sm"
				 title="上车点" :right-text="start_name"  clickable="true" @click="chooseLocation" :border="false" show-arrow="true" ></uni-list-item>
				<uni-list-item 
				thumb="../../static/endpointer.png"  thumbSize="sm" 
				title="下车点" :right-text="des_name"  clickable="true" @click="chooseLocation2" :border="false" show-arrow="true"></uni-list-item>
			</uni-list>
			
		
			<view>
				<uni-card :is-shadow="false" is-full v-if="is_reservation||is_instead"  @click="cleanInfo()">
					<text class="uni-h6" v-if="is_reservation">
						预约时间：{{ appointment_time}}
					</text>
					<text class="uni-h6" v-if="is_instead">
						乘车人电话：{{ customer_phone }}
					</text>
				</uni-card>
			</view>
			
			<view class="icon-list">
				<view class="icon-with-text" >
					<uni-icons custom-prefix="iconfont" type="icon-icon_order" size="64"  class="button-icon"  @click="showTimePicker">
					</uni-icons>
					<!-- 弹窗 disabled="false"v-model="isTimePickerVisible"  如果点击空白处可以退出的话，地图大小回不去-->
					<uni-popup  type="bottom" ref="reser"  :z-index="popupZIndex"  :is-mask-click="false">
						<view>
						<uni-section :title="'请选择上车时间：'" type="line"></uni-section>	
						<view class="example-body">
					  <uni-datetime-picker
						mode="datetime"
						:start="startDate"
						:end="endDate"
						
						return-type="string"
						@change="onTimeSelected"
						
					  ></uni-datetime-picker>
					  <button type="primary" @click="closeTimePicker">确定</button>
					  </view>
					  </view>
					</uni-popup>
					<span class="text">预约</span>  
				</view>
				<!--  -->
				<view class="icon-with-text" >
					<uni-icons custom-prefix="iconfont" type="icon-icon_daijia-copy" size="64"  class="button-icon"  @click="open">
					</uni-icons>
					<uni-popup ref="popup" type="bottom" :mask-click="false" :is-mask-click="false">
						<view class="example-body">
						<uni-section :title="'请输入乘车人电话：'" type="line"></uni-section>
						<uni-easyinput type="number" class="uni-mt-5" v-model="customer_phone" placeholder="乘车人电话" 
						@input="clearInput" @change="copyExist" confirm-type="done" @clear="clear_copy"></uni-easyinput>
						<button type="primary" @click="close_copy">确定</button>
						</view>
					</uni-popup>
					<span class="text">代叫</span>
				</view>
			
			</view>
			
			
			<!-- <button type="default" @click="cancelhailing()" v-if="isStart">取消当前约车</button> -->
			<button type="primary" @click="hailing" >{{StartText}}</button>
		   <!-- 如果已经有过正在进行的订单，再变为继续约车，一个变量判断当前 是否有约车 开始约车 -->
			
			
			<view class="example-body">
				<button type="primary" @click="showDrawer('showLeft')"><text class="word-btn-white">当前约车列表</text>
				</button>
				<uni-drawer ref="showLeft" mode="left" :width="400" @change="change($event,'showLeft')">
					 <scroll-view :style="{ height: '100%' }" :scroll-y="true" >
					<view class="close">
						<view>
							<br>
							<br>
							<uni-card v-for="(item, index) in requsetMessage" :key="index"
							:title="item.title"
							:sub-title="item.jsonData.subtitle"
							:extra="item.price"
							>
							  <!-- 根据 item 的属性来动态显示内容 -->
							  <view>
								  <text>
									  起点: {{item.jsonData.start_name}}<br>
									  终点: {{item.jsonData.des_name}}<br>
									  本次里程长度: {{item.jsonData.distance}}公里
								  </text>
								  <text v-if="item.jsonData.is_reservation"><br>预约时间: {{item.jsonData.appointment_time}}</text>
									<text  v-if="item.jsonData.isHaveDriver">
										<!-- 有时间了可以加上司机个人主页 联系方式: {{item.driverInfo.driverPhone}}<-->
										 <br>
										 
										司机: {{item.driverInfo.driverName}}   <button style="float: right; width: 120px; height: 35px; font-size: inherit; " @click="callDriver(index)">联系司机</button><br>  
										车辆: {{item.driverInfo.carColor}}{{item.driverInfo.carBrand}} {{item.driverInfo.licensePlateNumber}}<br> 
										 信用值: {{item.driverInfo.driverCredit}}
										
									</text>  
							  </view>
							  <view class="button-container">
								  
								  <button class="custom-button" @click="cancelhailing(index)"  type="primary" v-if="!item.jsonData.isReceivePassenger">
									取消订单 
									  </button>
									<button class="custom-button" @click="jumpCurrentOrder(index)"  type="primary">
									当前订单
									  </button>
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
import { androidWSPrefix} from '../../config/systemConfig.js'
import {AndriodcustomerRequest,AndriodcancelRequest} from '../../service/genService.js'
import {formatLocation,getMapDistance} from '../../util/formatter.js'
let _this=this;
export default {
	name:'selectPosition',
	data() {
		return {
			requsetMessage:[],//乘客端叫车订单列表（这个列表应该后端查询维护啊，不然每次登录就被刷新了）
			isStart:false,//当前乘客是否正在有订单
			StartText:"开始约车",
			isDrawerVisible:false,
			userId:uni.getStorageSync("userId"),//当前登录用户id
			showClearIcon: false,
			inputClearValue: '',
			// popupVisible: false,
			isPopupVisible: false,
			title: 'map',
			latitude: '39.901',
			longitude: '116.395',
			address: '',
			hasLocation:false,
			location:'',
			locationAddress:'',
			start_name:'选择上车点',
			des_name:'你要去哪儿',
			request_time: '',//time,//请求发起时间   年月日  时分秒
			is_reservation:false,//boolean,//是否预约，
			appointment_time:'',//time,//预约起始时间 年月日  时分秒
			is_instead:false,//boolean,//是否代叫，
			customer_phone:'',//string,//被代叫人手机号
			popupZIndex:1000,// 调整弹窗的层级
			polyline:[
				{
					points:[
						{
							latitude:0,
							longitude:0
						},
						{
							latitude:0,
							longitude:0
						}
					],
					color:'#ff00f0',
					width:2
				}

			],
			covers:[
			{
				label: {
					content: "上车点", //文本内容
					// color: "blue", //文本颜色
					// bgColor: "#fff", //文本背景色
					fontSize: 10, //文本字体大小
					x: -20, //label的坐标，原点是 marker 对应的经纬度
					y: 30, //label的坐标，原点是 marker 对应的经纬度
					borderWidth: 12, //边框宽度
					// borderColor: "write", //边框颜色
					// borderRadius: 20, //边框圆角
					// bgColor: "black", //背景色
					padding: 5, //文本边缘留白
					textAlign: "right", //文本对齐方式。
				 }, 
				id:0, // 使用 marker点击事件 需要填写id
				latitude:'',
				longitude:'',
				iconPath: '../../static/location.png'
			}, 
			{
				label: {
					content: "下车点", //文本内容
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
				id:1, // 使用 marker点击事件 需要填写id
				latitude: '',//39.90,
				longitude: '',//116.39,
				iconPath: '../../static/location.png'
			},
			{
				id:2, // 当前位置 使用 marker点击事件 需要填写id
				latitude: '',//39.90,
				longitude: '',//116.39,
				iconPath: '../../static/Info-Point.png'
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
			
			// single: '',
			// datetimesingle: '',
			// range: ['2021-02-1', '2021-3-28'],
			// datetimerange: [],
			startDate: Date.now(),//'2023-08-09 00:00:00',
			endDate: Date.now()+432000000,// 从今天开始后五天  1000000000,
			reqId:'',
		};
	},
	 computed: {
	    mapStyle() {
	        // 根据弹窗是否可见动态设置样式
	        if (this.isPopupVisible) {
	          return {
	            width: '100%',
	            height: '400px'
	          };
	        } else if (this.isDrawerVisible) {
	          return {
	            width: '100%',
	            height: '0px'
	          };
	        } else {
	          return {
	            width: '100%',
	            height: '600px'
	          };
	        }
	      },
	},
	onLoad() {
		// this.connectWSServer()
		_this=this;
		uni.connectSocket({
			url:androidWSPrefix+'/api/DispatchOrderServer/'+uni.getStorageSync("userId"),
			// androidWSPrefix+'/api/DispatchOrderServer/'+uni.getStorageSync("userId")
		})
		uni.onSocketOpen((res)=>{
			console.log("ws连接已打开,用户id:"+_this.userId)
		})
		uni.onSocketError((err)=>{
			console.log(err)
		})
		uni.onSocketMessage(function(res){
 			console.log(res.data)
			//处理ws订单信息
			_this.dealWSData(res.data);
		})
		this.init();
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
	mounted() {
		_this = this
	},
	methods: {
		//乘客没必要实时传数据给后端，所以只是监听当前位置在前端的变化
		//取消监听
		cancelUpdatePosition(){
			
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
				_this.covers[2].latitude=res.latitude;
				_this.covers[2].longitude=res.longitude;
			  });
			 //_this.UpdatePositionWS();
		
		},
		//开启监听()
		startUpdateLocation(){
			uni.startLocationUpdate({
			  success: res => console.log('开启小程序接收位置消息成功'),
			  fail: err => console.error('开启小程序接收位置消息失败：', err),
			  complete: msg => console.log('调用开启小程序接收位置消息 API 完成')
			});
			
		},

		callDriver(index){
			uni.makePhoneCall({
				phoneNumber:_this.requsetMessage[index].driverInfo.driverPhone,//手机号还没传入捏
				success: (res) => {
					console.log('调用成功!')	
					},
				// 失败回调
				fail: (res) => {
					console.log('调用失败!')
					}
			})
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
		
		//清除当前预约和代叫信息   
		cleanInfo(){
			// if()
			uni.showActionSheet({
				itemList: ['清除预约信息', '清除代叫信息','同时清除'],
				success: function (res) {
					console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
					if(res.tapIndex===0){
						_this.is_reservation=false;
						_this.appointment_time='';
					}
					else if(res.tapIndex===1){
						_this.is_instead=false;
						_this.customer_phone='';
					}
					else{
						_this.is_reservation=false;
						_this.appointment_time='';
						_this.is_instead=false;
						_this.customer_phone='';
					}
				},
				fail: function (res) {
					console.log(res.errMsg);
				}
			});

		},
		//跳转到当前订单页面
		jumpCurrentOrder(index){
			//起始点 终点  预约时间，代叫
			let currentOrder=_this.requsetMessage[index].jsonData;
			//上车点
			this.covers[0].longitude=currentOrder.startX;
			this.covers[0].latitude=currentOrder.startY;
			this.start_name=currentOrder.start_name;
			//下车点
			this.covers[1].longitude=currentOrder.desX;
			this.covers[1].latitude=currentOrder.desY;
			this.des_name=currentOrder.des_name;
			//预约
			this.is_reservation=currentOrder.is_reservation;
			if(currentOrder.is_reservation){
					this.appointment_time= currentOrder.appointment_time;
			}
			//代叫
			this.is_instead=currentOrder.is_instead;
			if(currentOrder.is_instead){
				this.customer_phone=currentOrder.customer_phone;
			}
		},	
		dealWSData(resData){
			if(resData.substring(0,17)==='司机已经接到您了,不能再取消订单:'){
				let cancelReqId=resData.substring(17);
				let index = _this.requsetMessage.findIndex(item=>item.jsonData.reqId==cancelReqId);
				if (index !== -1) {
				  _this.requsetMessage[index].jsonData.isReceivePassenger=true;
				  uni.showToast({
				  	title:'开始行程'
				  })
				  return;
				} else {
				  console.log(`id ${jsonData.reqId} 未在列表中找到`);
				  return;
				}
			}
				
			if(resData.substring(0,8)==='没有司机愿意接单'){
				// uni.showModal({
				// 	title:'提示',
				// 	content:'当前订单没有司机愿意接单'
				// })
				//感觉这个ws会不断提示啊
				return;
			}
			
			let jsonData= JSON.parse(resData);
			console.log(jsonData);
			
			let index = -1;
			index = _this.requsetMessage.findIndex(item=>item.jsonData.reqId==jsonData.reqId)
			// findIndex() 
			// 方法会返回找到的元素的索引值，如果没有找到则返回 -1。
			if (index !== -1) {
			  console.log(`id ${jsonData.reqId} 在列表中的索引为 ${index}`);
			} else {
			  console.log(`id ${jsonData.reqId} 未在列表中找到`);
			  return;
			}
			// let mergeJsonData=Object.assign({}, jsonData, originJsonData);
			//{jsonData,originJsonData};
			//司机信息单独存放，避免司机取消订单不好查询
			if(jsonData.cancelInfo==="订单已取消"){
				_this.requsetMessage[index].jsonData.isHaveDriver=false;
				uni.showModal({
					title: '订单被取消',
					content:`订单 ${index+1} 已被司机取消，正在为您分配新的司机`,
					showCancel:false,
					success: function (res) {
							if (res.confirm) {
								console.log('用户点击确定');
							} 
						}
				})
				return;
			}
			else if(jsonData.price !== null && jsonData.price !== undefined){
				console.log(jsonData);
				_this.requsetMessage[index].price=jsonData.price;
				//评价 "price" in resData
				uni.showModal({
					title:'评价提醒',
					content:'订单已完成，请前往评价',
					confirmText:'前往评价',
					success: function (res) {
							if (res.confirm) {
								console.log('用户点击确定');
								console.log(_this.requsetMessage[index].driverInfo.driverName);
								uni.navigateTo({
									url: './evaluateOrder?id=' + _this.userId + '&reqId=' + jsonData.reqId + '&driverName=' + _this.requsetMessage[index].driverInfo.driverName, 
									success: function(res) {
										console.log("跳转成功");
									  }
								})
								_this.requsetMessage.splice(index,1);
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
				})
				//完成评价删除
				
				return;
				//支付 跳转到支付页面
			}//如果乘客已经上车就不能取消订单
			else if(jsonData.receivePassenger===true){
				_this.requsetMessage[index].jsonData.isReceivePassenger=true;
				uni.showToast({
					title:'开始行程'
				})
				return;
			}
			_this.requsetMessage[index].driverInfo=jsonData;
			console.log(_this.requsetMessage[index]);

			//查到reqId对应的信息，弹窗司机已经接单，然后更新list
			uni.showToast({
				title:`订单 ${index + 1} 已接单`
			})
			_this.requsetMessage[index].jsonData.isHaveDriver=true;
			
			
			
		},
		//乘客端删除当前所有订单
		deleteAllOrders(){
			let deleteOrderList=[];
			for(let i=0;i<_this.requsetMessage.length;i++){
				if(_this.requsetMessage[i].jsonData.isReceivePassenger===false) {
					deleteOrderList.push(_this.requsetMessage[i].jsonData.reqId);
				}
				else{
					uni.showModal({
						title:'提示',
						content:'司机已经接到您，无法取消订单'+i+1
					})
					return;
				}
			}
			AndriodcancelRequest(_this.userId,deleteOrderList).then((res)=>{
					if(res.data.success){
						uni.showToast({
								title:"删除成功"
							})
						_this.requsetMessage=[];//删除从orderIndex开始的第一个
						_this.clean();
					}
					else {
						uni.showToast({
							title:"请重试"
						})
						console.log(res.data.errorMsg);
					}
			})
		},
		//插入用户约车信息
		InsertRequstMessage(jsonData){
			let index = _this.requsetMessage.length;	
			//计算订单路程长度
			let lon1=jsonData.startX;
			let lat1=jsonData.startY;
			let lon2=jsonData.desX;
			let lat2=jsonData.desY;
			let distance=getMapDistance(lat1,lon1,lat2,lon2);
			jsonData.distance=distance;
			//处理预约时间
			if(jsonData.is_reservation){
				let isoDateTime= jsonData.appointment_time.replace('T', ' ');
				jsonData.appointment_time=isoDateTime;
				jsonData.subtitle="预约单";
			}
			else jsonData.subtitle="实时单";
			jsonData.isHaveDriver=false;//订单刚生成没有司机接单
			jsonData.isReceivePassenger=false;//订单刚生成司机没接到乘客
			_this.requsetMessage.push({index,jsonData});
			_this.requsetMessage[index].title="订单"+(index+1);
			_this.requsetMessage[index].driverInfo={};
		},
		showDrawer(e) {
			
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
			console.log((type === 'showLeft' ? '左窗口' : '右窗口') + (e ? '打开' : '关闭'));
			this[type] = e
		},
		//用户叫车接口
		hailing(){
			if(this.start_name==='选择上车点'||this.des_name==='你要去哪儿'){
				uni.showToast({
					title:"请完善约车信息",
					icon:"error",
				})
				return
			}
			let isoDateTime='';
			// 将空格替换为 "T" 处理后端格式转换
			if(this.is_reservation===true){
				isoDateTime = this.appointment_time.replace(' ', 'T');
				console.log(isoDateTime); // 输出 "2023-08-11T14:41:27"
			}

			const requestData = {
			        // param1: 'value1',
			        // param2: 'value2',
			        // ...更多参数
					id:uni.getStorageSync("userId"),  //这里是用户的id，即user_id
					startX:this.covers[0].longitude,//39.90,乘客上车点的经度
					startY: this.covers[0].latitude,//116.39 纬度
					start_name:this.start_name,
					desX: this.covers[1].longitude,//39.90,乘客下车点的经纬度
					desY: this.covers[1].latitude,//116.39
					des_name:this.des_name,
					request_time: new Date(),//请求发起时间   年月日  时分秒
					is_reservation:this.is_reservation,//是否预约，
					appointment_time:isoDateTime,//预约起始时间 年月日  时分秒
					is_instead:this.is_instead,//是否代叫，
					customer_phone:this.customer_phone,//被代叫人手机号
					priority:0 //优先级 ??
			 };
			AndriodcustomerRequest(requestData).then((res)=>{
				if(res.data.success){
					uni.showToast({
						title:"正在约车"
					})
					_this.reqId=res.data.data;
					requestData.reqId=res.data.data;
					console.log(_this.reqId);
					this.StartText="继续约车";
					this.isStart=true;
					_this.InsertRequstMessage(requestData);
				
				}
				else{
					uni.showToast({
						icon:'error',
						title:res.data.errMsg
					})
					conlose.log(res.errorMsg);
				}
			});
		},
		//取消当前约车 和取消某一单是一个逻辑 
		cancelhailing(order){
			var requestData=[];
			//如果删除的不是最后一个
			if(order===''){
				order=_this.requsetMessage.length;
				order=order-1;
			}	
				
			requestData.push(_this.requsetMessage[order].jsonData.reqId);
		
			AndriodcancelRequest(this.userId,requestData).then((res)=>{
				// console.log(res);
				if(res.data.success){
					uni.showToast({
						title:"取消成功"
					})
					console.log(order);
					_this.requsetMessage.splice(order,1);
					console.log(_this.requsetMessage);
					
					_this.clean();
				}
				else{
					uni.showToast({
						icon:'error',
						title:"取消失败请重试",
						// title:res.errMsg
					})
					conlose.log(res.errorMsg);
				}
			})
			
		},
		clean(){
			this.reqId='';
			this.start_name='选择上车点';
			this.des_name='你要去哪儿';
			if(this.is_reservation){
				this.is_reservation=false;
				this.appointment_time='';
			}
			if(this.is_instead){
				this.is_instead=false;
				this.customer_phone='';
			}
			// TODO:删除当前约车订单!
			if(this.requsetMessage===''){
				this.isStart=false;//当前没有正在进行的订单
			}
			//上车点
			this.covers[0].longitude='';
			this.covers[0].latitude='';
			//下车点
			this.covers[1].longitude='';
			this.covers[1].latitude='';
		},
		copyExist(){
			console.log("number",this.customer_phone);
			if(this.customer_phone!=''){
				const phonePattern = /^1\d{10}$/;
				// 使用正则表达式进行验证
				let isValidPhoneNumber = phonePattern.test(this.customer_phone);
				if(isValidPhoneNumber===false){
					uni.showToast({
						title: "请输入有效的电话号码",
						icon: 'none',
						duration: 2000,
						
					  });
				}
				else if(isValidPhoneNumber===true){
					this.is_instead=true;
				}
			}
			else{
				this.is_instead=false;
			}
			
		},
		clear_copy(){
			this.is_instead=false;
		},
		clearInput: function(event) {
			this.customer_phone = event.detail.value;
			// if (event.detail.value.length > 0) {
			// 	this.showClearIcon = true;
			// } else {
			// 	this.showClearIcon = false;
			// }
		},
		clearIcon: function() {
			this.customer_phone = '';
			this.showClearIcon = false;
		},
		
	// 点击图标后显示时间选择器弹窗
		showTimePicker() {
			this.open_pop()
			// this.$refs.map.style.height = '400px';
			this.$refs.reser.open('bottom')
			this.datetimesingle=new Date();
			console.log(new Date());
		},
		closeTimePicker() {
			this.close_pop()
			this.$refs.reser.close();
		},
		// 用户选择时间后的回调
		onTimeSelected(value) {
		  console.log("Selected time:", value);
		  this.appointment_time=value;
		  if(this.appointment_time!=''){
			  this.is_reservation=true;
		  }
		  else{
			  this.is_reservation=false;
			}
		  
		  console.log("appointment_time:",this.appointment_time);
		  
		},
		
		//处理 代叫
		close_copy(){
			this.close_pop()
			this.$refs.popup.close('');
		},
		open() {
			this.open_pop()
			this.$refs.popup.open('bottom')
		},
		
		// 处理地图大小
		open_pop(){
			this.isPopupVisible=true;
		},
		close_pop(){
			this.isPopupVisible=false;
		},
		
		connectWSServer(){
			uni.connectSocket({
				url:androidWSPrefix+'/api/DispatchOrderServer/'+uni.getStorageSync("userId")
			})
			uni.onSocketOpen((res)=>{
				console.log("ws连接已打开,用户id:"+uni.getStorageSync("userId"))
			})
			uni.onSocketError((err)=>{
				console.log(err)
			})
			// console.log(uni.getStorageSync("role_id"))
			uni.onSocketMessage(function(res){
				console.log(res.data)
				//处理ws订单信息
				//_this.dealWSData(res.data);
			})
		},
		//预约车
		reservation(){
			console.log("预约")
		},
		//代叫
		copy(){
			console.log("代叫")
		},
		init(){

			uni.getLocation({
				type: 'gcj02', //返回可以用于uni.openLocation的经纬度
				geocode:true,//是否解析地址信息，仅app
				success: function (res) {
					var position = formatLocation(res.latitude,res.longitude);
					// console.log(position)
					_this.latitude = position.latitude;
					_this.longitude = position.longitude;
					_this.covers[2].latitude=res.latitude;
					_this.covers[2].longitude=res.longitude;
				},
				fail:function(res){
					console.log(res);
				}
			});
			
		},
		//上车点
		chooseLocation: function (index) {
			uni.chooseLocation({
				success: (res) => {
					this.hasLocation = true,
					this.location = formatLocation(res.longitude, res.latitude),
					this.locationAddress = res.name,
					this.latitude=res.latitude,
					this.longitude=res.longitude,
					this.start_name=res.name,
					this.covers[0].latitude=res.latitude,
					this.covers[0].longitude=res.longitude,
					this.polyline[0].points[0].latitude=res.latitude,
					this.polyline[0].points[0].longitude=res.longitude,
					console.log('位置名称：' + res.name);
					console.log('详细地址：' + res.address);
					console.log('纬度：' + res.latitude);
					console.log('经度：' + res.longitude);
					
				}
			})
		},
		//下车点：{不能用一个函数}
		chooseLocation2: function (index) {
			uni.chooseLocation({
				success: (res) => {
					this.hasLocation = true,
					this.location = formatLocation(res.longitude, res.latitude),
					// this.locationAddress = res.name,
					// this.latitude=res.latitude,
					// this.longitude=res.longitude,
					this.des_name=res.name,
					this.covers[1].latitude=res.latitude,
					this.covers[1].longitude=res.longitude,
					this.polyline[0].points[1].latitude=res.latitude,
					this.polyline[0].points[1].longitude=res.longitude,
					console.log('位置名称：' + res.name);
					console.log('详细地址：' + res.address);
					console.log('纬度：' + res.latitude);
					console.log('经度：' + res.longitude);
					
				}
			})
		},
	}
}
</script>

<style>
	@import "@/static/iconfont.css";
	.icon-list{
		display: flex;
		  justify-content:space-evenly; /* 控制 icon-with-text 元素之间的间距 */
	}
  .button-icon {
    width: 60px;
    height: 60px;
    margin-right: 10px;
  }
  
  .icon-with-text {
    display: flex;
     flex-direction: column;
	align-items: center;
  }
  
  .text {
     margin-top: 5px; /* 可调整图标和文字之间的间距 */
  }
  .example-body {
  		background-color: #fff;
  		padding: 10px;
  	}
.button-container {
  display: flex;
  justify-content: space-between; /* 将按钮平均分配容器的可用空间 */
}

.custom-button {
  width: 150px; /* 自定义按钮的宽度 */
  height: 35px; /* 自定义按钮的高度 */
  font-size: inherit; 
  /* font-size:  1.5em; /* 使用vw单位设置字体大小 */ 
}
</style>






