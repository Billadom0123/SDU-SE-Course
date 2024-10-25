import App from './App';
import { createSSRApp } from 'vue'
import {store} from "./store"
import myEvent from '@/eventBus/eventBus.js'
import { tokenExpireTime } from './config/redisConfig.js'
// import VConsole from 'vconsole';



// #ifndef VUE3
// import Vue from 'vue'
import './uni.promisify.adaptor'
// Vue.use(router)//
Vue.config.productionTip = false
App.mpType = 'app'


const app = new Vue({
  ...App
  render: h => h(App),
    // 注册路由
    // router
})
// app.use(router);
app.$mount()
// #endif



// uni.$on('beforeRouterEnter',(to,from,next)=>{
// 	//检查token是否过期
// 	// var latest = store.state.latest;
// 	var latest = uni.getStorageSync("latest")
// 	var loggedIn = true;
// 	if(latest===null||latest===undefined){
// 		loggedIn = false;
// 	}
// 	else if(Date.now()-tokenExpireTime>=latest){
// 		store.dispatch("logOutCommit").catch(
// 			(err)=>{
// 				console.log(err);
// 			}
// 		)
// 		loggedIn = false;
// 	}
// 	// var loggedIn = store.state.loggedIn && loggedIn
// 	var flag = uni.getStorageSync('loggedIn') && loggedIn
// 	const authPages = ['/login','/register']
// 	//去的是不是login
// 	const authRequired = authPages.includes(to.path)
// 	console.log(authRequired)
// 	if(!flag){
// 		//检查是否去不是login的(需要登录的)
// 		if(!authRequired){
// 			return next('/login')
// 		}
// 		 //不去，说明是去login ('/' 或 '/login')
// 		else{
// 			next()
// 		}
// 	}

// 	else{
// 		next()
// 	}
// })


//如果不是生产环境并且不是pc设备那么就显示调试
// if (process.env.NODE_ENV != "prod" && !isPc()) {
//     console.log(process.env.NODE_ENV);
//     const vConsole = new VConsole();
// }
// console.log("hello");
// vConsole.destroy();

// #ifdef VUE3
export function createApp() {
  const app = createSSRApp(App)
  app.config.warnHandler = () => null
  app.config.globalProperties.$event = myEvent
	app.use(store)

  return {
    app,
  };
}
// #endif