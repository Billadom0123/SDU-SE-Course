import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {store} from './store'
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import './components/css/common.css'

router.beforeEach((to,from,next)=>{
    //有没有登录
    var loggedIn = store.state.loggedIn
    const authPages = ['/login']
    //去的是不是login
    const authRequired = authPages.includes(to.path)

    // console.log(to.path); // '/login'

    //如果没登录
    if(!loggedIn){
        //检查是否去不是login的(需要登录的)
        if(!authRequired){
            return next('/login')
        }
        //不去，说明是去login ('/' 或 '/login')
        else{
            next()
        }
    }
    //登录了 直接跳转首页
    else{
        //如果要去登录页 直接跳转首页
        if(authRequired){
            next('/task')
        }
        //访问其他资源 允许访问
        else{
            next()
        }
    }
})

const app = createApp(App)
app.use(ElementPlus).use(store).use(router).mount('#app')
