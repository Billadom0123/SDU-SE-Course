import axios from "axios";
import { store } from "@/store/createStore.js"
import { ElMessage } from 'element-plus'

function generalRequest(url, data) {
    return axios.post(
        'http://localhost:9090' + url,
        // url,
        {
            data: data
        },
        {
            headers: {
                Authorization: 'Bearer ' + store.state.jwtToken
            }
        }
    ).then(res => {
        if (res.status == 500) {

            ElMessage({
                type: 'warnning',
                message: '后端报错'
            });
            return;
        }
        if (res.status == 404) {
            ElMessage({
                type: 'warnning',
                message: '后端方法不存在'
            });
            return;
        }
        return res.data
    }).catch(() => {
        return;
    })
}

export function getAuthHeader() {
    return {
      Authorization: 'Bearer ' + store.state.jwtToken
    }
  }

function getUimsConfig() {
    return generalRequest('/api/auth/getUimsConfig', null)
}

function getStudentIntroduceData(data) {
    return generalRequest('/api/teach/getStudentIntroduceData', data)
}

function sayHello(data){
    return generalRequest('/api/teach/sayHello',data)
}

function showSelectList(){
    return generalRequest('/api/course/showSelectList')
}

function showCourse(){
    return generalRequest('/api/course/showCourse')
}

function computeCredit(){
    return generalRequest('/api/course/computeCredit')
}

function chooseCourse(data){
    return generalRequest('/api/course/chooseCourse',data)
}

function rejectCourse(data){
    return generalRequest('/api/course/rejectCourse',data)
}

function courseQuery(data){
    return generalRequest('/api/course/courseQuery',data)
}

function showScore(){
    return generalRequest('/api/score/showScore')
}

function generateElOption(){
    return generalRequest('/api/score/generateElOption')
}

function scoreQuery(data){
    return generalRequest('/api/score/scoreQuery',data)
}

function computeAverageGPA(data){
    return generalRequest('/api/score/computeAverageGPA',data)
}

function getBlogger(){
    return generalRequest('/api/blog/getBlogger')
}

function getBlogs(){
    return generalRequest('/api/blog/getBlogs')
}

function fetchBlogId(data){
    return generalRequest('/api/blog/fetchBlogId',data)
}

function showBlog(){
    return generalRequest('/api/blog/showBlog')
}

function showOther(){
    return generalRequest('/api/blog/showOther')
}

function showPerson(){
    return generalRequest('/api/info/showPerson')
}

function getPersonImage(){
    return generalRequest('/api/info/getPersonImage')
}

function infoEdit(data){
    return generalRequest('/api/info/infoEdit',data)
}

function showWordCloud(){
    return generalRequest('/api/assessment/showWordCloud')
}

function showAssessment(){
    return generalRequest('/api/assessment/showAssessment')
}

function changeChecked(){
    return generalRequest('/api/assessment/changeChecked')
}

function showRecentDeliver(){
    return generalRequest('/api/assessment/showRecentDeliver')
}

function showRecentReceive(){
    return generalRequest('/api/assessment/showRecentReceive')
}

function searchStudent(data){
    return generalRequest('/api/assessment/searchStudent',data)
}

function addAssessment(data){
    return generalRequest('/api/assessment/addAssessment',data)
}

function showPractice(){
    return generalRequest('/api/studentManagement/showPractice')
}

function practiceQuery(data){
    return generalRequest('/api/studentManagement/practiceQuery',data)
}

function addPractice(data){
    return generalRequest('/api/studentManagement/addPractice',data)
}

function getTeacher(){
    return generalRequest('/api/blog/getTeacher')
}

function getResearch(){
    return generalRequest('/api/blog/getResearch')
}

function getPaper(){
    return generalRequest('/api/blog/getPaper')
}

function addResearch(data){
    return generalRequest('/api/blog/addResearch',data)
}

function deleteResearch(data){
    return generalRequest('/api/blog/deleteResearch',data)
}

function addPaper(data){
    return generalRequest('/api/blog/addPaper',data)
}

function deletePaper(data){
    return generalRequest('/api/blog/deletePaper',data)
}

function showTeacherCourse(){
    return generalRequest('/api/course/showTeacherCourse')
}

function getSimpleTeacherCourse(){
    return generalRequest('/api/course/getSimpleTeacherCourse')
}

