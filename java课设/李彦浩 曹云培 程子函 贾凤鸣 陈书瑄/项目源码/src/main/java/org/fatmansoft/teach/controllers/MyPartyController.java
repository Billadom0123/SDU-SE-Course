
package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyActivity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLOutput;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyPartyController {
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
    private MyActivityRepository myActivityRepository;

    public List getMyPartyListByStudentId(Integer Id){
        List dataList = new ArrayList<>();

        List<MyActivity> myActivityList = myActivityRepository.findMyActivityByStudentId(studentId);

        Map m;
        for(MyActivity myActivity:myActivityList){
            m = new HashMap();
            m.put("id",myActivity.getId());
            m.put("studentNum",myActivity.getStudent().getStudentNum());
            m.put("studentName",myActivity.getStudent().getStudentName());
            if(myActivity.getParty()!=null){
                m.put("party",myActivity.getParty());
            }
            else{
                continue;
            }

            String myActivityParas = "model=myActivity&studentId="+studentId;
            m.put("myActivity","返回");
            m.put("myActivityParas",myActivityParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myPartyInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myPartyInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyPartyListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myPartyEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myPartyEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        MyActivity s= null;
        Optional<MyActivity> op;
        if(id != null) {
            op = myActivityRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("party", s.getParty());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/myPartyEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myPartyEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String party = CommonMethod.getString(form,"party");
        MyActivity s= null;
        Optional<MyActivity> op;
        if(id != null) {
            op = myActivityRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new MyActivity();   //不存在 创建实体对象
            id = myActivityRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        Optional<Student> stu = studentRepository.findById(studentId);
        s.setId(id);  //设置属性
        s.setStudent(stu.get());
        s.setParty(party);
        myActivityRepository.save(s);
        return CommonMethod.getReturnData(s.getId());
    }

    @PostMapping("/myPartyDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse myPartyDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        MyActivity s= null;
        Optional<MyActivity> op;
        if(id != null) {
            op= myActivityRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            myActivityRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

}
