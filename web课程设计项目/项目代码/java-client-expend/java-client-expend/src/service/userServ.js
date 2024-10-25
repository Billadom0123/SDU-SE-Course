import axios from "axios";

function userLoginReq(username, password) {
    console.log('已被调用');
    return axios.post('/api/auth/login',
    // return axios.post('/api/auth/login',
        {
            username: username,
            password: password
        }).then(res => {
            //console.log(res.data);
            // return res.data.accessToken
            return res.data
        })
}

function registerUser(args) {
    return axios.post('/api/auth/signup',
    // return axios.post('/api/auth/login',
    args).then(res => {
            return res.data
        })
}

function sendEmail(username,checkCodeString){
    return axios.post('/api/auth/sendEmail',{data:{username:username,checkCodeString:checkCodeString}}).then(
        (res)=>{
            return res.data;
        }
    )
}
export {
    userLoginReq, registerUser,sendEmail,
}