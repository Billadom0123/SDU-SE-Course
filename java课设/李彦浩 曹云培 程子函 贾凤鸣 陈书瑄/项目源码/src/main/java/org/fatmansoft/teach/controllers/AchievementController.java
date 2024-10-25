package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.MyCourse;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.AchievementRepository;
import org.fatmansoft.teach.repository.CourseRepository;
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

//注意map的键与YAML标签的匹配

public class AchievementController  {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private MyCourseRepository myCourseRepository;

    //根据学生姓名或学号查找成绩的方法 返回一个map的list
    //这个方法传参传String 是入口方法
    public List getAchievementMapListByNumName(String NumName){
        List dataList = new ArrayList();

        //先用student的repository找出符合条件的所有学生
        List<Student> SList = studentRepository.findStudentListByNumNameNative(NumName);

        //特判空数表
        if(SList == null|| SList.size() == 0){
            return dataList;
        }

        Map m;
        //遍历student数表
        for(Student stu:SList){
            //获取出student的ID
            Integer ID = stu.getId();
            //根据ID用achievement的repository查所有满足的成绩信息
            //这个方法传参传Integer 是核心方法
            List<Achievement> AList = achievementRepository.FindScoreByStudentIdNative(ID);

            //查出来之后以map的形式放入最终返回的数表中
            for(Achievement A:AList){
                m = new HashMap();
                m.put("id",A.getId());
                m.put("studentNum",A.getStudent().getStudentNum());
                m.put("studentName",A.getStudent().getStudentName());
                m.put("courseNum",A.getCourse().getCourseNum());
                m.put("courseName",A.getCourse().getCourseName());
                m.put("score",A.getScore());
                dataList.add(m);
            }
        }

        return dataList;
    }

    //成绩页面初始化方法
    @PostMapping("/achievementInit")
    @PreAuthorize("hasRole('ADMIN')")
    //为了不在repository中写冗余的方法，直接单独写一份页面初始化方法
    public DataResponse achievementInit(@Valid @RequestBody DataRequest dataRequest){
        List dataList = getAchievementMapListByNumName("");
        return CommonMethod.getReturnData(dataList);
    }

    //根据学生主键或课程主键查询的界面
    //目的是要根据输入的学生姓名或学号来查询数据
    @PostMapping("/achievementQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("NumName");

        //入口处传字符串
        List dataList = getAchievementMapListByNumName(numName);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/achievementEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Achievement A = null;

        Optional<Achievement> op;

        if(id != null){
            op = achievementRepository.findById(id);
            if(op.isPresent()){
                A = op.get();
            }
        }
        Map form = new HashMap();
        if(A!=null){
            form.put("id",A.getId());
            form.put("studentNum",A.getStudent().getStudentNum());
            form.put("courseNum",A.getCourse().getCourseNum());
            form.put("score",A.getScore());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/achievementEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse achievementEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        Integer studentNum = CommonMethod.getInteger(form,"studentNum");
        Integer courseNum = CommonMethod.getInteger(form,"courseNum");
        Double score = CommonMethod.getDouble(form,"score");

        Achievement A= null;
        Optional<Achievement> op;

        if(id != null) {
            op= achievementRepository.findById(id);
            if(op.isPresent()) {
                A = op.get();
            }
        }

        if(A == null) {
            A = new Achievement();
            id = achievementRepository.getMaxId();
            if(id == null)
                id = 1;
            else
                id = id+1;
            A.setId(id);
        }

        //在Student和Course中分别加了返回Optional的根据号码和名称查询的方法
        //如果仍然要用findById则会出现No value present错误
        Optional<Student> s = studentRepository.OPFindStudentListByNumNameNative(studentNum.toString());
        Optional<Course> c= courseRepository.OPFindCourseListByNumNameNative(courseNum.toString());

        if(s.isPresent()&&c.isPresent()){
            //判重 一个学生的某一门课不能有两个成绩
            Integer sId = s.get().getId();
            Integer cId = c.get().getId();
            Optional<Achievement> check = achievementRepository.checkAchievement(sId,cId);
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，已经存在该学生该课程的成绩");
            }
            //先选了课才能填成绩(jfm问题)
            Optional<MyCourse> mc =myCourseRepository.checkMyCourse(s.get().getId(),c.get().getId());
            if(mc.isPresent()){
                A.setStudent(s.get());  //设置属性
                A.setCourse(c.get());
                A.setScore(score);
            }
            else{
                return CommonMethod.getReturnMessageError("提交失败，该学生未选择这门课或是不存在该学生");
            }
        }
        else{
            return  CommonMethod.getReturnMessageError("提交失败，不存在该学生");
        }

        achievementRepository.save(A);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(A.getId());  // 将记录的id返回前端
    }

    @PostMapping("/achievementDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Achievement A = null;
        Optional<Achievement> op;

        if(id !=null){
            op = achievementRepository.findById(id);
            if(op.isPresent()){
                A = op.get();
            }
        }

        if(A != null){
            achievementRepository.delete(A);
        }

        return CommonMethod.getReturnMessageOK();
    }
}
