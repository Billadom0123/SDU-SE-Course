// import { showToast } from 'vant';
import {host,androidHostPrefix} from '@/config/systemConfig.js'

function loginReq(phone,password,checkCode,role_id){
    //后端解码的是json对象格式的数据
    let body = {
        "phone":phone,
        "code":checkCode,
        "password":password,
        "role_id":role_id
    }
	// console.log(JSON.stringify(body))
	return uni.request({
		url:'/api/user/login',
		method:'POST',
		data:JSON.stringify(body)
	})
}

function AndroidLoginReq(phone,password,checkCode,role_id){
	let body = {
	    "phone":phone,
	    "code":checkCode,
	    "password":password,
	    "role_id":role_id
	}
	return uni.request({
		url:androidHostPrefix+'/api/user/login',
		// header:{
		// 	'Access-Control-Allow-Origin': '*'
		// },
		method:'POST',
		data:JSON.stringify(body)
	})
}

function registerReq(name,phone,checkCode,password,role_id){
    let body ={
        "name":name,
        "phone":phone,
        "code":checkCode,
        "password":password,
        "role_id":role_id
    }
	return uni.request({
		url:'/api/user/register',
		method:'POST',
		data:JSON.stringify(body)
	})
}

function AndroidRegisterReq(name,phone,checkCode,password,role_id){
	let body ={
	    "name":name,
	    "phone":phone,
	    "code":checkCode,
	    "password":password,
	    "role_id":role_id
	}
	return uni.request({
		url:androidHostPrefix+'/api/user/register',
		method:'POST',
		data:JSON.stringify(body)
	})
}

export 
{
    loginReq,
    registerReq,
	AndroidLoginReq,
	AndroidRegisterReq
}