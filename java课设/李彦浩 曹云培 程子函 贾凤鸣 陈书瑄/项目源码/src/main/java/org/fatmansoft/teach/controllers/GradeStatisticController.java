package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Grade;
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

public class GradeStatisticController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private MyCourseRepository myCourseRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;

    public List getClazzStatisticMapList(Integer gradeId){
        List dataList = new ArrayList();

        Optional<Grade> op = gradeRepository.findById(gradeId);

        //其实用不到
        if(!op.isPresent()){
            return dataList;
        }

        //导航到 年级学生成绩单 返回
        String gradeStudentAchievementParas,gradeStudentHomeworkParas,gradeParas,gradeStudentLogParas,gradeStudentLeaveParas,gradeStudentHonorParas;
        Map m = new HashMap();

        m.put("gradeNum",op.get().getGradeNum());

        gradeStudentAchievementParas="model=gradeStudentAchievement&gradeId="+gradeId;
        m.put("gradeStudentAchievement","年级学生成绩单");
        m.put("gradeStudentAchievementParas",gradeStudentAchievementParas);

        gradeStudentHomeworkParas="model=gradeStudentHomework&gradeId="+gradeId;
        m.put("gradeStudentHomework","年级学生作业情况");
        m.put("gradeStudentHomeworkParas",gradeStudentHomeworkParas);

        //classStudentCourseParas ="model=classStudentCourse&clazzId="+clazzId;
        //m.put("classStudentCourse","班级学生选课情况");
        //m.put("classStudentCourseParas",classStudentCourseParas);
        gradeStudentLogParas = "model=gradeStudentLog&gradeId="+gradeId;
        m.put("gradeStudentLog","年级学生花销信息");
        m.put("gradeStudentLogParas",gradeStudentLogParas);

        gradeStudentLeaveParas = "model=gradeStudentLeave&gradeId="+gradeId;
        m.put("gradeStudentLeave","年级学生外出请假信息");
        m.put("gradeStudentLeaveParas",gradeStudentLeaveParas);

        gradeStudentHonorParas="model=gradeStudentHonor&gradeId="+gradeId;
        m.put("gradeStudentHonor","年级学生荣誉信息");
        m.put("gradeStudentHonorParas",gradeStudentHonorParas);

        gradeParas = "model=grade&majorId="+op.get().getMajor().getId();
        m.put("grade","返回");
        m.put("gradeParas",gradeParas);

        dataList.add(m);
        return dataList;
    }

    @PostMapping("/gradeStatisticInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStatisticInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer gradeId = dataRequest.getInteger("gradeId");
        List dataList = getClazzStatisticMapList(gradeId);
        return CommonMethod.getReturnData(dataList);
    }
}
