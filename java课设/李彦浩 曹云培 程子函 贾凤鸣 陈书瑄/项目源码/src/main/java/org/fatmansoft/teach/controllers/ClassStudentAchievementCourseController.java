package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.MyCourse;
import org.fatmansoft.teach.models.Student;
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

public class ClassStudentAchievementCourseController {
    private static Integer courseId;
    private static Integer clazzId;
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

    public List getAllCourseAchievementList(Integer courseId,Integer clazzId){
        List dataList = new ArrayList();

        List<Achievement> achievementList = achievementRepository.findAchievementByCourseIdAndClazzId(courseId,clazzId);

        if(achievementList==null||achievementList.size()==0){
            return dataList;
        }

        Collections.sort(achievementList);
        Double sum = 0.0;
        Integer count=0;
        for(Achievement A:achievementList){
            if(A.getScore()!=null){
                sum+=A.getScore();
                count++;
            }
        }

        Achievement A;
        Map m;
        Double average=((double)((int)((sum/count)*100)))/100; //硬核保留两位小数,平均分
        Integer rank = 1; //排名
        for(int i=0;i<achievementList.size();i++){
            A = achievementList.get(i);
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
            m.put("average",average);
            String classStudentAchievementParas = "model=classStudentAchievement&clazzId="+clazzId;
            m.put("classStudentAchievement","返回");
            m.put("classStudentAchievementParas",classStudentAchievementParas);
            //还没到最后一个
            if(i!=achievementList.size()-1){
                if(A.getScore()>achievementList.get(i+1).getScore()){
                    m.put("rank",rank);
                    rank++;
                }
                else{
                    m.put("rank",rank);
                }
            }
            //最后一个
            else{
                m.put("rank",rank);
            }
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentAchievementCourseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentAchievementCourseInit(@Valid @RequestBody DataRequest dataRequest) {
        courseId = dataRequest.getInteger("courseId");
        clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllCourseAchievementList(courseId,clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/classStudentAchievementCourseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentAchievementCourseEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Achievement A=null;
        Optional<Achievement> op;

        if(id!=null){
            op = achievementRepository.findById(id);
            if (op.isPresent()){
                A = op.get();
            }
        }

        Map form = new HashMap();
        if(A!=null){
            form.put("id",A.getId());
            //form.put("majorNum",A.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            //form.put("gradeNum",A.getStudent().getClazz().getGrade().getGradeNum());
            //form.put("classNum",A.getStudent().getClazz().getClassNum());
            form.put("studentNum",A.getStudent().getStudentNum());
            //form.put("courseNum",A.getCourse().getCourseNum());
            form.put("score",A.getScore());
        }

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/classStudentAchievementCourseEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentAchievementCourseEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        //String majorNum = CommonMethod.getString(form,"majorNum");
        //Integer gradeNum = CommonMethod.getInteger(form,"gradeNum");
        //Integer classNum = CommonMethod.getInteger(form,"classNum");
        String studentNum = CommonMethod.getString(form,"studentNum");
        //String courseNum = CommonMethod.getString(form,"courseNum");
        Double score = CommonMethod.getDouble(form,"score");

        Achievement A=null;
        Optional<Achievement> op;

        if(id!=null){
            op = achievementRepository.findById(id);
            if(op.isPresent()){
                A= op.get();
            }
        }

        if(A==null){
            A = new Achievement();
            id=achievementRepository.getMaxId();
            if(id==null){
                id=1;
            }
            else{
                id=id+1;
            }
            A.setId(id);
        }

        //Optional<Student> s = studentRepository.OPFindStudentByMajorGradeClazzStudentNum(majorNum,gradeNum,classNum,studentNum);
        Optional<Course> c= courseRepository.findById(courseId);
        Optional<Student> s = studentRepository.OPFindStudentByClazzIdAndStudentNum(clazzId,studentNum);

        if(s.isPresent()&&c.isPresent()){
            //判重 一个学生的某一门课不能提交两条记录
            Optional<Achievement> achievementCheck = achievementRepository.OPFindAchievementByStudentIdAndCourseId(s.get().getId(),courseId);
            achievementCheck.ifPresent(achievement -> achievementRepository.delete(achievement));
            //先选了课才能填成绩(jfm问题)
            Optional<MyCourse> mc =myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(s.get().getId(),courseId);
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
        }

        achievementRepository.save(A);  //新建和修改都调用save方法

        return CommonMethod.getReturnData(A.getId());  // 将记录的id返回前端
    }

    @PostMapping("/classStudentAchievementCourseDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentAchievementCourseDelete(@Valid @RequestBody DataRequest dataRequest){
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
