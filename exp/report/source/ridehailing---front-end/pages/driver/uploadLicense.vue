<template>
	<uni-section title="驾照类型" type="line">
		<view class="example-body">
			<uni-combox :candidates="candidates" placeholder="请选择您的驾照类型" v-model="license_choice"></uni-combox>
			<view class="result-box">
				<text style="float: left;">所选驾照类型为：{{license_choice}}</text>
				<button class="custom-button" @click='uploadLicense' type="primary"> 确认上传</button>
			</view>
		</view>
	</uni-section>
</template>

<script>
	import {AndroidUploadLicense} from '../../service/genService.js'
	export default {
		data() {
			return {
				
				candidates: ['C1', 'C2'],
				license_choice: ''
			}
		},
		mounted() {
			
		},
		methods: {
			
			uploadLicense(){
				let uid = uni.getStorageSync("userId")
				AndroidUploadLicense(uid,this.license_choice).then(
					(res)=>{
						if(res.data.success){
							uni.showToast({
								title:'上传驾照成功'
							})
						}
						else{
							uni.showToast({
								title: res.data.errorMsg
							})
							
						}
					}
				)
			}
		}
	}
</script>

<style>
.example-body {
		padding: 12px;
		background-color: #FFFFFF;
	}

	.result-box {
		/* display: flex; */
		text-align: center;
		padding: 20px 0px;
		font-size: 16px;
	}
	
	.custom-button {
	  width: 100px; /* 自定义按钮的宽度 */
	  height: 35px; /* 自定义按钮的高度 */
	  font-size: inherit; 
	  float: right;
	  /* font-size:  1.5em; /* 使用vw单位设置字体大小 */ 
	}
</style>
