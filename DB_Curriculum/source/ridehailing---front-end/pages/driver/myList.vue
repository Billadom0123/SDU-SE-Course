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
				  <button class="custom-button" @click="callCustomer(index)">联系乘客</button>
			  </view>
		</uni-card>
	</view>
	
</template>

<script>
	let _this=this;
	import {AndriodShowOrder,AndriodDeleteOrder} from '../../service/genService.js'
	import {truncateDecimal} from '../../util/formatter.js'
	export default {
		data() {
			return {
				orderList:[],
				userId:'',
				role:2,//1乘客 2司机
				isHaveOrder:true
			}
		},
		mounted() {
			_this = this,
			_this.getOrderList();
		},
		computed:{
			isHaveOrderMethod(){
				 return _this.isHaveOrder;
			}
		},
		methods: {
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
					var str;
					currentOrder.indentId=resdata[i].indentId;
					let price2=resdata[i].price.toFixed(2);  //truncateDecimal(resdata[i].price,2);
					currentOrder.extra="￥"+price2;
					console.log(currentOrder);
					currentOrder.title="订单"+(i+1);
					if("appointTime" in resdata[i]){
						currentOrder.subtitle="预约单";
						
					}
					else{
						currentOrder.subtitle="实时单";
					}
					currentOrder.receiveTime=_this.changeTime(resdata[i].receiveTime);
					// console.log(currentOrder);
					currentOrder.startTime=_this.changeTime(resdata[i].startTime);
					currentOrder.endTime=_this.changeTime(resdata[i].endTime);
					// console.log(currentOrder);
					str='接单时间: '+currentOrder.receiveTime+'\n'+
						'上车点: '+resdata[i].startName+'\n'+
						'下车点: '+resdata[i].endName+'\n'+
						'开始行程: '+currentOrder.startTime+'\n'+
						'结束行程: '+currentOrder.endTime;
					// console.log(currentOrder);
					currentOrder.content=str;
					currentOrder.customerName=resdata[i].customerName;
					currentOrder.phone=resdata[i].customerPhone;
					this.orderList.push(currentOrder);
					console.log(currentOrder);
				}
				console.log(this.orderList);
			},
			callCustomer(index){
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