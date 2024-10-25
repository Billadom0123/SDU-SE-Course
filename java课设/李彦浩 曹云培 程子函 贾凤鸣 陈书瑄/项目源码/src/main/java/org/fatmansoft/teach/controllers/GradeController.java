package org.fatmansoft.teach.controllers;

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
public class GradeController {
    private static Integer majorId;
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
    private ClazzController clazzController;

    @PostMapping("/gradeInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeInit(@Valid @RequestBody DataRequest dataRequest) {
        majorId = dataRequest.getInteger("majorId");
        List dataList = new ArrayList();
        List<Grade> GradeList = gradeRepository.findGradeByMajorId(majorId);

        if(GradeList==null||GradeList.size()==0){
            return CommonMethod.getReturnData(dataList);
        }

        Collections.sort(GradeList);

        Grade g;
        Map m;
        String gradeNumParas,majorParas,courseParas,gradeStatisticParas;
        for(int i=0;i<GradeList.size();i++){
            m = new HashMap();
            g = GradeList.get(i);
            gradeNumParas="model=clazz&gradeId="+g.getId();
            majorParas="model=major";
            courseParas="model=course&gradeId="+g.getId();
            gradeStatisticParas="model=gradeStatistic&gradeId="+g.getId();
            m.put("id",g.getId());
            m.put("majorNum",g.getMajor().getMajorNum());
            m.put("majorName",g.getMajor().getMajorName());
            m.put("gradeNum",g.getGradeNum());
            m.put("gradeNumParas",gradeNumParas);
            m.put("major","返回");
            m.put("majorParas",majorParas);
            m.put("course","课程");
            m.put("courseParas",courseParas);
            m.put("gradeStatistic","年级信息统计");
            m.put("gradeStatisticParas",gradeStatisticParas);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    //由于就那么几个年级 query不写了 直接写edit
    @PostMapping("/gradeEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Grade g= null;
        Optional<Grade> op;
        if(id != null) {
            op= gradeRepository.findById(id);
            if(op.isPresent()) {
                g = op.get();
            }
        }
        Map form = new HashMap();
        if(g != null) {
            form.put("id",g.getId());
            //form.put("majorNum",g.getMajor().getMajorNum());
            form.put("gradeNum",g.getGradeNum());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/gradeEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse gradeEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        //String majorNum = CommonMethod.getString(form,"majorNum");
        Integer gradeNum = CommonMethod.getInteger(form,"gradeNum");

        Grade g= null;
        Optional<Grade> op;
        Optional<Grade> gradeCheck;

        gradeCheck = gradeRepository.OPFindGradeByMajorIdAndGradeNum(majorId,gradeNum);

        if(gradeCheck.isPresent()){
            return CommonMethod.getReturnMessageError("提交失败，已经存在该专业下的该年级");
        }

        if(id != null) {
            op= gradeRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                g = op.get();
            }
        }

        if(g == null) {
            g = new Grade();   //不存在 创建实体对象
            id = gradeRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            g.setId(id);  //设置新的id
        }

        Optional<Major> mj = majorRepository.findById(majorId);
        if(mj.isPresent()){
            g.setMajor(mj.get());  //设置属性
            g.setGradeNum(gradeNum);
            gradeRepository.save(g);  //新建和修改都调用save方法
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不能提交不存在的专业下的年级");
        }
        return CommonMethod.getReturnData(g.getId());  // 将记录的id返回前端
    }

    //grade下有clazz clazz下有student student下有achievement和myCourse
    /*@PostMapping("/gradeDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        //System.out.println(id+" from gradeDelete");
        Grade g = null;
        Optional<Grade> op;

        //需要附带删除的
        List<Clazz> ClazzList=null;
        List<Student> StudentList=null;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;
        List<Course> CourseList=null;

        if(id !=null){
            op = gradeRepository.findById(id);
            ClazzList = clazzRepository.findClazzByGradeId(id); //找出所有班级并删除
            StudentList = studentRepository.findStudentByGradeId(id); //找出所有学生并删除
            AchievementList= achievementRepository.findAchievementByGradeId(id); //找出所有成绩并删除
            MyCourseList = myCourseRepository.findMyCourseByGradeId(id); //找出所有我的课程并删除
            CourseList = courseRepository.findCourseListByGradeId(id);//找出所有的课程并删除
            if(op.isPresent()){
                g = op.get();
            }
        }
        if(g != null){
            if(CourseList!=null&&CourseList.size()!=0){
                for(Course course:CourseList){
                    courseRepository.delete(course);
                }
            }
            if(ClazzList!=null&&ClazzList.size()!=0){
                for(Clazz clazz:ClazzList){
                    clazzRepository.delete(clazz);
                }
            }
            if(StudentList!=null&&StudentList.size()!=0){
                for(Student student:StudentList){
                    studentRepository.delete(student);
                }
            }
            if(AchievementList!=null&&AchievementList.size()!=0){
                for(Achievement A:AchievementList){
                    achievementRepository.delete(A);
                }
            }
            if(MyCourseList!=null&&MyCourseList.size()!=0){
                for(MyCourse mc:MyCourseList){
                    myCourseRepository.delete(mc);
                }
            }
            gradeRepository.delete(g);
        }
        return CommonMethod.getReturnMessageOK();
    }*/

    @PostMapping("/gradeDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Grade g = null;
        Optional<Grade> op;

        //需要附带删除的
        List<Clazz> ClazzList=null;
        List<Course> courseList=null;

        if(id!=null){
            op = gradeRepository.findById(id);
            ClazzList = clazzRepository.findClazzByGradeId(id);
            for(Clazz clazz:ClazzList){
                clazzController.clazzAllDelete(clazz.getId());
            }
            courseList = courseRepository.findCourseListByGradeId(id);
            for(Course course:courseList){
                courseRepository.delete(course);
            }
            if(op.isPresent()){
                g = op.get();
            }
        }

        if(g!=null){
            gradeRepository.delete(g);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public void gradeAllDelete(Integer id){
        Grade g = null;
        Optional<Grade> op;

        //需要附带删除的
        List<Clazz> ClazzList=null;
        List<Course> courseList=null;

        if(id!=null){
            op = gradeRepository.findById(id);
            ClazzList = clazzRepository.findClazzByGradeId(id);
            for(Clazz clazz:ClazzList){
                clazzController.clazzAllDelete(clazz.getId());
            }
            courseList = courseRepository.findCourseListByGradeId(id);
            for(Course course:courseList){
                courseRepository.delete(course);
            }
            if(op.isPresent()){
                g = op.get();
            }
        }

        if(g!=null){
            gradeRepository.delete(g);
        }
    }

}
