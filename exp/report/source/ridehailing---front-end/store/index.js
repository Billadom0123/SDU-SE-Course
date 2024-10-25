import Vuex from 'vuex'
import { createStore } from 'vuex'
import {loginReq,AndroidLoginReq} from '@/service/userService'
// import { showToast } from 'vant';


const store = new createStore({

    state(){
        return {
            // loggedIn: false,
            // username:"",
            // userId:"",
            // token:"",
            // latest:null,
            // role_id:null
        }
    },

    mutations:{
        login(state,{userId,username,token,role_id}){
			uni.setStorageSync('loggedIn',true);
			uni.setStorageSync('userId',userId);
			uni.setStorageSync('username',username);
			uni.setStorageSync('token',token);
			uni.setStorageSync('role_id',role_id);
			// if(role_id==='')
			uni.setStorageSync('latest',Date.now());
            // state.loggedIn = true
            // state.userId = userId
            // state.username = username
            // state.token = token
            // state.latest = Date.now()
            // state.role_id = role_id
        },
        logout(state){
			uni.setStorageSync('loggedIn',false);
			uni.setStorageSync('userId',"");
			uni.setStorageSync('username',"");
			uni.setStorageSync('token',"");
			uni.setStorageSync('role_id',null);
			uni.setStorageSync('latest',null);
            // state.loggedIn = false
            // state.userId = ""
            // state.username = ""
            // state.token = ""
            // state.latest = null
        }
    },


    actions:{
		AndroidLoginCommit({commit},{phone,password,checkCode,role_id}){
			return AndroidLoginReq(phone,password,checkCode,role_id).then(
			    (result)=>{
			        // console.log(result);
			        let success = result.data.success
			        //报错
			        if(!success){
						uni.showToast({
							icon:'none',
							position:'top',
							title:result.data.errorMsg
						})
			        }
			        //
			        else{
			            var token = result.data.data.token;
			            var userId = result.data.data.id;
			            var username = result.data.data.name;
			            commit('login',{userId,username,token,role_id})
						uni.showToast({
							icon:'none',
							position:'top',
							title:'登陆成功'
						})
			            if(role_id === 1){
			                uni.reLaunch({
								url:'/pages/customer/selectPosition'
							})
			            }
			            else{
			                uni.reLaunch({
			                	url:'/pages/driver/startBusiness'
			                })
			            }
			        }
			    }
			)
		},
        loginCommit({commit},{phone,password,checkCode,role_id}){
            return loginReq(phone,password,checkCode,role_id).then(
                (result)=>{
                    let success = result.data.success
                    //报错
                    if(!success){
						uni.showToast({
							icon:'none',
							position:'top',
							title:result.data.errorMsg
						})
                    }
                    //
                    else{
                        var token = result.data.token;
                        var userId = result.data.id;
                        var username = result.data.name;
                        commit('login',{userId,username,token,role_id})
						uni.showToast({
							icon:'none',
							position:'top',
							title:'登陆成功'
						})
                        if(role_id === 1){
                            uni.navigateTo({
								url:'customer/selectPosition'
							})
                        }
                        else{
                            uni.navigateTo({
                            	url:'maps'
                            })
                        }
                    }
                }
            )
        },

        logOutCommit({commit}){
            commit('logout')
        }
    },

})



  export  {
    store
}
