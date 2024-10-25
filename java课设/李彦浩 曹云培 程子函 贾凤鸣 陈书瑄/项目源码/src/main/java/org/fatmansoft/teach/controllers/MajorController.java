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
public class MajorController {
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
    private GradeController gradeController;

    public List getMajorMapListByNumName(String numName){
        List dataList = new ArrayList();
        List<Major> mjList = majorRepository.findMajorByNumNameNative(numName);

        if(mjList == null || mjList.size() == 0){
            return dataList;
        }

        Collections.sort(mjList);

        Major mj;
        Map m;
        String majorNameParas;
        for(int i = 0; i < mjList.size();i++) {
            mj = mjList.get(i);
            m = new HashMap();
            m.put("id", mj.getId());
            m.put("majorNum",mj.getMajorNum());
            majorNameParas = "model=grade&majorId=" + mj.getId();
            m.put("majorName",mj.getMajorName());
            m.put("majorNameParas",majorNameParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/majorInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse majorInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getMajorMapListByNumName("");
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/majorQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse majorQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = getMajorMapListByNumName(numName);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/majorEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse majorEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Major mj= null;
        Optional<Major> op;
        if(id != null) {
            op= majorRepository.findById(id);
            if(op.isPresent()) {
                mj = op.get();
            }
        }
        Map form = new HashMap();
        if(mj != null) {
            form.put("id",mj.getId());
            form.put("majorNum",mj.getMajorNum());
            form.put("majorName",mj.getMajorName());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/majorEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse majorEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String majorNum = CommonMethod.getString(form,"majorNum");
        String majorName = CommonMethod.getString(form,"majorName");

        Major mj= null;
        Optional<Major> op;

        Optional<Major> NumCheck = majorRepository.OPFindMajorByMajorNumNative(majorNum);
        Optional<Major> NameCheck = majorRepository.OPFindMajorByMajorNameNative(majorName);

        if(NumCheck.isPresent()||NameCheck.isPresent()){
            return CommonMethod.getReturnMessageError("提交失败，已经存在该专业");
        }

        if(id != null) {
            op= majorRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                mj = op.get();
            }
        }

        if(mj == null) {
            mj = new Major();   //不存在 创建实体对象
            id = majorRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            mj.setId(id);  //设置新的id
        }

        mj.setMajorNum(majorNum);  //设置属性
        mj.setMajorName(majorName);
        majorRepository.save(mj);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(mj.getId());  // 将记录的id返回前端
    }

    //major下面有grade grade下面有clazz clazz下面有student student下有myCourse和achievement
    /*@PostMapping("/majorDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse majorDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Major mj = null;
        Optional<Major> op;

        //需要附带删除的
        List<Grade> GradeList=null;
        List<Clazz> ClazzList=null;
        List<Student> StudentList=null;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;
        List<Course> CourseList=null;

        if(id !=null){
            op = majorRepository.findById(id);
            GradeList = gradeRepository.findGradeByMajorId(id); //找出所有年级并删除
            ClazzList = clazzRepository.findClazzByMajorId(id); //找出所有班级并删除
            StudentList = studentRepository.findStudentByMajorId(id); //找出所有学生并删除
            AchievementList= achievementRepository.findAchievementByMajorId(id); //找出所有成绩并删除
            MyCourseList = myCourseRepository.findMyCourseByMajorId(id); //找出所有我的课程并删除
            CourseList = courseRepository.findCourseByMajorId(id);
            if(op.isPresent()){
                mj = op.get();
            }
        }
        if(mj != null){
            if(CourseList!=null&&CourseList.size()!=0){
                for(Course course:CourseList){
                    courseRepository.delete(course);
                }
            }
            if(GradeList!=null&&GradeList.size()!=0){
                for(Grade grade:GradeList){
                    gradeRepository.delete(grade);
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
            majorRepository.delete(mj);
        }
        return CommonMethod.getReturnMessageOK();
    }*/

    @PostMapping("/majorDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse majorDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Major mj = null;
        Optional<Major> op;

        //需要附带删除的
        List<Grade> GradeList=null;

        if(id !=null) {
            op = majorRepository.findById(id);
            GradeList = gradeRepository.findGradeByMajorId(id);
            for(Grade grade:GradeList){
                gradeController.gradeAllDelete(grade.getId());
            }
            if(op.isPresent()){
                mj = op.get();
            }
        }
        if(mj != null){
            majorRepository.delete(mj);
        }
        return CommonMethod.getReturnMessageOK();
    }
}
