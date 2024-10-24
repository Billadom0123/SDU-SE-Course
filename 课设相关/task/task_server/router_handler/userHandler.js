//数据库连接对象
const {db} = require('../db/datasource')
//加密模块（MD5）
const bcrypt = require('bcryptjs')
//jwt
const jwt = require('jsonwebtoken')
//密钥配置
const config = require('../config')

//用户注册
exports.register=(req,res)=>{
    //获取请求体
    const userinfo = req.body
    //检查用户名是否存在
    const sql = 'select * from task_user where username=?'

    db.query(sql,userinfo.username,(err,results)=>{
        if(err){
            return res.cc(err)
        }
        if(results.length>0){
            return res.cc('用户名被占用,请更换其他用户名')
        }
        //没有被占用
        userinfo.password = bcrypt.hashSync(userinfo.password,10)//10加盐长度
        const insertUser = 'insert into task_user set ?'
        db.query(insertUser,{username:userinfo.username,password:userinfo.password},(err,results)=>{
            if(err){
                return res.cc(err)
            }
            //判断影响行数
            if(results.affectedRows!==1){
                return res.cc('注册用户失败,请稍微再试')
            }
            return res.cc('注册用户成功',0)
        })
    })
}

//用户登录
exports.login = (req,res)=>{
    const userinfo = req.body
    const loginSql = 'select * from task_user where username=?'
    db.query(loginSql,userinfo.username,(err,results)=>{
        if(err){
            return res.cc(err)
        }
        //没这个人
        if(results.length!==1){
            return res.cc('该用户不存在')
        }
        //判断密码是否正确
        const cmpResult = bcrypt.compareSync(userinfo.password,results[0].password)
        if(!cmpResult){
            return res.cc('密码错误')
        }
        else{
            const user = {...results[0],password:'',avatar:'',isVIP:''}
            //token加密
            const token = jwt.sign(user,config.jwtSecretKey,{
                expiresIn:7*24+'h'
            })
            res.send({
                status:0,
                message:'登陆成功',
                token:'Bearer '+token
            })
        }
    })
}

//更新用户信息
exports.updateUserinfo = (req,res)=>{
    const userinfo = req.body
    const sql = 'update task_user set ? where id = ?'
    db.query(sql,[userinfo,userinfo.id],(err,result)=>{
        if(err){
            res.cc(err)
        }
        if(result.affectedRows!==1){
            res.cc('更新用户信息失败')
        }
        res.cc('更新用户信息成功',0)
    })
}

//重置用户密码
exports.updatePassword = (req,res)=>{
    const selectPwdById = 'select * from task_user where id = ?'
    db.query(selectPwdById,req.user.id,(err,results)=>{
        if(err){
            return res.cc(err)
        }
        //判断结果是否存在
        if(results.length!==1){
            return res.cc('密码不存在')
        }
        //用户输入的旧密码是否正确
        const cmpResult = bcrypt.compareSync(req.body.oldPwd,results[0].password)
        if(!cmpResult){
            return res.cc('旧密码错误')
        }
        //更新密码
        const updatePwdById = 'update task_user set password = ? where id = ?'
        //密码加密
        const MD5PWD = bcrypt.hashSync(req.body.newPwd,10)
        db.query(updatePwdById,[MD5PWD,req.user.id],(err,results)=>{
            if(err){
                return res.cc(err)
            }
            if(results.affectedRows!==1){
                return res.cc('更新密码失败')
            }
            res.cc('更新密码成功',0)
        })
    })
}
