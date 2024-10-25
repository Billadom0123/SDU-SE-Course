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
public class ClazzController {
    private static Integer gradeId;
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
    private TeachController teachController;

    @PostMapping("/clazzInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzInit(@Valid @RequestBody DataRequest dataRequest) {
        gradeId = dataRequest.getInteger("gradeId");
        List dataList = new ArrayList();
        List<Clazz> ClazzList = clazzRepository.findClazzByGradeId(gradeId);

        if(ClazzList==null||ClazzList.size()==0){
            return CommonMethod.getReturnData(dataList);
        }

        Collections.sort(ClazzList);

        Clazz clazz;
        Map m;
        String classNumParas,gradeParas,clazzStatisticParas;
        for(int i=0;i<ClazzList.size();i++){
            m = new HashMap();
            clazz = ClazzList.get(i);
            classNumParas="model=student&clazzId="+clazz.getId();
            gradeParas="model=grade&majorId="+clazz.getGrade().getMajor().getId();
            clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.getId();
            m.put("id",clazz.getId());
            m.put("majorNum",clazz.getGrade().getMajor().getMajorNum());
            m.put("majorName",clazz.getGrade().getMajor().getMajorName());
            m.put("gradeNum",clazz.getGrade().getGradeNum());
            m.put("classNum",clazz.getClassNum());
            m.put("classNumParas",classNumParas);
            m.put("clazzStatistic","班级信息统计");
            m.put("clazzStatisticParas",clazzStatisticParas);
            m.put("grade","返回");
            m.put("gradeParas",gradeParas);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    //总共也没几个班，再排序一下找起来很方便,query就跳过不写了
    @PostMapping("/clazzEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Clazz clazz= null;
        Optional<Clazz> op;
        if(id != null) {
            op= clazzRepository.findById(id);
            if(op.isPresent()) {
                clazz = op.get();
            }
        }
        Map form = new HashMap();
        if(clazz != null) {
            form.put("id",clazz.getId());
            //form.put("majorNum",clazz.getGrade().getMajor().getMajorNum());
            //form.put("gradeNum",clazz.getGrade().getGradeNum());
            form.put("classNum",clazz.getClassNum());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/clazzEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse clazzEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        //String majorNum = CommonMethod.getString(form,"majorNum");
        //Integer gradeNum = CommonMethod.getInteger(form,"gradeNum");
        Integer classNum = CommonMethod.getInteger(form,"classNum");

        Clazz clazz= null;
        Optional<Clazz> op;
        //Optional<Grade> gradeCheck;
        Optional<Clazz> clazzCheck;

        //gradeCheck = gradeRepository.
        clazzCheck = clazzRepository.OPFindClazzByGradeIdAndClazzNum(gradeId,classNum);

        if(clazzCheck.isPresent()){
            return CommonMethod.getReturnMessageError("提交失败，已经存在该专业下的该年级下的该班级");
        }

        if(id != null) {
            op= clazzRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                clazz = op.get();
            }
        }

        if(clazz == null) {
            clazz = new Clazz();   //不存在 创建实体对象
            id = clazzRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            clazz.setId(id);//设置新的id
            //System.out.println(id+" from clazzEditSubmit");
        }

        Optional<Grade> g = gradeRepository.findById(gradeId);

        if(g.isPresent()){
            clazz.setGrade(g.get());  //设置属性
            clazz.setClassNum(classNum);
            clazzRepository.save(clazz);  //新建和修改都调用save方法
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不能提交不存在的年级下的班级");
        }

        return CommonMethod.getReturnData(clazz.getId());  // 将记录的id返回前端
    }

    //clazz下有student student下有achievement和myCourse
 /*   @PostMapping("/clazzDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Clazz clazz = null;
        Optional<Clazz> op;


        //需要附带删除的
        List<Student> StudentList=null;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;

        if(id !=null){
            op = clazzRepository.findById(id);
            StudentList = studentRepository.findStudentByGradeId(id); //找出所有学生并删除
            AchievementList= achievementRepository.findAchievementByGradeId(id); //找出所有成绩并删除
            MyCourseList = myCourseRepository.findMyCourseByGradeId(id); //找出所有我的课程并删除
            if(op.isPresent()){
                clazz = op.get();
            }
        }
        if(clazz != null){
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
            clazzRepository.delete(clazz);
        }
        return CommonMethod.getReturnMessageOK();
    }*/
    //新的班级删除
    @PostMapping("/clazzDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");//clazzId

        Clazz clazz = null;
        Optional<Clazz> op;

        //需要附带删除的
        List<Student> StudentList=null;

        if(id !=null){
            op = clazzRepository.findById(id);
            StudentList = studentRepository.findStudentListByClazzId(id); //找出所有学生并删除
            for(Student student:StudentList){
                teachController.studentAllDelete(student.getId());

            }
            if(op.isPresent()){
                clazz = op.get();
            }
        }
        if(clazz != null){
            clazzRepository.delete(clazz);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public void clazzAllDelete(Integer id){
        Clazz clazz = null;
        Optional<Clazz> op;

        //需要附带删除的
        List<Student> StudentList=null;

        if(id !=null){
            op = clazzRepository.findById(id);
            StudentList = studentRepository.findStudentByGradeId(id); //找出所有学生并删除
            for(Student student:StudentList){
                teachController.studentAllDelete(student.getId());
            }
            if(op.isPresent()){
                clazz = op.get();
            }
        }
        if(clazz != null){
            clazzRepository.delete(clazz);
        }
    }
}
