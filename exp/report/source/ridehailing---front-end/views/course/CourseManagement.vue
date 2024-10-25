<template>
<SideBar/>

<div class="position">
    <a href="javascript:;" @click.prevent="gotoIndex">首页</a>
    /
    <a href="javascript:;" >课程中心</a>
    /
    <a href="javascript:;" >课程管理</a>
</div>

<!-- <div class="classify" style="margin-top:20px;margin-left:20px">
    <div class="circle" style="background-color:pink"></div>
    <div class="class">必修课</div>
    <div class="circle" style="background-color:rgb(205,221,252)"></div>
    <div class="class">限选课</div>
    <div class="circle" style="background-color:rgb(190,237,242)"></div>
    <div class="class">任选课</div>
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
            <td class="item-box" v-bind:class="get_color(lesson[0].prop)" >{{lesson[0].name}}<br>{{lesson[0].teacher}}<br>{{lesson[0].scope}}<br>{{lesson[0].place}}</td>
            <td class="item-box">{{lesson[1].name}}<br>{{lesson[1].teacher}}<br>{{lesson[1].scope}}<br>{{lesson[1].place}}</td>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box" v-bind:class="get_color(lesson[3].prop)">{{lesson[3].name}}<br>{{lesson[3].teacher}}<br>{{lesson[3].scope}}<br>{{lesson[3].place}}</td>
            <td class="item-box">概率与统计<br>潘荣江<br>第1-12周<br>软件园5区407d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">&nbsp;</td>
        </tr>
        <tr>
            <th class="time">第二节<br>(第3,4小节)</th>
            <td class="item-box">中华优秀传统文化英语解读<br>孟庆娟<br>第1-16周<br>软件园1区107d</td>
            <td class="item-box">计算机组成原理<br>李新<br>第1-16周<br>软件园4区402d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">概率与统计<br>潘荣江<br>第1-12周<br>软件园5区407d</td>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box">工业软件导论<br>潘丽<br>第1-8周<br>软件园4区106d</td>
        </tr>
        <tr>
            <th class="time">第三节<br>(第5,6小节)</th>
            <td class="item-box">中华优秀传统文化英语解读<br>孟庆娟<br>第1-16周<br>软件园1区107d</td>
            <td class="item-box">计算机组成原理<br>李新<br>第1-16周<br>软件园4区402d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">概率与统计<br>潘荣江<br>第1-12周<br>软件园5区407d</td>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box">工业软件导论<br>潘丽<br>第1-8周<br>软件园4区106d</td>
        </tr>
        <tr>
            <th class="time">第四节<br>(第7,8小节)</th>
            <td class="item-box">中华优秀传统文化英语解读<br>孟庆娟<br>第1-16周<br>软件园1区107d</td>
            <td class="item-box">计算机组成原理<br>李新<br>第1-16周<br>软件园4区402d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">概率与统计<br>潘荣江<br>第1-12周<br>软件园5区407d</td>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box">工业软件导论<br>潘丽<br>第1-8周<br>软件园4区106d</td>
        </tr>
        <tr>
            <th class="time">第五节<br>(第9,10小节)</th>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">Web技术<br>李学庆<br>第1-8周<br>软件园5区407d</td>
            <td class="item-box">中华优秀传统文化英语解读<br>孟庆娟<br>第1-16周<br>软件园1区107d</td>
            <td class="item-box">概率与统计<br>潘荣江<br>第1-12周<br>软件园5区407d</td>
            <td class="item-box">&nbsp;</td>
            <td class="item-box">&nbsp;</td>
        </tr>
    </table>
