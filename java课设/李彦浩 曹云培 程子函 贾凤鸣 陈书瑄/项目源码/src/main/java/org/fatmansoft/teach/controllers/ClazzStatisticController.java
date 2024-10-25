package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.IntroduceService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class ClazzStatisticController {
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
    @Autowired
    private IntroduceService introduceService;
    @Autowired
    private ResourceLoader resourceLoader;

    public List getClazzStatisticMapList(Integer clazzId){
        List dataList = new ArrayList();

        Optional<Clazz> op = clazzRepository.findById(clazzId);

        //其实用不到
        if(!op.isPresent()){
            return dataList;
        }

        //导航到 班级学生成绩单 班级学生选课
        String classStudentAchievementParas,clazzStudentInnovationParas,clazzStudentActivityParas,classStudentHomeworkParas,classStudentCourseParas,clazzParas,classStudentLogParas,classStudentLeaveParas,classStudentHonorParas;
        Map m = new HashMap();

        m.put("classNum",op.get().getClassNum());

        classStudentAchievementParas="model=classStudentAchievement&clazzId="+clazzId;
        m.put("classStudentAchievement","班级学生成绩单");
        m.put("classStudentAchievementParas",classStudentAchievementParas);

        clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+clazzId;
        m.put("clazzStudentInnovation","班级创新统计");
        m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);

        clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+clazzId;
        m.put("clazzStudentActivity","班级活动统计");
        m.put("clazzStudentActivityParas",clazzStudentActivityParas);

        classStudentHomeworkParas="model=classStudentHomework&clazzId="+clazzId;
        m.put("classStudentHomework","班级学生作业情况");
        m.put("classStudentHomeworkParas",classStudentHomeworkParas);

        //classStudentCourseParas ="model=classStudentCourse&clazzId="+clazzId;
        //m.put("classStudentCourse","班级学生选课情况");
        //m.put("classStudentCourseParas",classStudentCourseParas);
        classStudentLogParas = "model=classStudentLog&clazzId="+clazzId;
        m.put("classStudentLog","班级学生花销信息");
        m.put("classStudentLogParas",classStudentLogParas);

        classStudentLeaveParas = "model=classStudentLeave&clazzId="+clazzId;
        m.put("classStudentLeave","班级学生外出请假信息");
        m.put("classStudentLeaveParas",classStudentLeaveParas);

        classStudentHonorParas ="model=classStudentHonor&clazzId="+clazzId;
        m.put("classStudentHonor","班级学生荣誉情况");
        m.put("classStudentHonorParas",classStudentHonorParas);

        clazzParas = "model=clazz&gradeId="+op.get().getGrade().getId();
        m.put("clazz","返回");
        m.put("clazzParas",clazzParas);

        dataList.add(m);
        return dataList;
    }

    @PostMapping("/clazzStatisticInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStatisticInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStatisticMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }
}
