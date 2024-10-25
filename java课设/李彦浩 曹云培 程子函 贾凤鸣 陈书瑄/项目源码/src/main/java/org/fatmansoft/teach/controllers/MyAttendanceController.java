package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyAttendanceController {
    private  static Integer studentId;
    private  static Integer courseId;
    @Autowired
    private MyAttendanceRepository myAttendanceRepository;
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

    public List getMyAttendanceMapList(Integer studentId,Integer courseId){
        List dataList = new ArrayList();

        List<MyAttendance> myAttendanceList = myAttendanceRepository.findMyAttendanceByStudentIdAndCourseId(studentId,courseId);

        Collections.sort(myAttendanceList);

        Map m;
        for(int i=0;i<myAttendanceList.size();i++){
            MyAttendance myAttendance = myAttendanceList.get(i);
            m=new HashMap();
            m.put("id",myAttendance.getId());
            m.put("studentNum",myAttendance.getMyCourse().getStudent().getStudentNum());
            m.put("studentName",myAttendance.getMyCourse().getStudent().getStudentName());
            m.put("courseName",myAttendance.getMyCourse().getCourse().getCourseName());
            m.put("classTime", DateTimeTool.parseDateTime(myAttendance.getClassTime(),"yyyy-MM-dd"));
            if("1".equals(myAttendance.getAttendance())){
                m.put("attendance","是");
            }
            else{
                m.put("attendance","否");
            }
            String myCourseParas = "model=myCourse&studentId="+myAttendance.getMyCourse().getStudent().getId();
            m.put("myCourse","返回");
            m.put("myCourseParas",myCourseParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/myAttendanceInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAttendanceInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        courseId = dataRequest.getInteger("courseId");
        List dataList = getMyAttendanceMapList(studentId,courseId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myAttendanceEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAttendanceEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        MyAttendance ma = null;
        Optional<MyAttendance> op;

        if(id!=null){
            op = myAttendanceRepository.findById(id);
            if(op.isPresent()){
                ma = op.get();
            }
        }

        Map form = new HashMap();

        if(ma!=null){
            form.put("id",ma.getId());
            form.put("classTime",DateTimeTool.parseDateTime(ma.getClassTime(),"yyyy-MM-dd"));
            form.put("attendance",ma.getAttendance());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/myAttendanceEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAttendanceEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        Date classTime = CommonMethod.getDate(form,"classTime");
        String attendance = CommonMethod.getString(form,"attendance");

        MyAttendance ma = null;
        Optional<MyAttendance> op;

        //编辑
        if(id != null){
            op = myAttendanceRepository.findById(id);
            if(op.isPresent()){
                ma = op.get();
            }
        }

        //添加
        if(ma==null){
            ma=new MyAttendance();
            id = myAttendanceRepository.getMaxId();
            //设置主键
            if(id==null){
                id = 1;
            }
            else{
                id = id+1;
            }
            ma.setId(id);
        }

        Optional<Student> s = studentRepository.findById(studentId);
        //Integer gradeId = null;
        //if(s.isPresent()){
        //gradeId = s.get().getClazz().getGrade().getId();
        //}
        //Optional<Course> c = courseRepository.OPFindCourseByGradeIdAndCourseNum(gradeId,courseNum);
        Optional<Course> c= courseRepository.findById(courseId);

        if(s.isPresent()){//&&c.isPresent()){
            //这里不用判重了，万一一天两节课呢
            //Optional<MyCourse> check = myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,c.get().getId());
            //if(check.isPresent()){
               // return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            //}
            //else{
                ma.setClassTime(classTime);
                ma.setAttendance(attendance);
                ma.setMyCourse(myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,courseId).get());
                myAttendanceRepository.save(ma);
            //}
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }

        return CommonMethod.getReturnData(ma.getId());
    }

    @PostMapping("/myAttendanceDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAttendanceDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        MyAttendance ma = null;
        Optional<MyAttendance> op;



        if(id!=null){
            op = myAttendanceRepository.findById(id);
            if(op.isPresent()){
                ma = op.get();
            }
        }

        if(ma!=null){
            myAttendanceRepository.delete(ma);
        }

        return CommonMethod.getReturnMessageOK();
    }
}
