package org.fatmansoft.teach.controllers;

//MyAchievement页面
//希望可以在前端点击“我的成绩”按钮直接显示某学生所有科目的成绩 因为所有的成绩都已经在成绩管理里添加过了 所以不应该重新添加而是直接显示

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyAchievementController {
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

    //根据学生Id获取所有的MyAchievement
    public List getMyAchievementListByStudentId(Integer Id){
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        //根据这个Id查询出所有的Achievement对象
        List<Achievement> AchievementList = achievementRepository.FindScoreByStudentIdNative(Id);

        Map m;
        for(Achievement A:AchievementList){
            m = new HashMap();
            m.put("id",A.getId());
            m.put("majorNum",A.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",A.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum",A.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum",A.getStudent().getClazz().getClassNum());
            m.put("studentNum",A.getStudent().getStudentNum());
            m.put("studentName",A.getStudent().getStudentName());
            m.put("courseNum",A.getCourse().getCourseNum());
            m.put("courseName",A.getCourse().getCourseName());
            m.put("score",A.getScore());
            String StudentDetailsParas = "model=StudentDetails&studentId="+A.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myAchievementInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAchievementInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyAchievementListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myAchievementEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAchievementEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Achievement a = null;
        Optional<Achievement> op;

        if (id != null) {
            op = achievementRepository.findById(id);
            if (op.isPresent()) {
                a = op.get();
            }
            Map form = new HashMap();
            if (a != null) {
                form.put("id", a.getId());
                List courseList = new ArrayList<>();
                Map m;

                List<Achievement> achievements = achievementRepository.FindScoreByStudentIdNative(studentId);

                //已經有成績的課程
                List<Course> myhasScoreCourse = new ArrayList<>();
                for (Achievement achievement : achievements) {
                    myhasScoreCourse.add(achievement.getCourse());
                }

                //已经选过的课
                List<MyCourse> myCourses = myCourseRepository.findMyCourseByStudentId(studentId);

                //最后剩下的，选了课但是没成绩的
                /*List<Course> myCourseList = new ArrayList<>();
                for(MyCourse myCourse:myCourses){
                    if(!myhasScoreCourse.contains(myCourse.getCourse())){
                        myCourseList.add(myCourse.getCourse());
                    }
                }*/

                //取差集
                myCourses.removeIf(myCourse -> myhasScoreCourse.contains(myCourse.getCourse()));
                List<Course> myCourseList = new ArrayList<>();
                for (MyCourse myCourse : myCourses) {
                    if (!myhasScoreCourse.contains(myCourse.getCourse())) {
                        myCourseList.add(myCourse.getCourse());
                    }
                }

                    for (Course course : myCourseList) {
                        //if(myhasScoreCourse.contains(course)) {continue;}
                        m = new HashMap<>();
                        m.put("label", course.getCourseName());
                        m.put("value", course.getId());
                        courseList.add(m);
                    }
                    form.put("courseId", a.getCourse().getId());
                    //form.put("courseId",")";
                    m = new HashMap();
                    m.put("label",a.getCourse().getCourseName());//???
                    m.put("value",a.getCourse().getId());
                    courseList.add(m);
                    form.put("courseIdList", courseList);
                    form.put("score", a.getScore());
                }
                return CommonMethod.getReturnData(form);
            }

            //如過是新添加的話，就是學生已經選過的但是沒有成績的課程
            Optional<Student> stu = studentRepository.findById(studentId);
            Student student = stu.get();
            //其實就是根據學號找到achievement//根据学生Id
            List<Achievement> achievements = achievementRepository.FindScoreByStudentIdNative(studentId);
            //已經有成績的課程
            List<Course> myhasScoreCourse = new ArrayList<>();
            for (Achievement achievement : achievements) {
                myhasScoreCourse.add(achievement.getCourse());
            }
            //已经选过的课
            List<MyCourse> myCourses = myCourseRepository.findMyCourseByStudentId(studentId);
            List<Course> myCouseList = new ArrayList<>();
            for (MyCourse myCourse : myCourses) {
                myCouseList.add(myCourse.getCourse());
            }

            Map form = new HashMap<>();
            form.put("courseId", "");
            List<Map> courseList = new ArrayList<>();
            for (Course course : myCouseList) {
                if (myhasScoreCourse.contains(course)) continue;
                Map m = new HashMap<>();
                m.put("label", course.getCourseName());
                m.put("value", course.getId());
                courseList.add(m);
            }
            form.put("courseIdList", courseList);

            return CommonMethod.getReturnData(form);
        }

    @PostMapping("/myAchievementEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAchievementEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        Integer courseId = CommonMethod.getInteger(form,"courseId");//課程號
        //System.out.println(courseId);
        Double score = CommonMethod.getDouble(form,"score");

        Achievement A = null;
        Optional<Achievement> op;

        //编辑
        if(id != null){
            op = achievementRepository.findById(id);
            if(op.isPresent()){
                A = op.get();
            }
        }

        //添加
        if(A==null){
            A=new Achievement();
            id = achievementRepository.getMaxId();
            //设置主键
            if(id==null){
                id = 1;
            }
            else{
                id = id+1;
            }
            A.setId(id);
            Optional<Course> c = courseRepository.findById(courseId);
            Optional<Achievement> achievementCheck = achievementRepository.OPFindAchievementByStudentIdAndCourseId(studentId,c.get().getId());
            if(achievementCheck.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，已经存在该学生的成绩");
            }
        }

        Optional<Student> s = studentRepository.findById(studentId);
        Optional<Course> c = courseRepository.findById(courseId);
        A.setStudent(s.get());
        //System.out.println(s.get().getStudentName()+" from courseAchievementEditSubmit");
        A.setCourse(c.get());
        A.setScore(score);
        //Optional<Achievement> achievementCheck = achievementRepository.OPFindAchievementByMajorGradeClassStudentCourseNum(majorNum,gradeNum,classNum,studentNum,courseNum);
/*
        if(s.isPresent()&&c.isPresent()){
            //判重 一个学生的某一门课不能提交两条记录 如果提交了第二条 删了重新加一条
            Optional<Achievement> achievementCheck = achievementRepository.OPFindAchievementByStudentIdAndCourseId(studentId,c.get().getId());
            achievementCheck.ifPresent(achievement -> achievementRepository.delete(achievement));
            //先选了课才能填成绩(jfm问题)
            Optional<MyCourse> mc =myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(studentId,c.get().getId());
            if(mc.isPresent()){
                A.setStudent(s.get());
                //System.out.println(s.get().getStudentName()+" from courseAchievementEditSubmit");
                A.setCourse(c.get());
                A.setScore(score);
            }
            else{
                return CommonMethod.getReturnMessageError("提交失败，该学生未选择这门课或是不存在该学生");
            }
        }
        else{
            return  CommonMethod.getReturnMessageError("提交失败，不存在该学生或是不存在该课程");
        }*/

        achievementRepository.save(A);  //新建和修改都调用save方法

        return CommonMethod.getReturnData(A.getId());  // 将记录的id返回前端
    }

    @PostMapping("/myAchievementDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myAchievementDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Achievement a = null;
        Optional<Achievement> op;

        if(id!=null){
            op = achievementRepository.findById(id);
            if(op.isPresent()){
                a = op.get();
            }
        }

        if(a!=null){
            achievementRepository.delete(a);
        }

        return CommonMethod.getReturnMessageOK();
    }

}

