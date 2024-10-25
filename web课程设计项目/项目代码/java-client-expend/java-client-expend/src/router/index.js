import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/views/Login'

const routes = [
  {
    path: '/Home',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },

  {
    path: '/BaseTable',
    name: 'BaseTable',
    component: () => import('@/views/dashboard/BaseTable.vue')
  },
  {
    path: '/BaseTableOne',
    name: 'BaseTableOne',
    component: () => import('@/views/Table/BaseTableOne.vue')
  },
  {
    path: '/BaseTableTwo',
    name: 'BaseTableTwo',
    component: () => import('@/views/Table/BaseTableTwo.vue')
  },
  {
    path: '/BaseForm',
    name: 'BaseForm',
    component: () => import('@/views/dashboard/BaseForm.vue')
  },
  {
    path: '/index',
    name: 'index',
    component: () => import('@/views/index/index.vue')
  },
  {
    path: '/CourseManagement',
    name: 'CourseManagement',
    component: () => import('@/views/course/CourseManagement.vue')
  },
  {
    path: '/chooseCourse',
    name: 'chooseCourse',
    component: () => import('@/views/course/chooseCourse.vue')
  },
  {
    path: '/ScoreManagement',
    name: 'ScoreManagement',
    component: () => import('@/views/course/ScoreManagement.vue')
  },
  {
    path: '/MyBlog',
    name: 'MyBlog',
    component: () => import('@/views/MyInfo/MyBlog.vue')
  },
  {
    path: '/Blogs',
    name: 'Blogs',
    component: () => import('@/views/MyInfo/Blogs.vue')
  },
  {
    path: '/Profile',
    name: 'Profile',
    component: () => import('@/views/MyInfo/Profile.vue')
  },
  {
    path: '/MyAssessment',
    name: 'MyAssessment',
    component: () => import('@/views/MyInfo/MyAssessment.vue')
  },
  {
    path: '/Practice',
    name: 'Practice',
    component: () => import('@/views/StudentManageMent/Practice.vue')
  },
  {
    path:'/indexForTeacher',
    name:'indexForTeacher',
    component:() => import ('@/views/index/indexForTeacher.vue') 
  },
  {
    path:'/TeacherInfo',
    name:'TeacherInfo',
    component:() => import ('@/views/MyInfo/TeacherInfo.vue') 
  },
  {
    path:'/AcademicResearch',
    name:'AcademicResearch',
    component:() => import ('@/views/MyInfo/AcademicResearch.vue') 
  },
  {
    path:'/AddCourse',
    name:'AddCourse',
    component:() => import ('@/views/course/AddCourse.vue') 
  },
  {
    path:'/AddScore',
    name:'AddScore',
    component:() => import ('@/views/course/AddScore.vue') 
  },
  {
    path:'/CourseArrangement',
    name:'CourseArrangement',
    component:() => import ('@/views/course/CourseArrangement.vue') 
  },
  {
    path:'/temp',
    name:'Temp',
    component: () => import('@/views/temp.vue')
  },
  {
    path:'/indexOfLyh',
    name:'indexOfLyh',
    component: () => import('@/views/lyh/indexOfLyh.vue')
  },
  {
    path:'/memoryOfLyh',
    name:'memoryOfLyh',
    component: () => import('@/views/lyh/memoryOfLyh.vue')
  },
  {
    path:'/photoOfLyh',
    name:'photoOfLyh',
    component: () => import('@/views/lyh/photoOfLyh.vue')
  },
  {
    path:'/settingOfLyh',
    name:'settingOfLyh',
    component: () => import('@/views/lyh/settingOfLyh.vue')
  },
  {
    path:'/indexForAdmin',
    name:'indexForAdmin',
    component: () => import('@/views/index/indexForAdmin.vue')
  },
  {
    path:'/AdminInfo',
    name:'AdminInfo',
    component: () => import('@/views/MyInfo/AdminInfo.vue')
  },
  {
    path:'/CourseChannel',
    name:'CourseChannel',
    component: () => import('@/views/course/OpenCloseCourse.vue')
  },
  {
    path:'/PersonRegistration',
    name:'PersonRegistration',
    component: () => import('@/views/MyInfo/PersonRegistration.vue')
  },
  {
    path:'/Contact',
    name:'Contact',
    component:()=>import('@/views/Introduction/Contact.vue')
  },
  {
    path:'/Home',
    name:'Home',
    component:()=>import('@/views/Introduction/Home.vue'),
  },
  {
    path:'/Skill',
    name:'Skill',
    component:()=>import('../views/Introduction/Skill.vue'),
  },

  // {
  //   path:'/Introduction',
  //   name:'Introduction',

  //   children:[{
      
  //   },
  //     {
  //       
  //     },
  //     {
  //       
  //     },
  //   ]

  // },




]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
