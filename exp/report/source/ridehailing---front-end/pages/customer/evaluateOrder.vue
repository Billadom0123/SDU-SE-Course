<template>
	<view class="uni-container">
		<uni-section title="评分" type="line" padding>
			<uni-rate v-model="rateValue" @change="onChange" />
		</uni-section>
		<uni-section title="评论" :subTitle="'司机:'+driverName" type="line" padding>
			<uni-easyinput type="textarea" v-model="contentValue" placeholder="请输入评价内容,不多于50字"></uni-easyinput>
		</uni-section>
		<button style="float: right;" @click="submitEvaluation()">提交评价</button>
	</view>
</template>

<script>
	let _this=this;
	import {AndriodCommentOrder} from '../../service/genService.js'
	export default{
		data(){
			return{
				id:'',
				reqId:'',
				driverName:'',
				rateValue: 0,
				contentValue:''
			}
		},
		onLoad: function (option) { //option为object类型，会序列化上个页面传递的参数
		_this=this;
				this.id=option.id;
				this.reqId=option.reqId;
				this.driverName=option.driverName;
				console.log(option.id); //打印出上个页面传递的参数。
				console.log(option.reqId); //打印出上个页面传递的参数。
		},
		methods: {
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e));
				// console.log(this.rateValue);
			},
			submitEvaluation(){
				if(_this.rateValue===0||_this.contentValue===''){
					uni.showModal({
						content:'评分与评论均不可为空！'
					})
					return;
				}
				if(_this.contentValue.length>50){
					uni.showModal({
						content:'评论不能超过50字！'
					})
					return;
				}
				AndriodCommentOrder(_this.id,_this.reqId,_this.rateValue,_this.contentValue).then((res)=>{
					if(res.data.success){
						uni.showToast({
							title:'提交成功'
						})
					}
					else{
						console.log(res.data.errorMsg)
					}
				})
			}		
		}
	}
</script>

<style>
</style>