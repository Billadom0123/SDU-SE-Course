package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Course;
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
public class GradeStudentHomework {
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

    public List getAllCourseMapList(Integer gradeId){
        List dataList = new ArrayList();

        Optional<Grade> grade = gradeRepository.findById(gradeId);
        List<Course> courseList = courseRepository.findCourseListByGradeId(gradeId);
        if(courseList==null||courseList.size()==0){
            return dataList;
        }

        Map m;
        String courseNameParas,gradeStatisticParas;
        for(Course C:courseList){
            m = new HashMap();
            //m.put("classNum",clazz.get().getClassNum());
            m.put("gradeNum",grade.get().getGradeNum());
            m.put("courseNum",C.getCourseNum());
            courseNameParas = "model=gradeStudentHomeworkCourse&courseId="+C.getId()+"&gradeId="+gradeId;
            m.put("courseName",C.getCourseName());
            m.put("courseNameParas",courseNameParas);
            gradeStatisticParas="model=gradeStatistic&gradeId="+gradeId;
            m.put("gradeStatistic","返回");
            m.put("gradeStatisticParas",gradeStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/gradeStudentHomeworkInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHomeworkInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer gradeId = dataRequest.getInteger("gradeId");
        List dataList = getAllCourseMapList(gradeId);
        return CommonMethod.getReturnData(dataList);
    }

}
