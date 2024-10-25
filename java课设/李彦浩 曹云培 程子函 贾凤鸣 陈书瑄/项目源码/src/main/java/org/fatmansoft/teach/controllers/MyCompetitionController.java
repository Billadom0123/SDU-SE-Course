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

public class MyCompetitionController {
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
            if(myInnovation.getCompetition()!=null){
                m.put("competition",myInnovation.getCompetition());
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

    @PostMapping("/myCompetitionInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCompetitionInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyPracticeListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myCompetitionEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCompetitionEditInit(@Valid @RequestBody DataRequest dataRequest) {
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
            form.put("competition", s.getCompetition());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/myCompetitionEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myCompetitionEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String practice = CommonMethod.getString(form,"competition");
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
        s.setCompetition(practice);
        myInnovationRepository.save(s);
        return CommonMethod.getReturnData(s.getId());
    }

    @PostMapping("/myCompetitionDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myCompetitionDelete(@Valid @RequestBody DataRequest dataRequest) {
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
