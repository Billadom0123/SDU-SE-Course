import { createStore } from 'vuex';
import VuexPersist from 'vuex-persist';
import router from '@/router/index.js';
import { userLoginReq } from '@/service/userServ.js'



const vuexLocalStorage = new VuexPersist({
  key: 'vuex', // The key to store the state on in the storage provider.
  storage: window.localStorage, // or window.sessionStorage or localForage
  // Function that passes the state and returns the state with only the objects you want to store.
  // reducer: state => state,
  // Function that passes a mutation and lets you decide if it should update the state in localStorage.
  // filter: mutation => (true)
})

const store = createStore({
  state() {
    return {
      loggedIn: false,
      username: "",
      password:"",
      jwtToken: "",
      routerName: "",
      list: []
    }
  },
  mutations: {
    navi(state, data) {
      state.list = data
    },
    setRouterName(state, val) {
      state.routerName = val
    },
    
    login(state, { username, jwtToken,password }) {
      //调用者:actions-login:commit('login', { username, jwtToken })
      // console.log(username);
      state.loggedIn = true,
        state.username = username,
        state.jwtToken = jwtToken,
        state.password=password
    },
    logout(state) {
      state.loggedIn = false,
        state.username = "",
        state.jwtToken = "",
        state.list = []
    },
    changeUserName(state,username){
      state.username=username;
    }
  },
  actions: {
    login({ commit }, { username, password }) {
      // console.log("actions-login已被调用");
      return userLoginReq(username, password)
        // .then((jwtToken) => {
          .then((user) => {
          console.log(user);
          var jwtToken = user.accessToken;
          commit('login',{username,jwtToken,password})
          // commit('login', { username, jwtToken })

          if(user.roles==='ROLE_STUDENT'){
            router.push('/index')
          }
          else if(user.roles==='ROLE_TEACHER'){
            router.push('/indexForTeacher')
          }
          else if(user.roles==='ROLE_ADMIN'){
            router.push('/indexForAdmin')
          }
          
          // router.push('/index')

        })
    }
  },
  plugins: [vuexLocalStorage.plugin]
});

export { store }