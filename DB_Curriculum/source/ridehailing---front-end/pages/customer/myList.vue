<template>
	<view class="container">
		<br>
		<view class="center-text" v-if="isHaveOrder">
		    <text  class="no-order-text" >暂时没有您的订单哦~</text>
		 </view>
		 <uni-card v-for="(item, index) in orderList" :key="index" 
		 :title="item.title" 
		 :sub-title="item.subtitle"
			:extra="item.extra">
		      <text class="uni-body">{{ item.content }}</text>
			  <view class="button-container">
				  <button class="custom-button" @click="deleteOrder(index)">删除订单</button>
				  <button class="custom-button" @click="callDriver(index)">联系司机</button>
				  <uni-icons type="compose" @click="showComment(index)" size="30"></uni-icons>
				  <!-- <button class="custom-button" >评价</button> -->
				  <!-- 如何让已经完成的评价不显示？ -->
			  </view>
		</uni-card>
	</view>
	
</template>

<script>
	let _this=this;
	import {AndriodShowOrder,AndriodDeleteOrder} from '../../service/genService.js'
	export default {
		data() {
			return {
				orderList:[],
				userId:'',
				role:1,//1乘客 2司机
				isHaveOrder:true
			}
		},
		mounted() {
			_this = this,
			this.getOrderList();
		},
		computed:{
			isHaveOrderMethod(){
				 return _this.isHaveOrder;
			}
		},
		methods: {
			//回显评价
			showComment(index){
				let _this=this;
				this.userId=uni.getStorageSync("userId");
				let currentOrder = _this.orderList[index];
				uni.showModal({
					content:'评价提醒',
					confirmText:'前往评价',
					success: function (res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: './listEvaluateOrder?id=' + _this.userId + '&reqId=' + currentOrder.reqId + '&driverName=' + currentOrder.driverName, 
									success: function(res) {
										console.log("跳转成功");
									  }
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
				})
			},
			deleteOrder(index){
				uni.showModal({
					title:'温馨提示',
					content:'删除后将无法查看订单内容，确认删除吗？',
					success: function (res) {
							if (res.confirm) {
								console.log('用户点击确定');
								_this.deleteOrderDetail(index);
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
				})
				
			},
			deleteOrderDetail(index){
				console.log(_this.orderList[index].indentId,_this.role);
				AndriodDeleteOrder(_this.orderList[index].indentId,_this.role).then((res)=>{
					if(res.data.success){
						uni.showToast({
							title:'删除成功'
						})
						console.log('删除成功');
						
						_this.orderList.splice(index,1);
						if(_this.orderList.length===0){
							_this.isHaveOrder=true;
						}
					}
					else{
						uni.showToast({
							title:'删除失败'
						})
						console.log(res.data.errorMsg);
					}
				})
			},
			getOrderList(){
				this.userId=uni.getStorageSync("userId");
				AndriodShowOrder(uni.getStorageSync("userId"),this.role).then((res)=>{
					if(res.data.success){
						console.log(res.data.data);
						_this.isHaveOrder=false;
						_this.dealData(res.data.data);
					}
					else{
						console.log(res.data.errorMsg);
					}
				})
			},
			changeTime(localTime){
				let isoDateTime=localTime.replace('T', ' ');
				return isoDateTime;
			},
			dealData(resdata){
				// this.orderList=resdata;
				console.log(resdata.length);
				for(let i=0;i<resdata.length;i++){
					let currentOrder={};
					currentOrder.indentId=resdata[i].indentId;;
					currentOrder.reqId=resdata[i].reqId;
					var str;
					let price2=resdata[i].price.toFixed(2);  
					currentOrder.extra="￥"+price2;//但是平台有赚到钱，这笔钱应该不全是司机的吧
					console.log(currentOrder);
					currentOrder.title="订单"+(i+1);
					console.log(resdata[i].appointTime);
					if("appointTime" in resdata[i]){
						currentOrder.subtitle="预约单";console.log(currentOrder);
						currentOrder.appointTime=_this.changeTime(resdata[i].appointTime);
						
					}
					else{
						console.log(currentOrder);currentOrder.subtitle="实时单";
					}
					
					currentOrder.startTime=_this.changeTime(resdata[i].startTime);
					currentOrder.endTime=_this.changeTime(resdata[i].endTime);
					// console.log(currentOrder);
					//预约单:
					if("appointTime" in resdata[i]){
						str='出发时间: '+currentOrder.appointTime+'\n'+
							'出发地点: '+resdata[i].startName+'\n'+
							'到达地点: '+resdata[i].endName+'\n'+
							'开始行程: '+currentOrder.startTime+'\n'+
							'结束行程: '+currentOrder.endTime;
					}
					else{
						currentOrder.responseTime=_this.changeTime(resdata[i].responseTime);
						str='下单时间: '+currentOrder.responseTime+'\n'+
						'出发地点: '+resdata[i].startName+'\n'+
						'到达地点: '+resdata[i].endName+'\n'+
						'开始行程: '+currentOrder.startTime+'\n'+
						'结束行程: '+currentOrder.endTime;
					}
			
					
					// console.log(currentOrder);
					currentOrder.content=str;
					currentOrder.driverName=resdata[i].driverName;
					currentOrder.number=resdata[i].number;
					currentOrder.phone=resdata[i].driverPhone;
					console.log(currentOrder.number);
					this.orderList.push(currentOrder);
					console.log(currentOrder);
				}
				console.log(this.orderList);
			},
			callDriver(index){
				uni.makePhoneCall({
					phoneNumber:_this.orderList[index].phone,//手机号还没传入捏
					success: (res) => {
						console.log('调用成功!')	
						},
					// 失败回调
					fail: (res) => {
						console.log('调用失败!')
						}
				})
			}
		}
	}
</script>

<style>
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
.center-text {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 控制文本在屏幕垂直方向上的居中 */
}

.no-order-text {
  font-size: 20px; /* 控制文本的字体大小 */
}
</style>