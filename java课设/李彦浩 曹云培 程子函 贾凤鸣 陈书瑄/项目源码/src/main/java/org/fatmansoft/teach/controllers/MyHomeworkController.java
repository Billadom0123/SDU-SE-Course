package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.HomeworkRepository;
import org.fatmansoft.teach.repository.MyCourseRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyHomeworkController {
    private static Integer studentId;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private MyCourseRepository myCourseRepository;

    public List getMyHomeworkListByStudentId(Integer Id) {
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        //根据这个Id查询出所有的Achievement对象
        List<Homework> HomeworkList = homeworkRepository.FindHomeworkScoreByStudentIdNative(Id);

        Map m;
        for (Homework A : HomeworkList) {
            m = new HashMap();
            m.put("id", A.getId());
            m.put("majorNum", A.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName", A.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum", A.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum", A.getStudent().getClazz().getClassNum());
            m.put("studentNum", A.getStudent().getStudentNum());
            m.put("studentName", A.getStudent().getStudentName());
            m.put("courseNum", A.getCourse().getCourseNum());
            m.put("courseName", A.getCourse().getCourseName());
            m.put("homeworkScore", A.getHomeworkScore());
            String StudentDetailsParas = "model=StudentDetails&studentId="+A.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myHomeworkInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myHomeworkInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyHomeworkListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myHomeworkEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myHomeworkEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Homework a = null;
        Optional<Homework> op;
        if(id!=null){
            op = homeworkRepository.findById(id);
            if(op.isPresent()){
                a = op.get();
            }//如果已經存在成績的話，要改的只是這門課的成績,所以只能選這門課程
            /*寫成list的目的是讓label為課程名 value是傳給後端的courseId*/
            Map form = new HashMap();
            if(a!=null) {
                form.put("id", a.getId());
                List<Map> courseList = new ArrayList<>();
                Map m = new HashMap<>();
                form.put("courseId","");
                m.put("label",a.getCourse().getCourseName());
                m.put("value",a.getCourse().getId());
                courseList.add(m);
                form.put("courseIdList", courseList);
                form.put("score", a.getHomeworkScore());
            }
            return CommonMethod.getReturnData(form);
        }

        //如過是新添加的話，應該是對學生所有選過的課添加作業成績，一門課可以有多個作業成績，所以這裏不加判重
 /*       Optional<Student> stu = studentRepository.findById(studentId);
        Student student = stu.get();
        List<Homework> homeworks=homeworkRepository.findHomeworkByStudentId(studentId);
        //已經有作業成績的課程
        List<Course> myhasScoreCourse = new ArrayList<>();
        for(Homework homework:homeworks) {myhasScoreCourse.add(homework.getCourse());}*/
        //已经选过的课
        List<MyCourse> myCourses = myCourseRepository.findMyCourseByStudentId(studentId);

        Map form=new HashMap<>();
        form.put("courseId","");
        List<Map> courseList = new ArrayList<>();
        for(MyCourse myCourse:myCourses){
            Map m = new HashMap<>();
            m.put("label",myCourse.getCourse().getCourseName());
            m.put("value",myCourse.getCourse().getId());
            courseList.add(m);
        }
        form.put("courseIdList", courseList);

     /*   Map form = new HashMap();
        if(a!=null){
            form.put("id",a.getId());
            //form.put("majorNum",a.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            //form.put("gradeNum",a.getStudent().getClazz().getGrade().getGradeNum());
            //form.put("classNum",a.getStudent().getClazz().getClassNum());
            //form.put("studentNum",a.getStudent().getStudentNum());
            form.put("courseNum",a.getCourse().getCourseNum());
            form.put("score",a.getScore());
        }*/

        return CommonMethod.getReturnData(form);
    /*    if (id != null) {
            op = homeworkRepository.findById(id);
            if (op.isPresent()) {
                a = op.get();
            }
        }

        Map form = new HashMap();
        if (a != null) {
            form.put("id", a.getId());
            form.put("courseNum", a.getCourse().getCourseNum());
            form.put("homeworkScore", a.getHomeworkScore());
        }

        return CommonMethod.getReturnData(form);*/
    }

    @PostMapping("/myHomeworkEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myHomeworkEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form, "id");
        //String majorNum = CommonMethod.getString(form, "majorNum");
        //Integer gradeNum = CommonMethod.getInteger(form, "gradeNum");
        //Integer classNum = CommonMethod.getInteger(form, "classNum");
        //String studentNum = CommonMethod.getString(form, "studentNum");
        Integer courseId = CommonMethod.getInteger(form,"courseId");//課程號
        Double homeworkScore = CommonMethod.getDouble(form, "homeworkScore");

        Homework a = null;
        Optional<Homework> op;

        //编辑
        if (id != null) {
            op = homeworkRepository.findById(id);
            if (op.isPresent()) {
                a = op.get();
            }
        }

        //添加
        if (a == null) {
            a = new Homework();
            id = homeworkRepository.getMaxId();
            //设置主键
            if (id == null) {
                id = 1;
            } else {
                id = id + 1;
            }
            a.setId(id);
        }
        Optional<Student> s = studentRepository.findById(studentId);
      /*  Integer gradeId = null;
        if(s.isPresent()){
            gradeId = s.get().getClazz().getGrade().getId();
        }*/
        Optional<Course> c = courseRepository.findById(courseId);
        a.setStudent(s.get());
        //System.out.println(s.get().getStudentName()+" from courseAchievementEditSubmit");
        a.setCourse(c.get());
        a.setHomeworkScore(homeworkScore);
/*        if(s.isPresent()&&c.isPresent()){
            //判重 一个学生的某一门课不能提交两条记录 如果提交了第二条 删了重新加一条
            Optional<Homework> homeworkCheck = homeworkRepository.OPFindHomeworkByStudentIdAndCourseId(studentId,c.get().getId());
            homeworkCheck.ifPresent(homework -> homeworkRepository.delete(homework));
            //先选了课才能填成绩(jfm问题)
            Optional<MyCourse> mc =myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,c.get().getId());
            if(mc.isPresent()){
                a.setStudent(s.get());
                //System.out.println(s.get().getStudentName()+" from courseAchievementEditSubmit");
                a.setCourse(c.get());
                a.setHomeworkScore(homeworkScore);
            }
            else{
                return CommonMethod.getReturnMessageError("提交失败，该学生未选择这门课或是不存在该学生");
            }
        }
        else{
            return  CommonMethod.getReturnMessageError("提交失败，不存在该学生或是不存在该课程");
        }
*/
        homeworkRepository.save(a);  //新建和修改都调用save方法

        return CommonMethod.getReturnData(a.getId());  // 将记录的id返回前端
    }

    @PostMapping("/myHomeworkDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myHomeworkDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Homework a = null;
        Optional<Homework> op;

        if (id != null) {
            op = homeworkRepository.findById(id);
            if (op.isPresent()) {
                a = op.get();
            }
        }

        if (a != null) {
            homeworkRepository.delete(a);
        }

        return CommonMethod.getReturnMessageOK();
    }
}
