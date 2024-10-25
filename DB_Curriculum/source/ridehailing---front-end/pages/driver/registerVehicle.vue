<template>
	<view class="example">
		<!-- 自定义表单校验 -->
		<br>
		<uni-forms ref="customForm" labelPosition='top' :rules="customRules" :modelValue="customFormData" >
			<uni-forms-item label="车牌号" required name="number">
				<uni-easyinput v-model="customFormData.number" placeholder="请输入车牌号" />
			</uni-forms-item>
			<uni-forms-item label="车辆颜色" required name="color">
				<uni-easyinput v-model="customFormData.color" placeholder="请输入车辆颜色" />
			</uni-forms-item>
			<uni-forms-item label="驾照类型" required name="type">
				<uni-data-checkbox v-model="customFormData.type" :localdata="types" />
			</uni-forms-item>
			<uni-forms-item label="车辆品牌" required name="brand">
				<uni-easyinput v-model="customFormData.brand" placeholder="请输入车辆品牌" />
			</uni-forms-item>
		</uni-forms>
		<button type="primary" @click="submitRef()">提交</button>
	</view>
	
</template>
<!-- number,//车牌号
	color,//车辆颜色
	type,//车辆对应驾照类型
	brand//车辆品牌 -->
<script>
	let _this = this;
	import {AndroidRegisterVehicle} from '../../service/genService.js'
	export default{
		
		data(){
			return{
				types: [{
					text: 'C1',
					value:0
				}, {
					text: 'C2',
					value: 1
				}],
				customFormData: {
					number: '',
					color: '',
					type:0,
					brand:''
				},
				// 自定义表单校验规则
				customRules: {
					number: {
						rules: [{
							required: true,
							errorMessage: '车牌号不能为空'
						},
						{
							 // 车牌号正则表达式（普通格式和新能源车格式）
							pattern:/^(京|津|冀|晋|蒙|辽|吉|黑|沪|苏|浙|皖|闽|赣|鲁|豫|鄂|湘|粤|桂|琼|渝|川|贵|云|藏|陕|甘|青|宁|新)[A-HJ-NP-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1}$/,
							errorMessage:'车牌号格式错误'
						}]
					},
					color: {
						rules: [{
							required: true,
							errorMessage: '车辆颜色不能为空'
						}]
					},
					type: {
						rules: [{
								required: true,
								errorMessage: '车辆对应驾照类型不能为空'
							}]
					},
					brand: {
						rules: [{
							required: true,
							errorMessage: '车辆品牌不能为空'
						}]
					},

				},
			}
		},
		onReady() {
			_this=this;
				// 设置自定义表单校验规则，必须在节点渲染完毕后执行
			this.$refs.customForm.setRules(this.customRules)
		},
		methods:{
			submitRef() {
				console.log(_this.types[_this.customFormData.type].text);
				this.$refs['customForm'].validate().then(res => {
					console.log('success', res);
					let uid = uni.getStorageSync("userId");
					AndroidRegisterVehicle(uid,_this.customFormData.number,
											_this.customFormData.color,
											_this.types[_this.customFormData.type].text,
											_this.customFormData.brand).then((res)=>{
						if(res.data.success){
							uni.showToast({
								title: '提交成功'
							})
						}
						else{
							uni.showModal({
								content:res.data.errorMsg
							})
							console.log(res.data.errorMsg);
						}
						
					})
				}).catch(err => {
					console.log('err', err);
				})
			},

		}
	}
	
</script>

<style>
	.example {
			padding: 15px;
			background-color: #fff;
		}
	.form-item {
			display: flex;
			align-items: center;
		}


</style>