</div> -->
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
            <td class="item-box" v-bind:class="get_color(lesson[0].prop)" >{{lesson[0].name}}<br>{{lesson[0].teacher}}<br>{{lesson[0].scope}}<br>{{lesson[0].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[5].prop)">{{lesson[5].name}}<br>{{lesson[5].teacher}}<br>{{lesson[5].scope}}<br>{{lesson[5].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[10].prop)">{{lesson[10].name}}<br>{{lesson[10].teacher}}<br>{{lesson[10].scope}}<br>{{lesson[10].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[15].prop)">{{lesson[15].name}}<br>{{lesson[15].teacher}}<br>{{lesson[15].scope}}<br>{{lesson[15].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[20].prop)">{{lesson[20].name}}<br>{{lesson[20].teacher}}<br>{{lesson[20].scope}}<br>{{lesson[20].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[25].prop)">{{lesson[25].name}}<br>{{lesson[25].teacher}}<br>{{lesson[25].scope}}<br>{{lesson[25].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[30].prop)">{{lesson[30].name}}<br>{{lesson[30].teacher}}<br>{{lesson[30].scope}}<br>{{lesson[30].place}}</td>
        </tr>
        <tr>
            <th class="time">第二节<br>(第3,4小节)</th>
            <td class="item-box" v-bind:class="get_color(lesson[1].prop)">{{lesson[1].name}}<br>{{lesson[1].teacher}}<br>{{lesson[1].scope}}<br>{{lesson[1].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[6].prop)">{{lesson[6].name}}<br>{{lesson[6].teacher}}<br>{{lesson[6].scope}}<br>{{lesson[6].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[11].prop)">{{lesson[11].name}}<br>{{lesson[11].teacher}}<br>{{lesson[11].scope}}<br>{{lesson[11].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[16].prop)">{{lesson[16].name}}<br>{{lesson[16].teacher}}<br>{{lesson[16].scope}}<br>{{lesson[16].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[21].prop)">{{lesson[21].name}}<br>{{lesson[21].teacher}}<br>{{lesson[21].scope}}<br>{{lesson[21].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[26].prop)">{{lesson[26].name}}<br>{{lesson[26].teacher}}<br>{{lesson[26].scope}}<br>{{lesson[26].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[31].prop)">{{lesson[31].name}}<br>{{lesson[31].teacher}}<br>{{lesson[31].scope}}<br>{{lesson[31].place}}</td>
        </tr>
        <tr>
            <th class="time">第三节<br>(第5,6小节)</th>
            <td class="item-box" v-bind:class="get_color(lesson[2].prop)">{{lesson[2].name}}<br>{{lesson[2].teacher}}<br>{{lesson[2].scope}}<br>{{lesson[2].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[7].prop)">{{lesson[7].name}}<br>{{lesson[7].teacher}}<br>{{lesson[7].scope}}<br>{{lesson[7].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[12].prop)">{{lesson[12].name}}<br>{{lesson[12].teacher}}<br>{{lesson[12].scope}}<br>{{lesson[12].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[17].prop)">{{lesson[17].name}}<br>{{lesson[17].teacher}}<br>{{lesson[17].scope}}<br>{{lesson[17].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[22].prop)">{{lesson[22].name}}<br>{{lesson[22].teacher}}<br>{{lesson[22].scope}}<br>{{lesson[22].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[27].prop)">{{lesson[27].name}}<br>{{lesson[27].teacher}}<br>{{lesson[27].scope}}<br>{{lesson[27].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[32].prop)">{{lesson[32].name}}<br>{{lesson[32].teacher}}<br>{{lesson[32].scope}}<br>{{lesson[32].place}}</td>
        </tr>
        <tr>
            <th class="time">第四节<br>(第7,8小节)</th>
            <td class="item-box" v-bind:class="get_color(lesson[3].prop)">{{lesson[3].name}}<br>{{lesson[3].teacher}}<br>{{lesson[3].scope}}<br>{{lesson[3].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[8].prop)">{{lesson[8].name}}<br>{{lesson[8].teacher}}<br>{{lesson[8].scope}}<br>{{lesson[8].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[13].prop)">{{lesson[13].name}}<br>{{lesson[13].teacher}}<br>{{lesson[13].scope}}<br>{{lesson[13].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[18].prop)">{{lesson[18].name}}<br>{{lesson[18].teacher}}<br>{{lesson[18].scope}}<br>{{lesson[18].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[23].prop)">{{lesson[23].name}}<br>{{lesson[23].teacher}}<br>{{lesson[23].scope}}<br>{{lesson[23].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[28].prop)">{{lesson[28].name}}<br>{{lesson[28].teacher}}<br>{{lesson[28].scope}}<br>{{lesson[28].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[33].prop)">{{lesson[33].name}}<br>{{lesson[33].teacher}}<br>{{lesson[33].scope}}<br>{{lesson[33].place}}</td>
        </tr>
        <tr>
            <th class="time">第五节<br>(第9,10小节)</th>
            <td class="item-box" v-bind:class="get_color(lesson[4].prop)">{{lesson[4].name}}<br>{{lesson[4].teacher}}<br>{{lesson[4].scope}}<br>{{lesson[4].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[9].prop)">{{lesson[9].name}}<br>{{lesson[9].teacher}}<br>{{lesson[9].scope}}<br>{{lesson[9].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[14].prop)">{{lesson[14].name}}<br>{{lesson[14].teacher}}<br>{{lesson[14].scope}}<br>{{lesson[14].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[19].prop)">{{lesson[19].name}}<br>{{lesson[19].teacher}}<br>{{lesson[19].scope}}<br>{{lesson[19].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[24].prop)">{{lesson[24].name}}<br>{{lesson[24].teacher}}<br>{{lesson[24].scope}}<br>{{lesson[24].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[29].prop)">{{lesson[29].name}}<br>{{lesson[29].teacher}}<br>{{lesson[29].scope}}<br>{{lesson[29].place}}</td>
            <td class="item-box" v-bind:class="get_color(lesson[34].prop)">{{lesson[34].name}}<br>{{lesson[34].teacher}}<br>{{lesson[34].scope}}<br>{{lesson[34].place}}</td>
        </tr>
    </table>
</div>

</template>

<script>
import SideBar from "@/components/SideBar";
import {showCourse} from "@/service/genServ.js";
export default{
name:"CourseManageMent",
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
            
        

        ]
    }
},
mounted() {
    showCourse().then(
        (res)=>{
            console.log(res.data.data);
            this.lesson=res.data.data;
            console.log(this.lesson);
        }
    )
},
components:{
    SideBar
},
methods: {
    //给表格单元换颜色
    get_color: function(course) {
        if(course == '限选课'){
            return 'lim';
        }else if(course=='任选课'){
            return 'opt';
        }
        else if(course=='必修课'){
            return 'req';
        }

    },
    //去首页
    gotoIndex: function(){
        console.log(this.$routers);
        this.$router.push({
            path:'index',
        })
    }
},
}
</script>

<style scoped>
.req{
    background-color: pink;
}
.opt{
    background-color: rgb(190,237,242);
}
.lim{
    background-color: rgb(205,221,252);
}
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
.circle{
    width: 10px;
    height: 10px;
    border-radius: 5px;
}
.classify{
    width: 200px;
    height: 200px;
    color:black;
}
.class{
    transform: translate(-60px,-14px);

    font-size: 12px;
}
</style>