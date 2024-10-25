/**
 * @description 自定义路由拦截
 */
import {store} from "../store/index.js"
import { tokenExpireTime } from "../config/redisConfig";

// 白名单
const whiteList = [
	// 注意入口页必须直接写 '/'
	// 支持正则表达式   { pattern: /^\/pages\/list.*/ }, 
	'/pages/index',
	'/pages/login/login',
	'/pages/login/forget',
	'/pages/login/register',
	'/pages/testTemp/mapTest',
	'/pages/driver/uploadLicense',
	'/pages/customer/evaluateOrder',
]
 
export default async function() {
	const list = ['navigateTo', 'redirectTo', 'reLaunch', 'switchTab']
	// 用遍历的方式分别为,uni.navigateTo,uni.redirectTo,uni.reLaunch,uni.switchTab这4个路由方法添加拦截器
	list.forEach(item => {
		uni.addInterceptor(item, {
			invoke(e) {
				let latest = uni.getStorageSync('latest');
				let loggedIn = true;
				if(latest===null||latest==undefined){
					loggedIn=false;
				}
				else if(Date.now()-tokenExpireTime>=latest){
					store.dispatch("logOutCommit").catch(
						(err)=>{
							console.log(err);
						}
					)
					loggedIn = false;
				}
				const url = e.url.split('?')[0]
				var flag = uni.getStorageSync('loggedIn') && loggedIn
				// console.log(uni.getStorageSync("userId"))
				// console.log(flag)
				// console.log(url)
				// console.log(e.url)
				if(!flag){
					let pass = false;
					for(var i=0;i<whiteList.length;i++){
						if(whiteList[i].search(url)!=-1){
							pass=true;
							break;
						}
					}
					if(!pass){
						uni.redirectTo({
							url:"/pages/login/login"
						})
					}
				}
			},
			fail(err) { // 失败回调拦截
				console.log(err)
			}
		})
	})
}