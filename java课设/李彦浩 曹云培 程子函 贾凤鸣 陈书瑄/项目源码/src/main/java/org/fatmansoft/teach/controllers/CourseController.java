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

public class CourseController {
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
    private ResourceRepository resourceRepository;
    @Autowired
    private MyAttendanceRepository myAttendanceRepository;

    public List getCourseMapList(Integer gradeId){
        List dataList = new ArrayList();

        List<Course> CList = courseRepository.findCourseListByGradeId(gradeId);

        //空的直接返回
        if(CList==null||CList.size()==0){
            return dataList;
        }

        Collections.sort(CList);

        //每一门课都要查询对应的MyCourse对象
        for(int j=0;j<CList.size();j++) {
            //选某一门课的人
            Integer courseId = CList.get(j).getId(); //获取课程Id
            //根据课程Id查询这门课的所有有关对象
            List<MyCourse> numList = myCourseRepository.findMyCourseByCourseIdNative(courseId);
            Integer num = numList.size(); //选课人数
            Course C = CList.get(j);
            String courseAchievementParas = "model=courseAchievement&courseId="+C.getId();
            String gradeParas = "model=grade&majorId="+C.getGrade().getMajor().getId();
            String resourceParas = "model=resource&courseId="+C.getId();

            Map m = new HashMap();
            m.put("id", C.getId());
            m.put("majorNum",C.getGrade().getMajor().getMajorNum());
            m.put("majorName",C.getGrade().getMajor().getMajorName());
            m.put("gradeNum",C.getGrade().getGradeNum());
            m.put("courseNum", C.getCourseNum());
            m.put("courseName", C.getCourseName());
            m.put("credit",C.getCredit());
            m.put("studentNumber", num);
            m.put("courseAchievement","课程成绩");
            m.put("courseAchievementParas",courseAchievementParas);
            m.put("resource","课程资源");
            m.put("resourceParas",resourceParas);
            m.put("grade","返回");
            m.put("gradeParas",gradeParas);
            dataList.add(m);
        }
        return dataList;
    }

    public List getCourseMapList(String numName){
        List dataList = new ArrayList();

        List<Course> CList = courseRepository.findCourseListByNumNameNative(numName);

        //空的直接返回
        if(CList==null||CList.size()==0){
            return dataList;
        }

        Collections.sort(CList);

        //每一门课都要查询对应的MyCourse对象
        for(int j=0;j<CList.size();j++) {
            //选某一门课的人
            Integer courseId = CList.get(j).getId(); //获取课程Id
            //根据课程Id查询这门课的所有有关对象
            List<MyCourse> numList = myCourseRepository.findMyCourseByCourseIdNative(courseId);

            Integer num = numList.size(); //选课人数
            Course C = CList.get(j);
            String courseAchievementParas = "model=courseAchievement&courseId="+C.getId();
            String gradeParas = "model=grade&majorId="+C.getGrade().getMajor().getId();
            String resourceParas = "model=resource&courseId="+C.getId();

            Map m = new HashMap();
            m.put("id", C.getId());
            m.put("majorNum",C.getGrade().getMajor().getMajorNum());
            m.put("majorName",C.getGrade().getMajor().getMajorName());
            m.put("gradeNum",C.getGrade().getGradeNum());
            m.put("courseNum", C.getCourseNum());
            m.put("courseName", C.getCourseName());
            m.put("credit",C.getCredit());
            m.put("studentNumber", num);
            m.put("courseAchievement","课程成绩");
            m.put("courseAchievementParas",courseAchievementParas);
            m.put("resource","课程资源");
            m.put("resourceParas",resourceParas);
            m.put("grade","返回");
            m.put("gradeParas",gradeParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/courseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseInit(@Valid @RequestBody DataRequest dataRequest){
        gradeId = dataRequest.getInteger("gradeId");
        List dataList = getCourseMapList(gradeId);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/courseQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = new ArrayList();
        dataList = getCourseMapList(numName);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/courseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEdit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Course C = null;

        Optional<Course> op;

        if(id != null){
            op = courseRepository.findById(id);
            if(op.isPresent()){
                C = op.get();
            }
        }
        Map form = new HashMap();
        if(C!=null){
            form.put("id",C.getId());
            //form.put("majorNum",C.getGrade().getMajor().getMajorNum());
            //form.put("gradeNum",C.getGrade().getGradeNum());
            form.put("courseNum",C.getCourseNum());
            form.put("courseName",C.getCourseName());
            form.put("credit",C.getCredit());
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/courseDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Course C = null;
        Optional<Course> op;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;
        List<Resource> ResourceList=null;
        List<MyAttendance> myAttendanceList=null;
        //刪除了這門課程對應的學生選課信息
        if(id !=null){
            op = courseRepository.findById(id);
            AchievementList= achievementRepository.FindScoreByStudentIdNative(id);
            MyCourseList = myCourseRepository.findMyCourseByCourseIdNative(id);
            ResourceList = resourceRepository.FindResourceByCourseIdNative(id);
            myAttendanceList = myAttendanceRepository.findMyAttendanceByCourseId(id);
            if(op.isPresent()){
                C = op.get();
            }
        }
        if(C != null){
            if(AchievementList!=null||AchievementList.size()!=0){//改掉了&&為||
                for(Achievement A:AchievementList){
                    achievementRepository.delete(A);
                }
            }
            if(MyCourseList!=null||MyCourseList.size()!=0){
                for(MyCourse mc:MyCourseList){
                    myCourseRepository.delete(mc);
                }
            }
            if(ResourceList!=null||ResourceList.size()!=0){
                for(Resource R:ResourceList){
                    resourceRepository.delete(R);
                }
            }
            if(myAttendanceList!=null||myAttendanceList.size()!=0){
                for(MyAttendance myAttendance:myAttendanceList){
                    myAttendanceRepository.delete(myAttendance);
                }
            }
            courseRepository.delete(C);
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/courseEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    //传递一个DRequest对象进来，属性里是一个Map集
    public DataResponse courseEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        String CourseNum = CommonMethod.getString(form,"courseNum");  //Map 获取属性的值
        String CourseName = CommonMethod.getString(form,"courseName");
        Integer credit = CommonMethod.getInteger(form,"credit");

        Course C= null;
        Optional<Course> op;
        Optional<Course> CourseCheck;

        //CourseCheck = courseRepository.OPFindCourseByGradeIdAndCourseNum(gradeId, CourseNum);

        if(id != null) {
            op= courseRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                C = op.get();
            }
        }

        if(C == null) {
            CourseCheck = courseRepository.OPFindCourseByGradeIdAndCourseNum(gradeId, CourseNum);
            if(CourseCheck.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，已经存在该课程");
            }
            C = new Course();   //不存在 创建实体对象
            id = courseRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            C.setId(id);  //设置新的id
        }

        //return CommonMethod.getReturnMessageError("提交失败，已经存在该课程");
        //CourseCheck.ifPresent(course -> courseRepository.delete(course));

        Optional<Grade> tempGrade = gradeRepository.findById(gradeId);

        C.setGrade(tempGrade.get());
        C.setCourseNum(CourseNum);  //设置属性
        C.setCourseName(CourseName);
        C.setCredit(credit);
        courseRepository.save(C);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(C.getId());  // 将记录的id返回前端
    }
}
