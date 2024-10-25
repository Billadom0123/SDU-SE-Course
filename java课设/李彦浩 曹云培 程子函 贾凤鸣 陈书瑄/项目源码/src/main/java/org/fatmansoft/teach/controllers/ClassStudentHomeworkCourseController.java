package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Homework;
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
public class ClassStudentHomeworkCourseController {
        private static Integer courseId;
        private static Integer clazzId;
        @Autowired
        private StudentRepository studentRepository;
        @Autowired
        private CourseRepository courseRepository;
        @Autowired
        private MyCourseRepository myCourseRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
        @Autowired
        private MajorRepository majorRepository;
        @Autowired
        private GradeRepository gradeRepository;
        @Autowired
        private ClazzRepository clazzRepository;

        public List getAllCourseHomeworkList(Integer courseId, Integer clazzId) {
            List dataList = new ArrayList();

            List<Homework> homeworkList = homeworkRepository.findHomeworkByCourseIdAndClazzId(courseId, clazzId);

            if (homeworkList == null || homeworkList.size() == 0) {
                return dataList;
            }

            //Collections.sort(homeworkList);

            Homework A;
            Map m;//排名
            for (int i = 0; i < homeworkList.size(); i++) {
                A = homeworkList.get(i);
                m = new HashMap();
                m.put("id", A.getId());
                m.put("majorNum", A.getStudent().getClazz().getGrade().getMajor().getMajorNum());
                m.put("majorName", A.getStudent().getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum", A.getStudent().getClazz().getGrade().getGradeNum());
                m.put("classNum", A.getStudent().getClazz().getClassNum());
                m.put("studentNum", A.getStudent().getStudentNum());
                m.put("studentName", A.getStudent().getStudentName());
                System.out.println(A.getStudent().getStudentName());
                m.put("courseNum", A.getCourse().getCourseNum());
                m.put("courseName", A.getCourse().getCourseName());
                m.put("homeworkScore", A.getHomeworkScore());
                String classStudentHomeworkParas = "model=classStudentHomework&clazzId=" + clazzId;
                m.put("classStudentHomework", "返回");
                m.put("classStudentHomeworkParas", classStudentHomeworkParas);
                //还没到最后一个
                String S = A.judegecondition();
                m.put("submitcondition",S);
                dataList.add(m);
            }
            return dataList;
        }

        @PostMapping("/classStudentHomeworkCourseInit")
        @PreAuthorize("hasRole('ADMIN')")
        public DataResponse classStudentHomeworkCourseInit(@Valid @RequestBody DataRequest dataRequest) {
            courseId = dataRequest.getInteger("courseId");
            clazzId = dataRequest.getInteger("clazzId");
            List dataList = getAllCourseHomeworkList(courseId,clazzId);
            return CommonMethod.getReturnData(dataList);
        }

        @PostMapping("/classStudentHomeworkCourseEditInit")
        @PreAuthorize("hasRole('ADMIN')")
        public DataResponse classStudentHomeworkCourseEditInit(@Valid @RequestBody DataRequest dataRequest){
            Integer id = dataRequest.getInteger("id");
            Homework A=null;
            Optional<Homework> op;

            if(id!=null){
                op = homeworkRepository.findById(id);
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
                form.put("score",A.getHomeworkScore());
            }

            return CommonMethod.getReturnData(form);
        }

        @PostMapping("/classStudentHomeworkCourseEditSubmit")
        @PreAuthorize("hasRole('ADMIN')")
        public DataResponse classStudentHomeworkCourseEditSubmit(@Valid @RequestBody DataRequest dataRequest){
            Map form = dataRequest.getMap("form");
            Integer id = CommonMethod.getInteger(form,"id");
            //String majorNum = CommonMethod.getString(form,"majorNum");
            //Integer gradeNum = CommonMethod.getInteger(form,"gradeNum");
            //Integer classNum = CommonMethod.getInteger(form,"classNum");
            String studentNum = CommonMethod.getString(form,"studentNum");
            //String courseNum = CommonMethod.getString(form,"courseNum");
            Double homeworkScore = CommonMethod.getDouble(form,"homeworkScore");

            Homework A=null;
            Optional<Homework> op;

            if(id!=null){
                op = homeworkRepository.findById(id);
                if(op.isPresent()){
                    A= op.get();
                }
            }

            if(A==null){
                A = new Homework();
                id=homeworkRepository.getMaxId();
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
                Optional<Homework> homeworkCheck = homeworkRepository.OPFindHomeworkByStudentIdAndCourseId(s.get().getId(),courseId);
                homeworkCheck.ifPresent(homework -> homeworkRepository.delete(homework));
                //先选了课才能填成绩(jfm问题)
                Optional<MyCourse> mc =myCourseRepository.OPFindMyCourseByStudentIdAndCourseId(s.get().getId(),courseId);
                if(mc.isPresent()){
                    A.setStudent(s.get());
                    //System.out.println(s.get().getStudentName()+" from courseAchievementEditSubmit");
                    A.setCourse(c.get());
                    A.setHomeworkScore(homeworkScore);
                }
                else{
                    return CommonMethod.getReturnMessageError("提交失败，该学生未选择这门课或是不存在该学生");
                }
            }
            else{
                return  CommonMethod.getReturnMessageError("提交失败，不存在该学生或是不存在该课程");
            }

            homeworkRepository.save(A);  //新建和修改都调用save方法

            return CommonMethod.getReturnData(A.getId());  // 将记录的id返回前端
        }

        @PostMapping("/classStudentHomeworkCourseDelete")
        @PreAuthorize("hasRole('ADMIN')")
        public DataResponse classStudentAchievementCourseDelete(@Valid @RequestBody DataRequest dataRequest){
            Integer id = dataRequest.getInteger("id");
            Homework A = null;
            Optional<Homework> op;

            if(id !=null){
                op = homeworkRepository.findById(id);
                if(op.isPresent()){
                    A = op.get();
                }
            }

            if(A != null){
                homeworkRepository.delete(A);
            }

            return CommonMethod.getReturnMessageOK();
        }
    }


