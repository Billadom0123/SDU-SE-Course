package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.Course;
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

public class ClassStudentAchievementController {
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

    //上一个页面传下来的是班级id 查找这个id所属的年级id 年级id下的所有课程都是这个班级学生要学的(必修课相当于)
    //随后把每一门课的标签都做成超链 导航到下一个页面 下一个页面是关于这个班这个课的所有学生的成绩
    public List getAllCourseMapList(Integer clazzId){
        List dataList = new ArrayList();

        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        List<Course> courseList = courseRepository.findCourseListByGradeId(clazz.get().getGrade().getId());
        if(courseList==null||courseList.size()==0){
            return dataList;
        }

        Map m;
        String courseNameParas,clazzStatisticParas;
        for(Course C:courseList){
           m = new HashMap();
           m.put("classNum",clazz.get().getClassNum());
           m.put("courseNum",C.getCourseNum());
           courseNameParas = "model=classStudentAchievementCourse&courseId="+C.getId()+"&clazzId="+clazz.get().getId();
           m.put("courseName",C.getCourseName());
           m.put("courseNameParas",courseNameParas);
           clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.get().getId();
           m.put("clazzStatistic","返回");
           m.put("clazzStatisticParas",clazzStatisticParas);
           dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentAchievementInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentAchievementInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllCourseMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }
}
