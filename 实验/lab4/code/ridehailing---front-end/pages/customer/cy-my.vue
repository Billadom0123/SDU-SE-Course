<template>
	<view class="header">
		<view class="uesr">
			<view class="top-xh">
				<view class="h2">
					个人中心
				</view>
				<view class="pic">
					<image src="../../static/cy-my/logout.png" style="width: 40rpx;" mode="widthFix"  @click="logout"></image>
				</view>
			</view>
			<!-- <view class="fot-xh">
				<navigator url="" hover-class="none">
					<view class="pic">
						<image src="../../static/cy-my/pic-m1.png" style="width: 130rpx;" mode="widthFix">
						</image>
					</view>
					<view class="txt">
						<view class="name">
							<view class="h3">
								用户昵称
							</view>
							<view class="phone">
								178****6589
							</view>
						</view>
					</view>
				</navigator>
			</view> -->
		</view>

		<view class="ul-list1-xh">
			<view class="li">
				<navigator url="./myList" hover-class="none">
					<view class="pic">
						<image src="../../static/cy-my/dingdan.png" style="width: 33rpx;  height:45rpx;" mode="widthFix">
						</image>
					</view>
					<view class="txt">
						<text>我的订单</text>
					</view>
				</navigator>
			</view>
			<view class="li">
				<navigator url="" hover-class="none">
					<view class="pic">
						<image src="../../static/cy-my/yijian.png" style="width: 35rpx;" mode="widthFix"></image>
					</view>
					<view class="txt">
						<text>意见反馈</text>
					</view>
				</navigator>
			</view>
			<view class="li">
				<navigator url="" hover-class="none">
					<view class="pic">
						<image src="../../static/cy-my/guanyhu.png" style="width: 35rpx;" mode="widthFix"></image>
					</view>
					<view class="txt">
						<text>关于我们</text>
					</view>
				</navigator>
			</view>
			<view class="li">
				<navigator url="" hover-class="none">
					<view class="pic">
						<image src="../../static/cy-my/shehzi.png" style="width: 35rpx;" mode="widthFix"></image>
					</view>
					<view class="txt" style="border-bottom: none;">
						<text>设置</text>
					</view>
				</navigator>
			</view>
		</view>
	</view>
</template>

<script>
	import {store} from "../../store/index.js"
	import {AndroidEndBusiness} from '../../service/genService.js'
	export default {
		data() {
			return {
				userId:'',
			}
		},
		onLoad() {
	
		},
		methods: {
			logout(){
				let _this=this;
				_this.userId=uni.getStorageSync("userId");
				uni.showModal({
					title: '提示',
					content:'确认退出当前账号吗？',
					success:function(res){
						if(res.confirm){
							_this.$store.dispatch("logOutCommit");
							uni.reLaunch({ url: '../index' })
						}
						if(res.cancel){
							console.log("取消退出");
						}
					}
				})
			}
		}
	}
</script>

<style>
	@import url("cy-my.css");
</style>
