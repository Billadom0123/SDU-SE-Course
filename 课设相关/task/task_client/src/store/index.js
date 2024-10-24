import { createStore } from 'vuex'
import VuexPersistence from 'vuex-persist'
import {loginReq} from '@/service/userService'
import router from '../router/index'


const vuexLocal = new VuexPersistence({
  key: 'ETSUserInfo',
  storage: window.localStorage
})

const store = createStore({
  state(){
    return {
      loggedIn: false,
      username: "",
      jwt : ""
    }
  },
  mutations: {
    login(state, {username,jwt}){
      state.loggedIn = true
      state.username = username
      state.jwt = jwt
    },
    logout(state){
      state.loggedIn = false
      state.username = ""
      state.jwt = ""
    }
  },
  actions: {
    loginCommit({ commit },{ username, password}){
      return loginReq(username,password).then(
        (data)=>{
          if(data.status===0){
            var jwt = data.token
            commit('login',{username,jwt})
            router.push('/task')
          }
          else if(data.status===1){
            this.$message({
              message: data.message,
              type: "error",
            });
          }
        }
      )
    },
    logOutCommit({commit}){
      commit('logout')
    }
  },
  plugins:[vuexLocal.plugin]
})

export  {
  store
}

