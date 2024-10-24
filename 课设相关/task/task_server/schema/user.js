const joi = require('joi')

//id 数字+整形+最少为1+必填项
const id = joi.number().integer().min(1).required()
//用户名 字符串+数字字母+最短长度1+最长长度10+必填项
const username = joi.string().alphanum().min(1).max(10).required()
//密码 字符串+6-12位非空字符
const password = joi.string().pattern(/^[\S]{6,12}$/).required()
//手机号 字符串+手机号+必填项
const phone = joi.string().pattern(/^[1][3,4,5,6,7,8,9][0-9]{9}$/)
//邮箱 字符串+邮箱+必填项
const email = joi.string().email().required()
//头像 字符串+base64编码+必填项
const avatar = joi.string().dataUri().required

var loginSchema = 
{
    body:
    {
        username,
        password
    }
}

var updatePasswordSchema=
{
    body:
    {
        oldPwd:password,
        newPwd:joi.not(joi.ref('oldPwd')).concat(password)
    }
}

var updateAvatarSchema=
{
    body:
    {
        avatar
    }
}

var updateUserInfoSchema = 
{
    body:
    {
        username,
        email,
    }
}

module.exports=
{
    loginSchema,
    updatePasswordSchema,
    updateAvatarSchema,
    updateUserInfoSchema
}