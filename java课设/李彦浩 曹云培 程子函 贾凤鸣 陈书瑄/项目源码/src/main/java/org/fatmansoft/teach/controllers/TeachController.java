package org.fatmansoft.teach.controllers;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.impl.FSDefaultCacheStore;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.IntroduceService;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class TeachController {
    private static Integer clazzId = 0;
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
    private IntroduceService introduceService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private MyLogRepository myLogRepository;
    @Autowired
    private MyInnovationRepository myInnovationRepository;
    @Autowired
    private MyActivityRepository myActivityRepository;
    @Autowired
    private MyLeaveRepository myLeaveRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private MyAttendanceRepository myAttendanceRepository;

    private FSDefaultCacheStore fSDefaultCacheStore = new FSDefaultCacheStore();

    public List getStudentMapList(Integer clazzId) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByClazzId(clazzId);

        if(sList == null || sList.size() == 0){
            return dataList;
        }

        Collections.sort(sList);

        Student s;
        Map m;
        String studentNameParas,clazzParas,StudentDetailsParas;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            studentNameParas = "model=introduce&studentId=" + s.getId();
            m.put("studentName",s.getStudentName());
            m.put("studentNameParas",studentNameParas);
            if("1".equals(s.getSex())) {
                m.put("sex","男");
            }else {
                m.put("sex","女");
            }
            m.put("age",s.getAge());
            m.put("majorNum",s.getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",s.getClazz().getGrade().getMajor().getMajorName());
            m.put("grade",s.getClazz().getGrade().getGradeNum());
            m.put("class",s.getClazz().getClassNum());
            m.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));  //时间格式转换字符串

            StudentDetailsParas = "model=StudentDetails&studentId="+s.getId();
            m.put("StudentDetails","详细信息");
            m.put("StudentDetailsParas",StudentDetailsParas);

            clazzParas = "model=clazz&gradeId="+s.getClazz().getGrade().getId();
            m.put("clazz","返回");
            m.put("clazzParas",clazzParas);
            dataList.add(m);
        }

        return dataList;
    }

    public List getStudentMapList(Integer clazzId,String numName) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentByClazzIdAndNumName(clazzId,numName);

        if(sList == null || sList.size() == 0){
            return dataList;
        }

        Collections.sort(sList);

        Student s;
        Map m;
        String myCourseParas,studentNameParas,myAchievementParas,clazzParas,StudentDetailsParas;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            studentNameParas = "model=introduce&studentId=" + s.getId();
            m.put("studentName",s.getStudentName());
            m.put("studentNameParas",studentNameParas);
            if("1".equals(s.getSex())) {
                m.put("sex","男");
            }else {
                m.put("sex","女");
            }
            m.put("age",s.getAge());
            m.put("majorNum",s.getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",s.getClazz().getGrade().getMajor().getMajorName());
            m.put("grade",s.getClazz().getGrade().getGradeNum());
            m.put("class",s.getClazz().getClassNum());
            m.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));  //时间格式转换字符串
            myCourseParas = "model=myCourse&studentId=" + s.getId();
            m.put("myCourse","我的课程");
            m.put("myCourseParas",myCourseParas);
            myAchievementParas ="model=myAchievement&studentId="+s.getId();
            m.put("myAchievement","我的成绩");
            m.put("myAchievementParas",myAchievementParas);
            StudentDetailsParas = "model=StudentDetails&studentId="+s.getId();
            m.put("StudentDetails","详细信息");
            m.put("StudentDetailsParas",StudentDetailsParas);
            clazzParas = "model=clazz&gradeId="+s.getClazz().getGrade().getId();
            m.put("clazz","返回");
            m.put("clazzParas",clazzParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/studentInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentInit(@Valid @RequestBody DataRequest dataRequest) {
        clazzId = dataRequest.getInteger("clazzId");
        //System.out.println(clazzId);
        List dataList = getStudentMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/studentQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentQuery(@Valid @RequestBody DataRequest dataRequest) {
        //Integer clazzId = dataRequest.getInteger("clazzId");
        String numName= dataRequest.getString("numName");
        List dataList = getStudentMapList(clazzId,numName);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/studentEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s= null;
        Optional<Student> op;
        if(id != null) {
            op= studentRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("studentNum",s.getStudentNum());
            form.put("studentName",s.getStudentName());
            form.put("sex",s.getSex());
            form.put("age",s.getAge());
            //form.put("majorNum",s.getClazz().getGrade().getMajor().getMajorNum());
            //form.put("grade",s.getClazz().getGrade().getGradeNum());
            //form.put("class",s.getClazz().getClassNum());
            form.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));
        }
        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/studentEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    //传递一个DRequest对象进来，属性里是一个Map集
    public DataResponse studentEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");
        String studentName = CommonMethod.getString(form,"studentName");
        String sex = CommonMethod.getString(form,"sex");
        Integer age = CommonMethod.getInteger(form,"age");
        //String majorNum = CommonMethod.getString(form,"majorNum");
        //Integer gradeNum = CommonMethod.getInteger(form,"grade");
        //Integer classNum = CommonMethod.getInteger(form,"class");
        Date birthday = CommonMethod.getDate(form,"birthday");
        Student s= null;
        Optional<Student> op;
        //两个判重 1.编辑：如果已经有目标坐标的学生，则编辑不能成功 2.如果已经有目标添加的学生，则添加不能成功
        Optional<Student> studentCheck;

        //编辑功能，不能编辑当前学生至一个已经存在的学生
        if(id != null) {
            //studentCheck = studentRepository.OPFindStudentByClazzIdAndStudentNum(clazzId,studentNum);
            //if(studentCheck.isPresent()){
                //return CommonMethod.getReturnMessageError("提交失败，已经存在该学生");
            //}
            op= studentRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }

        if(s == null) {
            studentCheck = studentRepository.OPFindStudentByClazzIdAndStudentNum(clazzId,studentNum);
            if(studentCheck.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，已经存在该学生");
            }
            s = new Student();
            id = studentRepository.getMaxId();
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);
        }

        //studentCheck = studentRepository.OPFindStudentByClazzIdAndStudentNum(clazzId,studentNum);
        //studentCheck.ifPresent(student -> studentRepository.delete(student));

        Optional<Clazz> TempClazz = clazzRepository.findById(clazzId);
        if(TempClazz.isPresent()){
            s.setStudentNum(studentNum);
            s.setStudentName(studentName);
            s.setSex(sex);
            s.setAge(age);
            s.setClazz(TempClazz.get());
            s.setBirthday(birthday);
            studentRepository.save(s);
        }
        else{
            //System.out.println(clazzId);
            return CommonMethod.getReturnMessageError("提交失败，不能提交不存在的班级下的学生");
        }

        return CommonMethod.getReturnData(s.getId());
    }

    //新加入學生刪除的時候，這個和下面的delete都要添加，第一個是學生界面的刪除，第二個是對應的class界面的刪除(jfm
    @PostMapping("/studentDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s= null;
        Optional<Student> op;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;
        List<MyLog> logs = null;
        List<MyLeave> leaves = null;
        List<Honor> HonorList=null;
        List<Homework> homeworkList=null;
        List<MyAttendance> myAttendanceList=null;
        List<MyInnovation> myInnovationList = null;
        List<MyActivity> myActivityList=null;
        if(id != null) {
            op= studentRepository.findById(id);
            AchievementList=achievementRepository.FindScoreByStudentIdNative(id);
            MyCourseList= myCourseRepository.findMyCourseByStudentIdNative(id);
            logs=myLogRepository.findMyLogByStudentIdNative(id);
            leaves= myLeaveRepository.findMyLeaveByStudentId(id);
            HonorList= honorRepository.FindHonorByStudentIdNative(id);
            homeworkList = homeworkRepository.findHomeworkByStudentId(id);
            myAttendanceList = myAttendanceRepository.findMyAttendanceByStudentId(id);
            myInnovationList=myInnovationRepository.findMyInnovationByStudentId(id);
            myActivityList=myActivityRepository.findMyActivityByStudentId(id);

            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            //删关联表Achievement的信息
            if(AchievementList!=null||AchievementList.size()!=0){
                for(Achievement A:AchievementList){
                    achievementRepository.delete(A);
                }
            }
            //删关联表MyCourse的信息
            if(MyCourseList!=null||MyCourseList.size()!=0){
                for(MyCourse mc:MyCourseList){
                    myCourseRepository.delete(mc);
                }
            }
            if(logs!=null||logs.size()!=0){
                for(MyLog L:logs){
                    myLogRepository.delete(L);
                }
            }
            if(leaves!=null||leaves.size()!=0){
                for(MyLeave Le:leaves){
                    myLeaveRepository.delete(Le);
                }
            }
            if(HonorList!=null||HonorList.size()!=0){
                for(Honor h:HonorList){
                    honorRepository.delete(h);
                }
            }
            if(homeworkList!=null||homeworkList.size()!=0){
                for(Homework homework:homeworkList){
                    homeworkRepository.delete(homework);
                }
            }
            if(myAttendanceList!=null||myAttendanceList.size()!=0){
                for(MyAttendance myAttendance:myAttendanceList){
                    myAttendanceRepository.delete(myAttendance);
                }
            }
            if(myActivityList!=null||myActivityList.size()!=0){
                for(MyInnovation myInnovation:myInnovationList){
                    myInnovationRepository.delete(myInnovation);
                }
            }
            if(myActivityList!=null||myActivityList.size()!=0){
                for(MyActivity myActivity:myActivityList){
                    myActivityRepository.delete(myActivity);
                }
            }
            studentRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    //jfm提供的套娃删除方法//兩個刪除方法都要添加對應的刪除！！！
    public void studentAllDelete(Integer id) {
        //   Integer id = dataRequest.getInteger("id");
        System.out.println("此方法被调用");
        Student s= null;
        Optional<Student> op;
        List<Achievement> AchievementList=null;
        List<MyCourse> MyCourseList=null;
        List<MyLog> logs = null;
        List<MyLeave> leaves = null;
        List<Honor> HonorList=null;
        List<Homework> homeworkList=null;
        List<MyAttendance> myAttendanceList=null;
        List<MyActivity> myActivityList = null;
        List<MyInnovation> myInnovationList = null;
        if(id != null) {
            op= studentRepository.findById(id);
            AchievementList=achievementRepository.FindScoreByStudentIdNative(id);
            MyCourseList= myCourseRepository.findMyCourseByStudentIdNative(id);
            logs=myLogRepository.findMyLogByStudentIdNative(id);
            leaves= myLeaveRepository.findMyLeaveByStudentId(id);
            HonorList= honorRepository.FindHonorByStudentIdNative(id);
            homeworkList = homeworkRepository.findHomeworkByStudentId(id);
            myAttendanceList = myAttendanceRepository.findMyAttendanceByStudentId(id);
            myActivityList = myActivityRepository.findMyActivityByStudentId(id);
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            //删关联表Achievement的信息
            if(AchievementList!=null||AchievementList.size()!=0){
                for(Achievement A:AchievementList){
                    achievementRepository.delete(A);
                }
            }
            //删关联表MyCourse的信息
            if(MyCourseList!=null||MyCourseList.size()!=0){
                for(MyCourse mc:MyCourseList){
                    myCourseRepository.delete(mc);
                }
            }
            if(logs!=null||logs.size()!=0){
                for(MyLog L:logs){
                    myLogRepository.delete(L);
                }
            }
            if(leaves!=null||leaves.size()!=0){
                for(MyLeave Le:leaves){
                    myLeaveRepository.delete(Le);
                }
            }
            if(HonorList!=null||HonorList.size()!=0){
                for(Honor h:HonorList){
                    honorRepository.delete(h);
                }
            }
            if(homeworkList!=null||homeworkList.size()!=0){
                for(Homework homework:homeworkList){
                    homeworkRepository.delete(homework);
                }
            }
            if(myAttendanceList!=null||myAttendanceList.size()!=0){
                for(MyAttendance myAttendance:myAttendanceList){
                    myAttendanceRepository.delete(myAttendance);
                }
            }
            if(myActivityList!=null||myActivityList.size()!=0){
                for(MyActivity myActivity:myActivityList){
                    myActivityRepository.delete(myActivity);
                }
            }
            if(myInnovationList!=null||myActivityList.size()!=0){
                for(MyInnovation myInnovation:myInnovationList){
                    myInnovationRepository.delete(myInnovation);
                }
            }
            studentRepository.delete(s);
            System.out.println(s.getStudentName());
        }
        //  return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getStudentIntroduceData")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getStudentIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Map data = introduceService.getIntroduceDataMap(studentId);
        return CommonMethod.getReturnData(data);
    }

    public ResponseEntity<StreamingResponseBody> getPdfDataFromHtml(String htmlContent) {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.useFastMode();
            builder.useCacheStore(PdfRendererBuilder.CacheStore.PDF_FONT_METRICS, fSDefaultCacheStore);
            Resource resource = resourceLoader.getResource("classpath:font/SourceHanSansSC-Regular.ttf");
            InputStream fontInput = resource.getInputStream();
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    return fontInput;
                }
            }, "SourceHanSansSC");
            StreamingResponseBody stream = outputStream -> {
                builder.toStream(outputStream);
                builder.run();
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(stream);

        }
        catch (Exception e) {
            return  ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/getStudentIntroducePdf")
    public ResponseEntity<StreamingResponseBody> getStudentIntroducePdf(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Map data = introduceService.getIntroduceDataMap(studentId);
        String content= "<!DOCTYPE html>";
        content += "<html>";
        content += "<head>";
        content += "<style>";
        content += "html { font-family: \"SourceHanSansSC\", \"Open Sans\";}";
        content += "</style>";
        content += "<meta charset='UTF-8' />";
        content += "<title>Insert title here</title>";
        content += "</head>";

        String myName = (String) data.get("myName");
        String overview = (String) data.get("overview");
        List<Map> attachList = (List) data.get("attachList");

//        content += getHtmlString();
        content += "<body>";

        content += "<table style='width: 100%;'>";
        content += "   <thead >";
        content += "     <tr style='text-align: center;font-size: 32px;font-weight:bold;'>";
        content += "        "+myName+" </tr>";
        content += "   </thead>";
        content += "   </table>";

        content += "<table style='width: 100%;'>";
        content += "   <thead >";
        content += "     <tr style='text-align: center;font-size: 32px;font-weight:bold;'>";
        content += "        "+overview+" </tr>";
        content += "   </thead>";
        content += "   </table>";

        content += "<table style='width: 100%;border-collapse: collapse;border: 1px solid black;'>";
        content +=   " <tbody>";

        for(int i = 0; i <attachList.size(); i++ ){
            content += "     <tr style='text-align: center;border: 1px solid black;font-size: 14px;'>";
            content += "      "+attachList.get(i).get("title")+" ";
            content += "     </tr>";
            content += "     <tr style='text-align: center;border: 1px solid black; font-size: 14px;'>";
            content += "            "+attachList.get(i).get("content")+" ";
            content += "     </tr>";
        }
        content +=   " </tbody>";
        content += "   </table>";

        content += "</body>";
        content += "</html>";
        return getPdfDataFromHtml(content);
    }

}
