var socketOpen

function connectToWSServer(serverEndPoint){
	uni.connectSocket({
		url:serverEndPoint
	})
	
	uni.onSocketOpen(function (res){
		console.log('ws连接成功,用户id:'+uni.getStorageSync("userId"))
		socketOpen = true
	})
	
	uni.onSocketError(function(res){
		console.log(res)
		socketOpen = false;
	})
	
	// uni.onSocketMessage(function(res){
	// 	console.log(res)
	// })
	
	return socketOpen
}

export{
	connectToWSServer
}