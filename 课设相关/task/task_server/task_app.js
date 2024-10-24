//web服务端框架express
const express = require('express')
const app = express()
//校验框架joi
const joi = require('joi')
//跨域配置
const cors = require('cors')
app.use(cors())
//表单解析中间件PostBody:application/x-www-form-urlencoded
app.use(express.urlencoded({extended:false}))

//常量配置文件
const config = require('./config')

//优化res.send
app.use(function(req,res,next){
    //status=config.resSuccess为成功,config.resFail为
    //默认失败,不失败自己去改
    res.cc = function(err,status=config.resFail){
        res.send
        (
            {
                status,
                //err可能是一个错误对象，但也可能是一个字符串
                //判断是否属于错误对象Error,属于发送消息，不属于直接发送字符串
                message:err instanceof Error?err.message:err
            }
        )
    }
    next()
})

//token中间件express-jwt
const {expressjwt:jwt} = require('express-jwt')

//权限中间件,user接口下的不需要权限
app.use
(jwt
    (
        {
            secret:config.jwtSecretKey,
            algorithms: ['HS256'] 
        }
    ).
    unless
    (
        {
            path:[/^\/api\/user/]
        }
    )
)

//用户路由模块
const userRouter = require('./router/userRouter')
app.use('/api/user',userRouter)

//任务调度模块
const taskScheduler = require('./router/taskSchedulerRouter')
app.use('/api/task',taskScheduler)

//错误级别中间件
app.use((err,req,res,next)=>{
    //表单验证不通过
    if(err instanceof joi.ValidationError){
        return res.cc(err)
    }
    //无权限错误
    else if(err.name=='UnauthorizedError'){
        console.log(err);
        return res.cc('UnAuthoriaed')
    }
    //未知错误
    else{
        return res.cc(err)
    }
})

app.listen(9090,()=>{
    console.log('task_server running at localhost:9090');
})