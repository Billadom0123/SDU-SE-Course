import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  // }
  {
    path:'/',
    redirect:'/login'
  },
  {
    path:'/login',
    name:'login',
    component:() => import('@/views/login.vue')
  },
  {
    path:'/task',
    name:'task',
    component:() => import('@/views/task.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),//createWebHistory(process.env.BASE_URL),
  routes
})

export default router
