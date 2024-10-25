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
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class CourseAchievementController {
    private static Integer courseId;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private MyCourseRepository myCourseRepository;

    //上个页面回传的课程号
    public List getCourseAchievementMapListByCourseId(Integer courseId){
        List dataList = new ArrayList();

        List<Achievement> achievementList = achievementRepository.FindScoreByCourseIdNative(courseId);

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
            String courseParas = "model=course&gradeId="+A.getStudent().getClazz().getGrade().getId();
            m.put("course","返回");
            m.put("courseParas",courseParas);
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

    @PostMapping("/courseAchievementInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseAchievementInit(@Valid @RequestBody DataRequest dataRequest){
        courseId = dataRequest.getInteger("courseId");
        List dataList = getCourseAchievementMapListByCourseId(courseId);
        return CommonMethod.getReturnData(dataList);
    }

    /*@PostMapping("/courseAchievementQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseAchievementQuery(@Valid @RequestBody DataRequest dataRequest){
        String demand = dataRequest.getString("demand");

    }*/


    @PostMapping("/courseAchievementEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseAchievementEditInit(@Valid @RequestBody DataRequest dataRequest){
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
            form.put("classNum",A.getStudent().getClazz().getClassNum());
            form.put("studentNum",A.getStudent().getStudentNum());
            //form.put("courseNum",A.getCourse().getCourseNum());
            form.put("score",A.getScore());
        }

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/courseAchievementEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseAchievementEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        //String majorNum = CommonMethod.getString(form,"majorNum");
        //Integer gradeNum = CommonMethod.getInteger(form,"gradeNum");
        Integer classNum = CommonMethod.getInteger(form,"classNum");
        String studentNum = CommonMethod.getString(form,"studentNum");
        String courseNum = CommonMethod.getString(form,"courseNum");
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


        //在Student和Course中分别加了返回Optional的根据号码和名称查询的方法
        //如果仍然要用findById则会出现No value present错误
        //Optional<Student> s = studentRepository.OPFindStudentByMajorGradeClazzStudentNum(majorNum,gradeNum,classNum,studentNum);
        Optional<Course> c= courseRepository.findById(courseId);
        Integer majorId=null,gradeId=null;
        if(c.isPresent()){
            majorId =  c.get().getGrade().getMajor().getId();
            gradeId = c.get().getGrade().getId();
        }
        Optional<Student> s = studentRepository.OPFindStudentByMajorAndGradeIdAndClazzAndStudentNum(majorId,gradeId,classNum,studentNum);

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

    @PostMapping("/courseAchievementDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseAchievementDelete(@Valid @RequestBody DataRequest dataRequest){
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
