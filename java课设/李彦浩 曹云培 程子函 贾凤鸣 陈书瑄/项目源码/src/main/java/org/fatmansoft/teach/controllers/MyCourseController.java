package org.fatmansoft.teach.controllers;

//MyCourse页面：通过Student页面中的 Link类型的键导航 显示的是某个学生所有所学的课程
//希望在前端看到的:0.学号 1.学生姓名 2.课程号 3.课程名 4.编辑删除按钮

import org.fatmansoft.teach.models.*;
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

public class MyCourseController {
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
    private MyAttendanceRepository myAttendanceRepository;

    //根据学生Id获取所有的MyCourse
    public List getMyCourseListByStudentId(Integer Id){
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        List<MyCourse> tempList = myCourseRepository.findMyCourseByStudentIdNative(Id);
        //按课程号排序
        Collections.sort(tempList);

        Map m;
        String StudentDetailsParas,myAttendanceParas;
        for(MyCourse mc:tempList){
            m = new HashMap();
            m.put("id",mc.getId());
            m.put("majorNum",mc.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",mc.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum",mc.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum",mc.getStudent().getClazz().getClassNum());
            m.put("studentNum",mc.getStudent().getStudentNum());
            m.put("studentName",mc.getStudent().getStudentName());
            m.put("courseNum",mc.getCourse().getCourseNum());
            m.put("courseName",mc.getCourse().getCourseName());
            myAttendanceParas="model=myAttendance&studentId="+mc.getStudent().getId()+"&courseId="+mc.getCourse().getId();
            m.put("myAttendance","我的考勤");
            m.put("myAttendanceParas",myAttendanceParas);
            StudentDetailsParas = "model=StudentDetails&studentId="+mc.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myCourseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyCourseListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myCourseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        MyCourse mc = null;
        Optional<MyCourse> op;

        if(id!=null){//這門課我已經選過
            op = myCourseRepository.findById(id);
            if(op.isPresent()){
                mc = op.get();//如果當前是編輯頁面的話，可選項裏不會出現這門課(好像也沒啥改的)
            }
        }

        //如果是一个学生新添加的课程
        Optional<Student> stu = studentRepository.findById(studentId);
        Student student = stu.get();

        //学生可以选择的所有课
        List<Course> c = courseRepository.findCourseListByGradeId(student.getClazz().getGrade().getId());
        //已经选过的课
        List<MyCourse> myCourses = myCourseRepository.findMyCourseByStudentId(studentId);
        List<Course> myCouseList = new ArrayList<>();
        for (MyCourse myCourse : myCourses) {
            myCouseList.add(myCourse.getCourse());
        }
        Map form=new HashMap<>();
        form.put("courseId","");
        List<Map> courseList = new ArrayList<>();
        for (Course course : c) {
            if (myCouseList.contains(course)) continue;
            Map m = new HashMap<>();
            m.put("label", course.getCourseName());
            m.put("value", course.getId());
            courseList.add(m);
        }
        form.put("courseIdList", courseList);

  /*      List courseIdList = new ArrayList();
        List<Course> courses = courseRepository.findAll();
        for(int i=0;i<courses.size();i++) {
            m = new HashMap();
            m.put("label", courses.get(i).getCourseName());
            //m.put("value", i + 1 + "");
            m.put("value",courses.get(i).getId());
            courseIdList.add(m);
        }

        Map form = new HashMap();
        form.put("courseId","");
        if(mc!=null){
            form.put("id",mc.getId());
            form.put("courseId",mc.getCourse().getId());
            //form.put("courseNum",mc.getCourse().getCourseNum());
        }

        form.put("courseIdList",courseIdList);*/

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/myCourseEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        Integer courseId = CommonMethod.getInteger(form,"courseId");//課程號

        MyCourse mc = null;
        Optional<MyCourse> op;

        //编辑
        if(id != null){
            op = myCourseRepository.findById(id);
            if(op.isPresent()){
                mc = op.get();
            }
        }

        //添加
        if(mc==null){
            mc=new MyCourse();
            id = myCourseRepository.getMaxId();
            //设置主键
            if(id==null){
                id = 1;
            }
            else{
                id = id+1;
            }
            mc.setId(id);
            Optional<Course> c= courseRepository.findById(courseId);
            Optional<MyCourse> check = myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,c.get().getId());
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            }
        }

        Optional<Student> s = studentRepository.findById(studentId);
        //Integer gradeId = null;
        //if(s.isPresent()){
            //gradeId = s.get().getClazz().getGrade().getId();
        //}
        //Optional<Course> c = courseRepository.OPFindCourseByGradeIdAndCourseNum(gradeId,courseNum);
        Optional<Course> c= courseRepository.findById(courseId);
        mc.setStudent(s.get());
        mc.setCourse(c.get());
        myCourseRepository.save(mc);
        //這裏的判重就沒必要了
    /*    if(s.isPresent()){//&&c.isPresent()){
            //判重
            Optional<MyCourse> check = myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,c.get().getId());
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            }
            else{
                mc.setStudent(s.get());
                mc.setCourse(c.get());
                myCourseRepository.save(mc);
            }
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }*/

        return CommonMethod.getReturnData(mc.getId());
    }

    @PostMapping("/myCourseDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        MyCourse mc = null;
        Optional<MyCourse> op;

        if(id!=null){
            op = myCourseRepository.findById(id);
            if(op.isPresent()){
                mc = op.get();
            }
        }

        if(mc!=null){
            List<Achievement> achievementList = achievementRepository.findAchievementByStudentIdAndCourseId(studentId,mc.getCourse().getId());
            if(achievementList!=null&&achievementList.size()!=0){
                for(Achievement achievement:achievementList){
                    achievementRepository.delete(achievement);
                }
            }
            List<MyAttendance> myAttendanceList = myAttendanceRepository.findMyAttendanceByStudentIdAndCourseId(studentId,mc.getCourse().getId());
            if(myAttendanceList!=null&&myAttendanceList.size()!=0){
                for(MyAttendance myAttendance:myAttendanceList){
                    myAttendanceRepository.delete(myAttendance);
                }
            }
            myCourseRepository.delete(mc);
        }

        return CommonMethod.getReturnMessageOK();
    }

}
