package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class StudentDetailsController {
    @Autowired
    private StudentRepository studentRepository;

    //返回学生的相关超链，只保留了学生的姓名，学号
    public List getStudentDetailsList(Integer Id) {

        List dataList = new ArrayList();
        Optional<Student> sList = studentRepository.findById(Id);//Id为空所以显示了所有学生//
        if(!sList.isPresent()){
            return dataList;
        }
        Student s;
        Map m;
        String myCourseParas,studentNameParas,myHomeworkParas,myActivityParas,myInnovationParas,myInfoParas,honorParas,myAchievementParas,studentParas,myLogParas,myLeaveParas;
        s = sList.get();
        m = new HashMap();
        //  m.put("id", s.getId());
        //m.put("studentNum",s.getStudentNum());
        //简历超链
        studentNameParas = "model=introduce&studentId=" + s.getId();
        m.put("studentName",s.getStudentName());
        m.put("studentNameParas",studentNameParas);

        myCourseParas = "model=myCourse&studentId=" + s.getId();
        m.put("myCourse","我的课程");
        m.put("myCourseParas",myCourseParas);

        myInnovationParas = "model=myInnovation&studentId="+s.getId();
        m.put("myInnovation","我的创新");
        m.put("myInnovationParas",myInnovationParas);

        myActivityParas = "model=myActivity&studentId="+s.getId();
        m.put("myActivity","我的活动");
        m.put("myActivityParas",myActivityParas);

        myAchievementParas ="model=myAchievement&studentId="+s.getId();
        m.put("myAchievement","我的成绩");
        m.put("myAchievementParas",myAchievementParas);

        myHomeworkParas ="model=myHomework&studentId="+s.getId();
        m.put("myHomework","我的作业成绩");
        m.put("myHomeworkParas",myHomeworkParas);

        myInfoParas="model=myInfo&studentId="+s.getId();
        m.put("myInfo","我的基本信息");
        m.put("myInfoParas",myInfoParas);

        myLogParas = "model=myLog&studentId="+s.getId();
        m.put("myLog","花销信息");
        m.put("myLogParas",myLogParas);

        myLeaveParas = "model=myLeave&studentId="+s.getId();
        m.put("myLeave","外出请假信息");
        m.put("myLeaveParas",myLeaveParas);

        //把自己的超链加到这里
        honorParas = "model=honor&studentId=" + s.getId();
        m.put("honor","我的荣誉");
        m.put("honorParas",honorParas);

        studentParas = "model=student&clazzId="+s.getClazz().getId();
        m.put("student","返回");
        m.put("studentParas",studentParas);


        dataList.add(m);

        return dataList;
    }

    @PostMapping("/StudentDetailsInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse StudentDetailsInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer Id = dataRequest.getInteger("studentId");//把Id改成studentId就可以传参（why）
        List dataList = getStudentDetailsList(Id);//
        return CommonMethod.getReturnData(dataList);
    }

}