function addCourse(data){
    return generalRequest('/api/course/addCourse',data)
}

function deleteCourse(data){
    return generalRequest('/api/course/deleteCourse',data)
}

function getLyhPerson(){
    return generalRequest('/api/lyh/getLyhPerson')
}

function getMemoryImage(){
    return generalRequest('/api/lyh/getMemoryImage')
}

function deleteMemoryImage(data){
    return generalRequest('/api/lyh/deleteMemoryImage',data)
}

function getDefaultBackGround(){
    return generalRequest('/api/lyh/getDefaultBackGround')
}

function setUserBackGroundInDefault(data){
    return generalRequest('/api/lyh/setUserBackGroundInDefault',data)
}

function getUserBackGround(){
    return generalRequest('/api/lyh/getUserBackGround')
}

function addUserPreviousBackGroundFromDefault(data){
    return generalRequest('/api/lyh/addUserPreviousBackGroundFromDefault',data)
}

function getPreviousBackGround(){
    return generalRequest('/api/lyh/getPreviousBackGround')
}

function setUserBackGroundInPrevious(data){
    return generalRequest('/api/lyh/setUserBackGroundInPrevious',data)
}

function showTeacherScore(){
    return generalRequest('/api/score/showTeacherScore')
}

function openCourse(data){
    return generalRequest('/api/course/openCourse',data)
}

function checkIsOpen(){
    return generalRequest('/api/course/checkIsOpen')
}

function changeToken(data){
    return generalRequest('/api/info/changeToken',data)
}

function generateWordCloud(){
    return generalRequest('/api/info/generateWordCloud')
}

function generateStatistics(){
    return generalRequest('/api/info/generateStatistics')
}

function showUnChecked(){
    return generalRequest('/api/assessment/showUnChecked')
}

function addBlog(data){
    return generalRequest('/api/blog/addBlog',data)
}

function deleteBlog(data){
    return generalRequest('/api/blog/deleteBlog',data)
}

function downloadExcel(){
    return generalRequest('/api/course/downloadExcel')
}

function generateDirectories(){
    return generalRequest('/api/auth/generateDirectories')
}


export {
    generalRequest,
    getStudentIntroduceData,
    getUimsConfig,
    sayHello,
    showSelectList,
    showCourse,
    computeCredit,
    chooseCourse,
    rejectCourse,
    courseQuery,
    showScore,
    generateElOption,
    scoreQuery,
    computeAverageGPA,
    getBlogger,
    getBlogs,
    fetchBlogId,
    showBlog,
    showOther,
    showPerson,
    getPersonImage,
    infoEdit,
    showWordCloud,
    showAssessment,
    changeChecked,
    showRecentDeliver,
    showRecentReceive,
    searchStudent,
    addAssessment,
    showPractice,
    practiceQuery,
    addPractice,
    getTeacher,
    getResearch,
    getPaper,
    addResearch,
    deleteResearch,
    addPaper,
    deletePaper,
    showTeacherCourse,
    getSimpleTeacherCourse,
    addCourse,
    deleteCourse,
    getLyhPerson,
    getMemoryImage,
    deleteMemoryImage,
    getDefaultBackGround,
    setUserBackGroundInDefault,
    getUserBackGround,
    addUserPreviousBackGroundFromDefault,
    getPreviousBackGround,
    setUserBackGroundInPrevious,
    showTeacherScore,
    openCourse,
    checkIsOpen,
    changeToken,
    generateWordCloud,
    generateStatistics,
    showUnChecked,
    addBlog,
    deleteBlog,
    downloadExcel,
    generateDirectories
}



export function downloadPost(url, label, data) {

    const requestOptions = {
        method: 'POST',
        headers: {
            'content-type': 'application/json',
            'Authorization': 'Bearer ' + store.state.jwtToken
        },
        body: JSON.stringify({
            data: data
        })
    };
    return fetch(url, requestOptions)
        .then(async response => {
            const blob = await response.blob()

            // check for error response
            if (!response.ok) {
                // get error message from body or default to response status
                const error = response.status;
                return Promise.reject(error)
            }
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = label
            link.click()
            URL.revokeObjectURL(link.href)
        })
        .catch(error => {
            console.error('There was an error!', error);
        });
}