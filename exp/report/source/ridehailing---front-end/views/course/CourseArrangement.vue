<template>
<SideBarForTeacher/>
<div class="position">
    <a href="javascript:;" >首页</a>
    /
    <a href="javascript:;" >教师课程</a>
    /
    <a href="javascript:;" >课程安排</a>
</div>
<div class="timetable">
    <table id="timetable">
        <tr>
            <th class="week">&nbsp;</th>
            <th class="week">星期一</th>
            <th class="week">星期二</th>
            <th class="week">星期三</th>
            <th class="week">星期四</th>
            <th class="week">星期五</th>
            <th class="week">星期六</th>
            <th class="week">星期日</th>
        </tr>

        <tr>
            <th class="time">第一节<br>(第1,2小节)</th>
            <td v-for="item in value" :key="item"  v-bind:class="getColor(item,0)" class="item-box">{{this.lesson[item*5+0].name}} <br> {{this.lesson[item*5+0].scope}} <br> {{this.lesson[item*5+0].place}} </td>
        </tr>
        <tr>
            <th class="time">第二节<br>(第3,4小节)</th>
            <td v-for="item in value" :key="item"  v-bind:class="getColor(item,1)" class="item-box">{{this.lesson[item*5+1].name}} <br> {{this.lesson[item*5+1].scope}} <br> {{this.lesson[item*5+1].place}}</td>
        </tr>
        <tr>
            <th class="time">第三节<br>(第5,6小节)</th>
            <td v-for="item in value" :key="item"  v-bind:class="getColor(item,2)" class="item-box">{{this.lesson[item*5+2].name}} <br> {{this.lesson[item*5+2].scope}} <br> {{this.lesson[item*5+2].place}}</td>
        </tr>
        <tr>
            <th class="time">第四节<br>(第7,8小节)</th>
            <td v-for="item in value" :key="item"  v-bind:class="getColor(item,3)" class="item-box">{{this.lesson[item*5+3].name}} <br> {{this.lesson[item*5+3].scope}} <br> {{this.lesson[item*5+3].place}}</td>
        </tr>
        <tr>
            <th class="time">第五节<br>(第9,10小节)</th>
            <td v-for="item in value" :key="item"  v-bind:class="getColor(item,4)" class="item-box">{{this.lesson[item*5+4].name}} <br> {{this.lesson[item*5+4].scope}} <br> {{this.lesson[item*5+4].place}}</td>
        </tr>
    </table>
</div>
</template>

<script>
import SideBarForTeacher from "@/components/SideBarForTeacher";
import {getSimpleTeacherCourse} from "@/service/genServ.js"
export default{
    name:'CourseArrangement',
    data() {
        return {
            lesson:[
                {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },{
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },{
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },{
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },{
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:"",
            },
            {
                name:"",
                teacher:"",
                scope:"",
                place:"",
                prop:""
            },
            ],
            value:[0,1,2,3,4,5,6],
        }
    },
    methods: {
        getColor(index,offset){
            var prop = this.lesson[index*5+offset].prop;
            console.log(prop);
            if(prop=='必修课'){
                return 'req';
            }
            else if(prop=='任选课'){
                return 'opt';
            }
            else if (prop=='限选课'){
                return 'lim';
            }
            return '';
        }
    },
    mounted() {
        getSimpleTeacherCourse().then(
        (res)=>{
            console.log(res.data.data[0].name);
            this.lesson=res.data.data;
        }
        )
    },
    components:{
        SideBarForTeacher,
    }
}
</script>

<style scoped>
.position{
    color: #97A8BE;
    position:absolute;
    left: 0;
    top: 0;
    transform:translate(60px,12px);
  }
  .position a{
    color: #97A8BE !important;
    text-decoration: none;
  }

  .req{
    background-color: pink;
}
.opt{
    background-color: rgb(190,237,242);
}
.lim{
    background-color: rgb(205,221,252);
}

.timetable{
    margin-left: 20px;
    margin-top: 20px;
}
#timetable{
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    border-collapse: collapse;
    color: #757575;
    background-color: #fff;
    border: 1px solid #757575;
}
#timetable th{
    min-width: 100px;
    text-align: center;
    padding-left: 10px;
    padding-right: 10px;
    padding-bottom: 10px;
    padding-top: 10px;
    border: 1px solid #757575;
}
#timetable td{
    min-width: 100px;
    border: 1px solid #757575;
}
.week{
    background-color:rgb(222, 225, 230);
}
.time{
    background-color:rgb(222, 225, 230);
}
.item-box{
    padding: 10px 10px 10px 10px;
    color: black;
    font-size: 12px;
}
</style>