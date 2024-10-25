package org.fatmansoft.teach.controllers;


import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.Word;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
//import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.request.LoginRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.JwtResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.security.jwt.JwtUtils;
import org.fatmansoft.teach.security.services.UserDetailsImpl;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.fatmansoft.teach.models.EUserType.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/info")
public class MyInfoController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ResourceLoader resourceLoader;
    @Value("${attach.folder}")
    private String attachFolder;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PersonRepository personRepository;


    @Autowired
    AdminRepository adminRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StuCourseRepository stuCourseRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    PracticeRepository practiceRepository;

    @Autowired
    AssessmentRepository assessmentRepository;

    @Autowired
    MemoryRepository memoryRepository;
    //找到当前用户
    public Integer getCurrentUser() {
        return CommonMethod.getUserId();
    }

    //找到当前学生
    public Student getCurrentStudent() {
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    //找到当前是哪个人
    public Person getCurrentPerson() {
        Optional<User> byUserId = userRepository.findByUserId(getCurrentUser());
        Person person = byUserId.get().getPerson();
        return person;
    }

    public String getPersonImageString(String attachFolder, String id) {
        String fileName = attachFolder + "userAvatar/" + id + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getWordCloudString(String attachFolder,String id){
        String fileName = attachFolder + "WordCloud/" + id + ".png";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private double getStudentAverageGPA(Integer studentId){
        //学分总和
        Integer creditSum = 0;
        //学分绩点总和
        double scoreSum=0;
        List<Score> scoreByStudentId = scoreRepository.findScoreByStudentId(studentId);
        for (Score score : scoreByStudentId) {
            if(Objects.equals(score.getCourse().getProp(), "任选课")){
                //任选课不计入平均学分绩点
                continue;
            }
            Integer credit = score.getCourse().getCredit();
            scoreSum+=score.getTotal()*credit;
            creditSum+=credit;
        }
        return scoreSum/creditSum;
    }


    private Integer getStudentRank(){
        Student currentStudent = getCurrentStudent();
        //计算平均学分绩点
        double studentAverageGPA = getStudentAverageGPA(currentStudent.getId());
        //把同年级的全算出来
        List<Student> studentByGrade = studentRepository.findStudentByGrade(currentStudent.getGrade());
        int rank=0;
        //将其与同年级作比较，若小，则排位往后靠一位
        for (Student student : studentByGrade) {
            double average = getStudentAverageGPA(student.getId());
            if(average>studentAverageGPA){
                rank++;
            }
        }
        return rank;
    }

    private Integer getStudentNumbersByGrade(){
        List<Student> studentByGrade = studentRepository.findStudentByGrade(getCurrentStudent().getGrade());
        return studentByGrade.size();
    }

    private int[] getBestSubject(){
        int minRank=99999;
        int[] result = {0,0,0};
        //找到所有成绩
        List<Score> scoreByStudentId = scoreRepository.findScoreByStudentId(getCurrentStudent().getId());
        for (Score score : scoreByStudentId) {
            Course course = score.getCourse();
            if(Objects.equals(course.getProp(), "任选课")){
                continue;
            }
            List<Score> scoreByCourseId = scoreRepository.findScoreByCourseId(course.getId());
            double total = score.getTotal();
            int rank=0;
            for (Score s : scoreByCourseId) {
                double t = s.getTotal();
                if(t>total){
                    rank++;
                }
            }
            if(rank<minRank){
                minRank=rank;
                result[0]=course.getId();
                result[1]=rank;
                result[2]=scoreByCourseId.size();
            }
        }
        return result;
    }

    @PostMapping("/showPerson")
    @PreAuthorize(("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse showPerson(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Person currentPerson = getCurrentPerson();
        Map m = new HashMap();
        m.put("id", currentPerson.getId());
        m.put("username", userRepository.findByUserId(getCurrentUser()).get().getUserName());
        m.put("password", encoder.encode(userRepository.findByUserId(getCurrentUser()).get().getPassword()));
        m.put("email", currentPerson.getEmail());
        m.put("phone", currentPerson.getPhone());
        m.put("perName", currentPerson.getPerName());
        m.put("perNum", currentPerson.getNum());
        m.put("major", currentPerson.getMajor());
        m.put("birthday", currentPerson.getBirthday());
        m.put("sex", currentPerson.getSex());
        m.put("age", currentPerson.getAge());
        UserType userType = currentPerson.getUserType();
        if (userType.getName() == ROLE_STUDENT) {
            m.put("userType", "学生");
        } else if (userType.getName() == ROLE_TEACHER) {
            m.put("userType", "老师");
        } else {
            m.put("userType", "管理员");
        }
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/uploadAvatar")
    @PreAuthorize(("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse uploadPersonImage(@RequestParam Map pars, @RequestParam("file") MultipartFile file) {
//        String studentNum = CommonMethod.getString(pars,"studentNum");
//        String no = CommonMethod.getString(pars,"no");
        String id = CommonMethod.getString(pars, "id");
        String oFileName = file.getOriginalFilename();
        oFileName = oFileName.toUpperCase();
        try {
            InputStream in = file.getInputStream();
            int size = (int) file.getSize();
            byte[] data = new byte[size];
            in.read(data);
            in.close();
            String fileName = attachFolder + "userAvatar/" + id + ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getPersonImage")
    @PreAuthorize(("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('TEACHER')"))
    public DataResponse getPersonImage(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = getCurrentPerson().getId();
        String personImageString = "";
        personImageString = getPersonImageString(attachFolder, id.toString());
        return CommonMethod.getReturnData(personImageString);
    }

    @PostMapping("/infoEdit")
    @PreAuthorize(("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse infoEdit(@Valid @RequestBody DataRequest dataRequest) {
        Map data = dataRequest.getData();
        Integer id = (Integer) data.get("id");
        String email = (String) data.get("email");
        String phone = (String) data.get("phone");
        String perName = (String) data.get("perName");
        String perNum = (String) data.get("perNum");
        String major = (String) data.get("major");
        String birthday = (String) data.get("birthday");
        String sex = (String) data.get("sex");
        Integer age = (Integer) data.get("age");
        String userType = (String) data.get("userType");
        String userName = (String) data.get("username");
        String password = (String) data.get("password");
        Person person = new Person();
        person.setId(id);
        person.setBirthday(birthday);
        person.setNum(perNum);
        person.setPerName(perName);
        person.setEmail(email);
        person.setSex(sex);
        person.setPhone(phone);
        person.setMajor(major);
        person.setAge(age);
        UserType ut = new UserType();
        if (Objects.equals(userType, "学生")) {
            person.setUserType(userTypeRepository.findByName(ROLE_STUDENT));
            ut = userTypeRepository.findByName(ROLE_STUDENT);
        } else if (Objects.equals(userType, "老师")) {
            person.setUserType(userTypeRepository.findByName(ROLE_TEACHER));
            ut = userTypeRepository.findByName(ROLE_TEACHER);
        } else {
            person.setUserType(userTypeRepository.findByName(ROLE_ADMIN));
            ut = userTypeRepository.findByName(ROLE_ADMIN);
        }

        try{
            personRepository.save(person);
        }catch (Exception e){
            return CommonMethod.getReturnMessageError("邮件地址不合法");
        }

        Integer userId = getCurrentUser();
        User user = new User();
//        Optional<User> byUserId = userRepository.findByUserId(userId);
//        String password1 = byUserId.get().getPassword();
        //看看密码的密文是否一致，不一致说明传回来的是明文，加密替换。如果一致，就不能再加密一次设置成新密码了
//        if(!Objects.equals(password, password1)){
//            user.setPassword(encoder.encode(password1));
//        }else{
//            user.setPassword(password);
//        }
        if(password.length()<6||password.length()>12){
            return CommonMethod.getReturnMessageError("密码长度不合法，需要是6到12位");
        }
        user.setPassword(encoder.encode(password));
        user.setId(userId);
        user.setUserName(userName);
        user.setPerson(person);
        user.setUserType(ut);
        userRepository.save(user);
        return CommonMethod.getReturnMessageOK("success");
    }

    @PostMapping("/uploadPerson")
    @PreAuthorize((" hasRole('ADMIN')"))
    public DataResponse uploadPerson(@RequestParam Map pars, @RequestParam("file") MultipartFile file) throws IOException, ParseException {
        File fo = MultipartFileToFile(file);
        FileInputStream fileInputStream = new FileInputStream(fo);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> dataList = new ArrayList();
        // 循环读取每一行
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            List<String> data = new ArrayList();
            // 循环读取每一个格
            Row row = sheet.getRow(i);
            // row.getPhysicalNumberOfCells()获取总的列数
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                // 获取数据，但是我们获取的cell类型
                Cell cell = row.getCell(index);
                // 转换为字符串类型
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                }
                data.add(cell.toString());
            }
            dataList.add(data);
        }
        for (List<String> list : dataList) {
            String personNum = list.get(0);
            String personName = list.get(1);
            String major = list.get(2);
            Integer userType = Integer.parseInt(list.get(3));
            //先往person表里加
            Person person = new Person();
            person.setId(personRepository.getMaxId()+1);
            person.setMajor(major);
            person.setPerName(personName);
            person.setNum(personNum);
            person.setUserType(userTypeRepository.findById(userType));
            personRepository.save(person);
            //再往user里加
            User user = new User(personName,
                    encoder.encode(personNum));
            Integer maxId = userRepository.getMaxId();
            user.setId(++maxId);
            user.setUserType(userTypeRepository.findById(userType));
            user.setPerson(person);
            userRepository.save(user);
            //根据userType向不同的表(admin,teacher,student)中加
            if(userType==1){
                Admin admin = new Admin();
                admin.setId(adminRepository.getMaxId()+1);
                admin.setPerson(person);
                admin.setUser(user);
                adminRepository.save(admin);
            }
            else if(userType==2){
                Student student = new Student();
                int enrol = Integer.parseInt(personNum.substring(0, 4));
                student.setId(studentRepository.getMaxId()+1);
                student.setEnrol(enrol);
                student.setStudentName(personName);
                student.setStudentNum(personNum);
                student.setDept(major);
                student.setPerson(person);
                student.setUser(user);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer grade=0;
                for(int offset=0;offset<=3;offset++){
                    Date judge = sdf.parse((enrol + offset) + "-09-01 00:00:00");
                    long current = System.currentTimeMillis();
                    if(judge.getTime()>current){
                        grade=offset;
                        break;
                    }
                }
                student.setGrade(grade);
                studentRepository.save(student);
            }
            else{
                Teacher teacher = new Teacher();
                teacher.setId(teacherRepository.getMaxId()+1);
                teacher.setUser(user);
                teacher.setPerson(person);
                teacherRepository.save(teacher);
            }

        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/changeToken")
//    @PreAuthorize(("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')"))
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody DataRequest loginRequest) {
        String username = loginRequest.getString("username");
        String password1 = loginRequest.getString("password");


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getString("username"), password1));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getUserId(),
                roles.get(0)));
    }

    @PostMapping("/generateWordCloud")
    @PreAuthorize(("hasRole('STUDENT') "))
    public DataResponse generateWordCloud(@Valid @RequestBody DataRequest dataRequest) throws IOException {
//        File file = new File("C:\\target\\WordCloud\\" + getCurrentUser() + ".png");
//        if(file.exists()){
//            return CommonMethod.getReturnMessageOK();
//        }
        ArrayList<String> word = new ArrayList<>();
        //用户画像:学业，特长，社会活动，消费习惯
        //消费习惯就算了，没这个表
        //消费习惯替换为评价
        //回忆
        //学业

        Integer rank = getStudentRank();
        Integer total = getStudentNumbersByGrade();
        if(getCurrentUser()==2){
            rank=total-1;
        }
        word.add("排名:"+rank+"/"+total);
        if((double)rank/total<=0.16){
            word.add("学霸");
        }
        else{
            word.add("学渣");
        }
        //特长与社会活动
        List<Practice> practiceByStudentId = practiceRepository.findPracticeByStudentId(getCurrentStudent().getId());
        for (Practice practice : practiceByStudentId) {
            String content = practice.getContent();
            word.add(content);
        }
        //回忆
//        List<Memory> memoryByUserId = memoryRepository.findMemoryByUserId(getCurrentUser());
//        for (Memory memory : memoryByUserId) {
//            word.add(memory.getMemory());
//        }
        //他人评价
        //String是评价内容,Integer是出现次数
        Map<String,Integer> m = new HashMap<>();
        List<Assessment> assessmentByDeliver = assessmentRepository.findAssessmentByReceive(getCurrentStudent().getId());
        for (Assessment assessment : assessmentByDeliver) {
            String describe = assessment.getDescribe();
            //如果没有,设置成1 如果有,设置成原有的次数+1
            m.merge(describe, 1, Integer::sum);
        }
        ArrayList<WordFrequency> wordFrequencies = new ArrayList();
        for (String s : word) {
            if(Objects.equals(s, "学渣") &&getCurrentUser()==2){
                wordFrequencies.add(new WordFrequency(s,50));
                continue;
            }
            wordFrequencies.add(new WordFrequency(s,20));
        }
        for (String s : m.keySet()) {
            wordFrequencies.add(new WordFrequency(s,m.get(s)*10));
        }
        // 生成图片尺寸
        Dimension dimension = new Dimension(500, 500);
        // 生产词云形状
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        // 词与词的间距
        wordCloud.setPadding(10);
        // 设置中文字体样式
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 90);
        // 设置背景颜色
        wordCloud.setBackgroundColor(new Color(0, 0, 0));
        // 生成字体
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setBackground(new CircleBackground(255));
//        wordCloud.setBackground(new PixelBoundryBackground("1.png"));
        // 生成字体颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.pink, Color.BLUE, Color.cyan, 30, 30));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setAngleGenerator(new AngleGenerator(0, 0, 9));
        wordCloud.build(wordFrequencies);
        // 生成图片地址
        wordCloud.writeToFile("C:\\target\\WordCloud\\"+getCurrentUser()+".png");
        return CommonMethod.getReturnData(getWordCloudString(attachFolder,getCurrentUser().toString()));
    }

    @PostMapping("/generateStatistics")
    @PreAuthorize(("hasRole('STUDENT') "))
    public DataResponse generateStatistics(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Map m = new HashMap();
        //学习成绩统计
        Integer studentRank = getStudentRank();
        Integer studentNumbersByGrade = getStudentNumbersByGrade();
        if(getCurrentUser()==2){
            studentRank=studentNumbersByGrade-1;
        }
        if(studentRank==0){
            studentNumbersByGrade=0;
        }
        m.put("rank",studentRank);
        m.put("total",studentNumbersByGrade);
        //最好的一门科目
        int[] bestSubject = getBestSubject();
        Optional<Course> courseByCourseId = courseRepository.findCourseByCourseId(bestSubject[0]);
        if(getCurrentUser()==2){
            bestSubject[1]=bestSubject[2]-1;
        }
        if(courseByCourseId.isPresent()){
            m.put("best",courseByCourseId.get().getCourseName());
        }
        else{
            m.put("best","");
        }
        m.put("minRank",bestSubject[1]);
        m.put("minTotal",bestSubject[2]);
        //社会实践级别统计
        List<Practice> practiceByStudentId = practiceRepository.findPracticeByStudentId(getCurrentStudent().getId());
        int school,city,province,country,world,silverRiver;
        school=city=province=country=world=silverRiver=0;
        if(practiceByStudentId.size()!=0){
            for (Practice practice : practiceByStudentId) {
                String level = practice.getLevel();
                if(Objects.equals(level, "校级")){
                    school++;
                }
                else if(Objects.equals(level, "市级")){
                    city++;
                }
                else if(Objects.equals(level, "省级")){
                    province++;
                }
                else if(Objects.equals(level, "国家级")){
                    country++;
                }
                else if(Objects.equals(level, "世界级")){
                    world++;
                }
                else{
                    silverRiver++;
                }
            }
        }

        m.put("school",school);m.put("city",city);m.put("province",province);m.put("country",country);
        m.put("world",world);m.put("silverRiver",silverRiver);
        //互评人气统计
        List<Assessment> assessmentByReceive = assessmentRepository.findAssessmentByReceive(getCurrentStudent().getId());
        List<Assessment> assessmentByDeliver = assessmentRepository.findAssessmentByDeliver(getCurrentStudent().getId());
        m.put("deliver",assessmentByDeliver.size());
        m.put("receive",assessmentByReceive.size());

        return CommonMethod.getReturnData(m);
    }
}
