package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.IntroduceService;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class StudentQueryController {
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
    @Autowired
    private MyLogRepository myLogRepository;
    @Autowired
    private MyLeaveRepository myLeaveRepository;
    @Autowired
    private HonorRepository honorRepository;

    public List getStudentMapList(String majorNum,Integer gradeNum,Integer clazzNum,String studentNum){
        List dataList = new ArrayList();
        Optional<Student> sList = studentRepository.OPFindStudentByMajorGradeClazzStudentNum(majorNum,gradeNum,clazzNum,studentNum);

        if(!sList.isPresent()){
            return dataList;
        }

        Student s;
        Map m;
        String studentNameParas,clazzParas,StudentDetailsParas;
        //for(int i = 0; i < sList.size();i++) {
            s = sList.get();
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            studentNameParas = "model=introduce&studentId=" + s.getId();
            m.put("studentName",s.getStudentName());
            m.put("studentNameParas",studentNameParas);
            if("1".equals(s.getSex())) {
                m.put("sex","男");
            }else {
                m.put("sex","女");
            }
            m.put("age",s.getAge());
            m.put("majorNum",s.getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",s.getClazz().getGrade().getMajor().getMajorName());
            m.put("grade",s.getClazz().getGrade().getGradeNum());
            m.put("class",s.getClazz().getClassNum());
            m.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));  //时间格式转换字符串

            StudentDetailsParas = "model=StudentDetails&studentId="+s.getId();
            m.put("StudentDetails","详细信息");
            m.put("StudentDetailsParas",StudentDetailsParas);

            dataList.add(m);
        //}

        return dataList;
    }

    @PostMapping("/studentQueryInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentQueryInit(@Valid @RequestBody DataRequest dataRequest) {
        return CommonMethod.getReturnData(new ArrayList());
    }

    @PostMapping("/studentQueryQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentQueryQuery(@Valid @RequestBody DataRequest dataRequest) {
        //Integer clazzId = dataRequest.getInteger("clazzId");
        String[] info= dataRequest.getString("info").split(" ");
        List dataList = new ArrayList();
        if(info.length==4){
            dataList = getStudentMapList(info[0],Integer.parseInt(info[1]),Integer.parseInt(info[2]),info[3]);

        }
        else{
            return CommonMethod.getReturnMessageError("查询无果，请检查输入格式");
        }
        return CommonMethod.getReturnData(dataList);
    }
}
