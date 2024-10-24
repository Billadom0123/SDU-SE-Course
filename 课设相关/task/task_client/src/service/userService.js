import axios from 'axios'
import { ElMessage } from 'element-plus'

function loginReq(username,password){
    //后端解码的是x-www-url-encoded格式的数据
    let body = new URLSearchParams()
    body.append("username",username)
    body.append("password",password)
    return axios.post
    (
        'api/user/login',
        body
    ).then
    (
        res=>{
            // console.log(res.data);
            return res.data
        }
    )
}

function registerReq(username,password){
    let body = new URLSearchParams()
    body.append("username",username)
    body.append("password",password)
    return axios.post
    (
        '/api/user/register',
        body
    ).then(
        res=>{
            var resp = res.data
            if(resp.status===0){
                ElMessage({
                    message: '注册成功,快去看看吧!',
                    type: 'success',
                  })
            }
            else if(resp.status===1){
                ElMessage.error('该用户名已被占用')
            }
        }
    )
}

export 
{
    loginReq,
    registerReq
}