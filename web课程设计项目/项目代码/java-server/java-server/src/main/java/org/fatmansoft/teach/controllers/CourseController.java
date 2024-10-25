package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Value("${attach.folder}")
    private String attachFolder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StuCourseRepository studentCourseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private OpenRepository openRepository;

    public Teacher getCurrentTeacher(){
        return teacherRepository.findTeacherByUserId(getCurrentUser());
    }

    //author:lsh 数字转字符串上课时间
    public    String  GetCourseTime(String origin){
        String[]s=origin.split(" ");
        int k;
        k=s.length;
        String[]number={"一","二","三","四","五","六","日"};
        String result="";
        for(int i=0;i<k;i++){
            result+="周";
            result+=number[Integer.parseInt(s[i])/5];
            result+="第";
            result+=number[Integer.parseInt(s[i])%5];
            result+="节 ";
        }
        return result;
    }

    //检查当前学生用户是否选择了某一门课
    public boolean whetherSelected(Integer courseId){
        Optional<StuCourse> studentCourse = studentCourseRepository.findStudentCourse(getCurrentStudent().getId(), courseId);
        return studentCourse.isPresent();
    }


    //找到当前用户
    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    //找到当前学生已经选择的课程
    public List<Course> getCourseByStudentId(){
        List<Course> result = new ArrayList<>();
        List<StuCourse> courseByStudentId = studentCourseRepository.findCourseByStudentId(getCurrentStudent().getId());
        for (StuCourse stuCourse : courseByStudentId) {
            result.add(courseRepository.findCourseByCourseId(stuCourse.getCourse().getId()).get());
        }
        return result;
    }

    //根据当前课程找到对应老师名字
    public String getTeacherNameByCourse(Course course){
        //找到老师是哪个人
        Person person = course.getTeacher().getPerson();
        return person.getPerName();
    }

    //模糊查询 获取可能是所查老师得id
    public List<Integer> getPossibleTeacherId(String possibleName){
        List<Integer> dataList = new ArrayList<>();
        //teacher表里没有名字 关联到person表了，先在person表中模糊查询
        List<Person> teacherByName = personRepository.findTeacherByName(possibleName);
        for (Person person : teacherByName) {
            //找到personId,去teacher表里把teacher的id查出来
            Integer personId = person.getId();
            Optional<Teacher> teacherByPersonId = teacherRepository.findTeacherByPersonId(personId);
            Integer teacherId = teacherByPersonId.get().getId();
            dataList.add(teacherId);
        }
        return dataList;
    }

    //根据可能教师的id查询所有可能的课程
    public List<Course> getPossibleCourseByTeacherIdList(String courseName,Integer credit,List<Integer> teacherIdList,String prop){
        List<Course> courseList = new ArrayList<>();
        for (Integer teacherId : teacherIdList) {
            List<Course> courseByCourseNameAndCreditAndCreditAndTeacherIdAndProp = courseRepository.findCourseByCourseNameAndCreditAndCreditAndTeacherIdAndProp(courseName,credit , teacherId, prop);
            for (Course course : courseByCourseNameAndCreditAndCreditAndTeacherIdAndProp) {
                courseList.add(course);
            }
        }
        return courseList;
    }

    public List<Course> getPossibleCourseByTeacherIdList(String courseName,List<Integer> teacherIdList,String prop ){
        List<Course> courseList = new ArrayList<>();
        for (Integer teacherId : teacherIdList) {
            List<Course> courseByCourseNameAndCreditAndCreditAndTeacherIdAndProp = courseRepository.findCourseByCourseNameAndPropAndTeacherId(courseName,prop,teacherId);
            for (Course course : courseByCourseNameAndCreditAndCreditAndTeacherIdAndProp) {
                courseList.add(course);
            }
        }
        return courseList;
    }

    public boolean JudgeCourseZip(String attachFolder, String id) {
        String fileName =attachFolder + "CourseZip/" +  id + "-" + getCurrentTeacher().getId() + ".ZIP";
        File file = new File(fileName);
        return file.exists();
    }

    @PostMapping("/computeCredit")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse computeCredit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();

        List<Course> courseByStudentId = getCourseByStudentId();
        int result=0;
        for (Course course : courseByStudentId) {
            result+=course.getCredit();
        }
        Map m = new HashMap();
        m.put("result",result);
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showCourse")
    @PreAuthorize(("hasRole('STUDENT')"))
    //页面加载完毕后调用的方法，展示学生用户本学期的所有选课
    public DataResponse showCourse(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        for(int i=0;i<=34;i++){
            Map temp = new HashMap();
            temp.put("name","");
            temp.put("credit","");
            temp.put("teacher","");
            temp.put("scope","");
            temp.put("place","");
            temp.put("prop","");
            dataList.add(temp);
        }
        //先找出来这是哪个用户
        Integer userId = CommonMethod.getUserId();
        Optional<Student> studentByUserId = studentRepository.findStudentByUserId(userId);
        //查询结果不为空，查询该学生的所有选课
        if(studentByUserId.isPresent()){
            Map m;
            //根据学生Id查其选择课程
            List<StuCourse> courseByStudentId = studentCourseRepository.findCourseByStudentId(studentByUserId.get().getId());
            for (StuCourse stuCourse : courseByStudentId) {
                m=new HashMap<>();
                //根据课程Id查对应课程实体
                Integer CourseId = stuCourse.getCourse().getId();
                Optional<Course> courseByCourseId = courseRepository.findCourseByCourseId(CourseId);
                //拿到课程对象，找这是哪个老师的
                Course course = courseByCourseId.get();
                //找到这节课什么时候上
                String time = course.getTime();
                //一节课一周可能上多次
                String[] times = time.split(" ");
                Person person = course.getTeacher().getPerson();
                Integer personId = person.getId();
                Optional<Person> personById = personRepository.findPersonById(personId);
                if(personById.isPresent()){
                    m.put("teacher",personById.get().getPerName());
                }
                m.put("name",course.getCourseName());
//                m.put("teacher",course.getTeacher());
                m.put("credit",course.getCredit());
                m.put("scope",course.getScope());
                m.put("place",course.getPlace());
                m.put("prop",course.getProp());
                //多次课需要按个set
                for (String s : times) {
                    dataList.set(Integer.parseInt(s),m);
                }
            }
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showSelectList")
    @PreAuthorize(("hasRole('STUDENT')"))
    //选课页面自动加载，展示所有可供选的课程
    public DataResponse showSelectList(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        //一个需求,选课后,不能再选同一个时间上课的课了
        //找到这个学生，找到他已经选择的课，把所有占用的时间放到线性表中,之后比对当前课程和线性表中课程是否时间有重复，
        // 若有则不能选这门课
        List<Course> courseByStudentId = getCourseByStudentId();
        HashSet<String> times = new HashSet<>();
        //已选课程所有占用时间放入集合
        for (Course course : courseByStudentId) {
            //这节课所有的上课时间
            String[] split = course.getTime().split(" ");
            for (String s : split) {
                times.add(s);
            }
        }
        //获悉该学生是第几学期了
        Integer term = getCurrentStudent().getTerm();
        //获取该学生年级
        Integer grade = getCurrentStudent().getGrade();
        List<Course> courseByGradeAndTerm = courseRepository.findCourseByGradeAndTerm(grade, term);
        //对每一门可能可以选的课进行检查
        for (Course course : courseByGradeAndTerm) {
            //待检查的课程时间
            //false代表未冲突
            boolean flag = false;
            boolean isSelected = courseByStudentId.contains(course);
            String[] check = course.getTime().split(" ");
            for (String s : check) {
                for (String time : times) {
                    if (time.equals(s)) {
                        flag = true;
                        break;
                    }

                }
            }
            List<StuCourse> courseByCourseId = studentCourseRepository.findCourseByCourseId(course.getId());
            int size = courseByCourseId.size();
            //如果没被选，当前人数也超过了上限
            if(!isSelected&&size>=course.getUpperLimit()){
                continue;
            }
            Map m = new HashMap();
            m.put("id",course.getId());
            m.put("name",course.getCourseName());
            m.put("credit",course.getCredit());
            m.put("teacher",getTeacherNameByCourse(course));
            m.put("scope",course.getScope());
            m.put("place",course.getPlace());
            m.put("prop",course.getProp());
            m.put("time",GetCourseTime(course.getTime()));
            m.put("currentSize/upperLimit",size+"/"+course.getUpperLimit());
            //如果时间冲突，检查是否已经选过这门课了，选过则加入，没选过就跳过
            if(flag&&isSelected){
                m.put("selected","true");
            }
            else if(flag&&(!isSelected)){
                continue;
            }
            else{
                m.put("selected","false");
            }
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/chooseCourse")
    @PreAuthorize(("hasRole('STUDENT')"))
    //学生选课
    public DataResponse chooseCourse(@Valid @RequestBody DataRequest dataRequest) {
        Map data = dataRequest.getData();
        //找到课程id
        Integer CourseId = (Integer) data.get("id");

        //向stuCourse表中添加
        StuCourse stuCourse = new StuCourse();
        stuCourse.setCourse(courseRepository.findCourseByCourseId(CourseId).get());
        stuCourse.setStudent(getCurrentStudent());
        if(studentCourseRepository.getMaxId()!=null){
            stuCourse.setId(studentCourseRepository.getMaxId()+1);
        }
        else{
            stuCourse.setId(1);
        }
        studentCourseRepository.save(stuCourse);
        return CommonMethod.getReturnData(new ArrayList<>());
    }

    @PostMapping("/rejectCourse")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse rejectCourse(@Valid @RequestBody DataRequest dataRequest) {
        Map data = dataRequest.getData();

        Integer courseId = (Integer) data.get("id");
        Integer studentId = getCurrentStudent().getId();
        Optional<StuCourse> studentCourse = studentCourseRepository.findStudentCourse(studentId, courseId);
        studentCourse.ifPresent(stuCourse -> studentCourseRepository.delete(stuCourse));

        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/courseQuery")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse courseQuery(@Valid @RequestBody DataRequest dataRequest) {
        Map data = dataRequest.getData();
        List dataList = new ArrayList();
        String courseName = (String) data.get("name");
//        Integer credit;
//        if(data.get("credit")!=""){
//            credit= (Integer) data.get("credit");
//        }
//        else{
//            credit=null;
//        }
        String credit = (String) data.get("credit");
        String teacher = (String) data.get("teacher");
        String prop = (String) data.get("prop");
        //模糊查询里有教师姓名
        if(!teacher.equals("")){
            List<Course> possibleCourseByTeacherIdList;
            if(credit!=""){
                possibleCourseByTeacherIdList = getPossibleCourseByTeacherIdList(courseName, Integer.parseInt(credit), getPossibleTeacherId(teacher), prop);
            }else{
                possibleCourseByTeacherIdList = getPossibleCourseByTeacherIdList(courseName,getPossibleTeacherId(teacher),prop);
            }

            for (Course course : possibleCourseByTeacherIdList) {
                Map m = new HashMap();
                m.put("id",course.getId());
                m.put("name",course.getCourseName());
                m.put("credit",course.getCredit());
                m.put("teacher",getTeacherNameByCourse(course));
                m.put("scope",course.getScope());
                m.put("place",course.getPlace());
                m.put("prop",course.getProp());
                m.put("time",GetCourseTime(course.getTime()));
                //选了课
                if(whetherSelected(course.getId())){
                    m.put("selected","true");
                }
                //没选课
                else{
                    m.put("selected","false");
                }
                dataList.add(m);
            }
        }
        //模糊查询里没教师姓名
        else{
            List<Course> courseByCourseNameAndProp;
            if(credit!=""){
                courseByCourseNameAndProp=courseRepository.findCourseByCourseNameAndCreditAndProp(courseName,Integer.parseInt(credit),prop);
            }
            else{
                courseByCourseNameAndProp= courseRepository.findCourseByCourseNameAndProp(courseName, prop);
            }

            for (Course course : courseByCourseNameAndProp) {
                Map m = new HashMap();
                m.put("id",course.getId());
                m.put("name",course.getCourseName());
                m.put("credit",course.getCredit());
                m.put("teacher",getTeacherNameByCourse(course));
                m.put("scope",course.getScope());
                m.put("place",course.getPlace());
                m.put("prop",course.getProp());
                m.put("time",GetCourseTime(course.getTime()));
                //选了课
                if(whetherSelected(course.getId())){
                    m.put("selected","true");
                }
                //没选课
                else{
                    m.put("selected","false");
                }
                dataList.add(m);
            }
        }

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showTeacherCourse")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse showTeacherCourse(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Teacher currentTeacher = getCurrentTeacher();
        Integer teacherId = currentTeacher.getId();
        List<Course> courseByTeacherId = courseRepository.findCourseByTeacherId(teacherId);
        for (Course course : courseByTeacherId) {
            Map m = new HashMap();
            m.put("id",course.getId());
            m.put("name",course.getCourseName());
            m.put("credit",course.getCredit());
            m.put("scope",course.getScope());
            m.put("place",course.getPlace());
            m.put("prop",course.getProp());
            m.put("grade",course.getGrade());
            m.put("time",GetCourseTime(course.getTime()));
            if(JudgeCourseZip(attachFolder,course.getId().toString())){
                m.put("upload","true");
            }
            else{
                m.put("upload","false");
            }
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);

    }

    @PostMapping("/getSimpleTeacherCourse")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse getSimpleTeacherCourse(@Valid @RequestBody DataRequest dataRequest){
        //总归是35节课，列表给出
        List dataList = new ArrayList();
        for(int i=0;i<=34;i++){
            Map temp = new HashMap();
            temp.put("name","");
            temp.put("scope","");
            temp.put("place","");
            temp.put("prop","");
            dataList.add(temp);
        }
        //找到当前的老师
        Teacher currentTeacher = getCurrentTeacher();
        Integer teacherId = currentTeacher.getId();
        //去课程里找这个老师的课
        //先找到这是第几学期的
        Optional<Student> byId = studentRepository.findById(2);
        Integer term = byId.get().getTerm();
        List<Course> courseByTeacherIdAndTerm = courseRepository.findCourseByTeacherIdAndTerm(teacherId, term);
        //对于每个课程，查询时间,把这门课对应的所有时间的map都更新，设置到dataList中
        for (Course course : courseByTeacherIdAndTerm) {
            String time = course.getTime();
            String[] times = time.split(" ");
            Map m = new HashMap();
            m.put("prop",course.getProp());
            m.put("name",course.getCourseName());
            m.put("scope",course.getScope());
            m.put("place",course.getPlace());
            for (String s : times) {
                dataList.set(Integer.parseInt(s),m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/uploadCourseZip")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse uploadCourseZip(@RequestParam Map pars, @RequestParam("file") MultipartFile file) {
        Integer i = courseRepository.getMaxId() + 1;
        String id = i.toString();
        Integer teacherId = getCurrentTeacher().getId();
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "CourseZip/" +  id + "-" + teacherId + ".ZIP";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/addCourse")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse addCourse(@Valid @RequestBody DataRequest dataRequest){
        String name = (String) dataRequest.get("name");
        Integer credit = Integer.parseInt((String) dataRequest.get("credit"));
        String place = (String) dataRequest.get("place");
        String prop = (String) dataRequest.get("prop");
        String scope = (String) dataRequest.get("scope");
        Integer grade = Integer.parseInt((String) dataRequest.get("grade"));
        String time = (String) dataRequest.get("time");
        Integer upperLimit = dataRequest.getInteger("upperLimit");
        Teacher currentTeacher = getCurrentTeacher();
        Course course = new Course();
        course.setId(courseRepository.getMaxId()+1);
        course.setCourseName(name);
        course.setCredit(credit);
        course.setPlace(place);
        course.setProp(prop);
        course.setScope(scope);
        course.setGrade(grade);
        course.setTime(time);
        course.setTerm(studentRepository.getById(2).getTerm());
        course.setTeacher(currentTeacher);
        course.setUpperLimit(upperLimit);
        courseRepository.save(course);
        return CommonMethod.getReturnMessageOK("success!");
    }

    @PostMapping("/updateCourseZip")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse updateCourseZip(@RequestParam Map pars, @RequestParam("file") MultipartFile file) {
        String id = CommonMethod.getString(pars, "id");
        Integer teacherId = getCurrentTeacher().getId();
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "CourseZip/" +  id + "-" + teacherId + ".ZIP";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteCourse")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse deleteCourse(@Valid @RequestBody DataRequest dataRequest){
        Integer id = (Integer) dataRequest.get("id");
        Course byId = courseRepository.getById(id);
        courseRepository.delete(byId);
        return CommonMethod.getReturnMessageOK("success!");
    }

    @PostMapping("/openCourse")
    @PreAuthorize(("hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse openCourse(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        String start = (String) dataRequest.get("startTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse(start);
        String end = (String) dataRequest.get("endTime");
        Date endTime = sdf.parse(end);
        if(endTime.getTime()<=startTime.getTime()){
            return CommonMethod.getReturnMessageError("起始时间大于等于终止时间了捏");
        }
        Integer maxId = openRepository.getMaxId();
        Integer id;
        if(maxId==null){
            id=1;
        }
        else {
            id=maxId+1;
        }
        Open open = new Open();
        open.setId(id);
        open.setStartTime(startTime);
        open.setEndTime(endTime);
        openRepository.save(open);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/checkIsOpen")
    @PreAuthorize(("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')"))
    public DataResponse checkIsOpen(@Valid @RequestBody DataRequest dataRequest){
        Integer maxId = openRepository.getMaxId();
        if(maxId!=null){
            Open byId = openRepository.getById(maxId);
            long current = System.currentTimeMillis();
            Date startTime = byId.getStartTime();
            Date endTime = byId.getEndTime();
            if(current>=startTime.getTime()&&current<=endTime.getTime()) {
                Map m = new HashMap();
                m.put("isOpen", "true");
                return CommonMethod.getReturnData(m);
            }
        }
        Map m = new HashMap();
        m.put("isOpen","false");
        return CommonMethod.getReturnData(m);
    }

    @GetMapping ("/downloadExcel")
    public void generateCheckCode( HttpServletRequest request,HttpServletResponse response) throws Exception {
//        System.out.println(request.getParameter("courseId"));
        Integer courseId = Integer.parseInt(request.getParameter("courseId")) ;
        List<StuCourse> courseByCourseId = studentCourseRepository.findCourseByCourseId(courseId);
        //内容列表
        List<CourseSheet> list = new ArrayList<>();
        for (StuCourse stuCourse : courseByCourseId) {
            Student student = stuCourse.getStudent();
            list.add(new CourseSheet(student.getStudentName(), student.getStudentNum(), courseId));
        }
//        list.add(new CourseSheet("lyh","202100300063",1));
        //输出流 格式
        response.setContentType("application/vnd.ms-excel");
        Course byId = courseRepository.getById(courseId);
        String perName = byId.getTeacher().getPerson().getPerName();
        response.setHeader("Content-Disposition","attachment; filename="+perName+"-"+courseId+".xlsx");
        //response输出流写回
        ExcelUtil.writeExcel(response,list);
    }

}
