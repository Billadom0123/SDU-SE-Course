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
public class ClassStudentHomeworkController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private MyCourseRepository myCourseRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;
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
            courseNameParas = "model=classStudentHomeworkCourse&courseId="+C.getId()+"&clazzId="+clazz.get().getId();
            m.put("courseName",C.getCourseName());
            m.put("courseNameParas",courseNameParas);
            clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.get().getId();
            m.put("clazzStatistic","返回");
            m.put("clazzStatisticParas",clazzStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentHomeworkInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentHomeworkInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllCourseMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }
}

