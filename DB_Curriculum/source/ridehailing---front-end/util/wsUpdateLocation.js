function updateDriverLocation(curX,curY){
	var uid = uni.getStorageSync("userId")
	var orderId = uni.getStorageSync("orderId")
	console.log(curX+"  "+curY)
	var msg = {
		"id":uid,
		"orderId":orderId,
		"curX":curX,
		"curY":curY
	}
	return uni.sendSocketMessage({
		data: JSON.stringify(msg)
	})
}

export{
	updateDriverLocation
}