import {store} from '@/store/index'
import {host,androidHostPrefix,androidWSPrefix,amapWebKey,amapDrivingRouteURL} from '@/config/systemConfig.js'

function genRequest(urlStr,data) {
	return uni.request({
		url:urlStr,
		method:'POST',
		header: {
			'content-type': 'application/json',
			'Access-Control-Allow-Origin': '*',
			'authorization': uni.getStorageSync("token")
		},
		data:JSON.stringify(data)
	})
}

function genAndroidRequest(urlStr,data){
	return uni.request({
		url:androidHostPrefix+urlStr,
		method:'POST',
		header:{
			'content-type': 'application/json',
			// 'Access-Control-Allow-Origin': '*',
			'authorization': uni.getStorageSync("token")
		},
		data:JSON.stringify(data)
	})
}

function genAndroidWSConnect(urlStr){
	return uni.connectSocket({
		url:androidWSPrefix+urlStr,
		// header: {
		// 		'content-type': 'application/json'
		// 	},
	})
}

function genAndroidRequestGet(urlStr, params) {
  return uni.request({
    url: androidHostPrefix + urlStr,
	method:'GET',
    data: params ,// 传递查询参数对象
	header:{
		'content-type': 'application/json',
		'authorization': uni.getStorageSync("token")
	}
  });
}

function getCheckCodeReq(phone){
    return genRequest('/api/user/checkCode',{"phone":phone})
}

function getAndroidCheckCodeReq(phone){
	return genAndroidRequest('/api/user/checkCode',{"phone":phone})
}

//上传驾照
function AndroidUploadLicense(uid,license){
	return genAndroidRequest('/api/driver/registerLicense',
		{
			"id":uid,
			"license":license
		}
	)
}

//完善车辆信息 /vehicle/register
function AndroidRegisterVehicle(uid,number,color,type,brand){
	return genAndroidRequest('/api/vehicle/register',
		{
			"id":uid,
			"number":number,//车牌号
			"color":color,//车辆颜色
			"type":type,//车辆对应驾照类型
			"brand":brand//车辆品牌
		}
	)
}

//司机开始听单 
function AndroidStartBusiness(uid,curX,curY){
	return genAndroidRequest('/api/driver/startBusiness',
		{		
			"id":uid,
			"curX":curX,
			"curY":curY
		},
	)
}

//司机结束听单（是否接单）
function AndroidEndBusiness(id){
	return genAndroidRequestGet('/api/driver/endBusiness',
		{		
			"id":id,
		},
	)
}

//司机确认订单（是否接单）
function AndroidconfirmOrder(id,requestId,accept){
	return genAndroidRequestGet('/api/driver/confirmOrder',
		{		
			"id":id,
			"requestId":requestId,
			"accept":accept
		},
	)
}

//司机接到乘客
function AndroidReceivePassenger(userId,reqId){
	return genAndroidRequestGet('/api/driver/receivePassenger',
		{		
			"id":userId,
			"reqId":reqId
		},
	)
}

//删除订单
function AndroidDeleteOrder(id,requestList,idle){
	return genAndroidRequest('/api/driver/deleteOrder',
		{		
			"id":id,//userId
			"requestList":requestList,//删除订单列表
			"idle":idle//boolean值,true代表这个司机此时应该为空闲状态,false代表这个司机应该为忙碌状态
		},
	)
}

//完成订单
function AndroidCommitOrder(id,reqId,endX,endY){
	return genAndroidRequest('/api/driver/commitOrder',
		{		
			"id":id,//userId
			"reqId":reqId,//对应请求的id
			"endX":endX,//结束位置经度
			"endY":endY//结束位置纬度 latitude
		},
	)
}

//用户叫车
function AndriodcustomerRequest(requestData){
	return genAndroidRequest('/api/request/customerRequest',
		{
			// "requestData":requestData
			"id": requestData.id, //这里是用户的id，即user_id
			"startX": requestData.startX,//39.90,乘客上车点的经纬度
			"startY": requestData.startY,//116.39
			"start_name":requestData.start_name,
			"desX": requestData.desX,//39.90,乘客下车点的经纬度
			"desY": requestData.desY,//116.39
			"des_name": requestData.des_name,
		    "request_time": requestData.request_time,//请求发起时间   年月日  时分秒
			"is_reservation":requestData.is_reservation,//是否预约，
			"appointment_time":requestData.appointment_time,//预约起始时间 年月日  时分秒
			"is_instead":requestData.is_instead,//是否代叫，
			"customer_phone":requestData.customer_phone,//被代叫人手机号
			"priority": requestData.priority //优先级
		},
	)
}

//用户取消用车
function AndriodcancelRequest(userId,requestData){
	return genAndroidRequest('/api/request/cancelRequest',
		{
			"id":userId,
			"requestList":requestData
		}
	)
}

//乘客评价已经完成的订单
function AndriodCommentOrder(userId,reqId,comment,content){
	return genAndroidRequest('/api/comment/commentOrder',
		{
			"id":userId,
			"reqId":reqId,
			"comment":comment,
			"content":content
		}
	)
}


//回显评价
function AndriodShowComment(userId,reqId){
	return genAndroidRequestGet('/api/comment/showComment',
		{
			"id":userId,
			"reqId":reqId
		}
	)
}

// 返回司机，乘客已经完成的订单列表
function AndriodShowOrder(userId,role){
	return genAndroidRequestGet('/api/indent/showOrder',
		{
			"id":userId,
			"role":role
		},
	)
}

//司机 乘客删除某个已经完成的订单 
function AndriodDeleteOrder(indentId,role){
	return genAndroidRequest('/api/indent/deleteOrder',
		{
			"indentId":indentId,
			"role":role
		},
	)
}

//路线规划
function getDrivingRoute(origin,destination,wayPoints){
	let wayPointStr = wayPoints.longitude+','+wayPoints.latitude
	console.log(wayPointStr)
	let originStr = origin.longitude+','+origin.latitude
	let destinationStr = destination.longitude+','+destination.latitude
	console.log(originStr)
	console.log(destinationStr)
	return uni.request({
		url:amapDrivingRouteURL+'?origin='+originStr+'&destination='+destinationStr+'&waypoints='+wayPointStr+'&extensions=all'+'&key='+amapWebKey
	})
}



export{
    getCheckCodeReq,
	getAndroidCheckCodeReq,
	
	AndroidUploadLicense,
	AndroidRegisterVehicle,
	AndroidStartBusiness,
	AndroidEndBusiness,
	AndroidconfirmOrder,
	AndroidReceivePassenger,
	AndroidDeleteOrder,
	AndroidCommitOrder,
	getDrivingRoute,
	
	AndriodcustomerRequest,
	AndriodcancelRequest,
	AndriodCommentOrder,
	AndriodShowComment,
	
	AndriodShowOrder,
	AndriodDeleteOrder,
	
	genAndroidWSConnect
}