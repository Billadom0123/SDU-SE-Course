
const getlocation = (opt) => {
	return new Promise((resolve, reject) => {
		
		//app开始
		// #ifdef APP-PLUS
		uni.showLoading({
			title: '获取信息中'
		});
		uni.getLocation({
			// map组件默认为国测局坐标gcj02，调用 uni.getLocation返回结果传递给组件时，需指定 type 为 gcj02 
			type: 'gcj02',
			geocode: true,
			success: function(data) {
				resolve(data)
				console.log(data)
			},
			fail: function(err) {
				reject(err)
			},
			complete() {
				uni.hideLoading();
			}
		})
		// #endif
		//app结束
		
		
	})
};
export default {
	getlocation: getlocation
}