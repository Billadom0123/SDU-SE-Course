package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyInnovation;
import org.fatmansoft.teach.models.Student;
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

public class MyLectureController {
    private static Integer studentId;
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
    private MyInnovationRepository myInnovationRepository;

    public List getMyPracticeListByStudentId(Integer Id){
        List dataList = new ArrayList<>();

        List<MyInnovation> myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);

        Map m;
        for(MyInnovation myInnovation:myInnovationList){
            m = new HashMap();
            m.put("id",myInnovation.getId());
            m.put("studentNum",myInnovation.getStudent().getStudentNum());
            m.put("studentName",myInnovation.getStudent().getStudentName());
            if(myInnovation.getLecture()!=null){
                m.put("lecture",myInnovation.getLecture());
            }
            else{
                continue;
            }
            String myInnovationParas = "model=myInnovation&studentId="+studentId;
            m.put("myInnovation","返回");
            m.put("myInnovationParas",myInnovationParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myLectureInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLectureInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyPracticeListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myLectureEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLectureEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        MyInnovation s= null;
        Optional<MyInnovation> op;
        if(id != null) {
            op = myInnovationRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("lecture", s.getLecture());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/myLectureEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myLectureEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String practice = CommonMethod.getString(form,"lecture");
        MyInnovation s= null;
        Optional<MyInnovation> op;
        if(id != null) {
            op = myInnovationRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new MyInnovation();   //不存在 创建实体对象
            id = myInnovationRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        Optional<Student> stu = studentRepository.findById(studentId);
        s.setId(id);  //设置属性
        s.setStudent(stu.get());
        s.setLecture(practice);
        myInnovationRepository.save(s);
        return CommonMethod.getReturnData(s.getId());
    }

    @PostMapping("/myLectureDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myLectureDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        MyInnovation s= null;
        Optional<MyInnovation> op;
        if(id != null) {
            op= myInnovationRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            myInnovationRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

}

