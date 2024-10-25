package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Practice;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.PracticeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentManagement")
public class StudentManagementController {
//    private static Integer fileId=0;
    @Value("${attach.folder}")
    private String attachFolder;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    @Autowired
    private UserRepository userRepository;

    //author:lsh 将grade和term转换为字符串
    public static String GetCourseYear ( int grade, int term){
        String result = "大";
        String[] number = {"一", "二", "三", "四"};
        result += number[grade-1];
        result +="第";
        result+=number[term-1];
        result+="学期";

        return result;
    }

    //author:lsh 将字符串转换为grade和term
    public static int[] GetCurrentTimeNumber(String time){
        int[]a=new int[2];
        char str=time.charAt(1);
        char temp=time.charAt(3);
        switch (str){
            case '一':
                a[0]=1;
                break;
            case '二':
                a[0]=2;
                break;
            case '三':
                a[0]=3;
                break;
            case '四':
                a[0]=4;
                break;
            default:
                break;
        }
        switch (temp){
            case '一':
                a[1]=1;
                break;
            case '二':
                a[1]=2;
                break;
            default:
                break;
        }
        return a;

    }

    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    //找到当前是哪个人
    public Person getCurrentPerson(){
        Optional<User> byUserId = userRepository.findByUserId(getCurrentUser());
        Person person = byUserId.get().getPerson();
        return person;
    }

    public String getPersonImageString(String attachFolder, String id) {
        String fileName =attachFolder + "userProof/" + id  + ".ZIP";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:zip;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @PostMapping("/showPractice")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showPractice(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Integer id = getCurrentStudent().getId();
        List<Practice> practiceByStudentId = practiceRepository.findPracticeByStudentId(id);
        for (Practice practice : practiceByStudentId) {
            Map m = new HashMap<>();
            m.put("id",practice.getId());
            m.put("term",GetCourseYear(practice.getGrade(), practice.getTerm()));
            m.put("level", practice.getLevel());
            m.put("content", practice.getContent());
            m.put("award", practice.getAward());
            m.put("date", practice.getDate());
            m.put("status",practice.getStatus());
            dataList.add(m);
        }

        return  CommonMethod.getReturnData(dataList);

    }

    @PostMapping("/practiceQuery")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse practiceQuery(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Map data = dataRequest.getData();
        Integer term = null;
        Integer grade = null;
        String tg = (String) data.get("term");
        String level = (String) data.get("level");
        String status = (String) data.get("status");
        String date = (String) data.get("date");
        if(tg!="") {
            int[] termAndGrade = GetCurrentTimeNumber(tg);
            term = termAndGrade[1];
            grade = termAndGrade[0];
        }
        List<Practice> practiceByTermAndGradeAndLevelAndStatusAndDate;
        if(term!=null){
            practiceByTermAndGradeAndLevelAndStatusAndDate = practiceRepository.findPracticeByTermAndGradeAndLevelAndStatusAndDate(term,grade,level,status,date);
        }
        else {
            practiceByTermAndGradeAndLevelAndStatusAndDate = practiceRepository.findPracticeByLevelAndStatusAndDate(level,status,date);
        }
        for (Practice practice : practiceByTermAndGradeAndLevelAndStatusAndDate) {
            Map m = new HashMap<>();
            m.put("term",GetCourseYear(practice.getGrade(), practice.getTerm()));
            m.put("level", practice.getLevel());
            m.put("content", practice.getContent());
            m.put("award", practice.getAward());
            m.put("date", practice.getDate());
            m.put("status",practice.getStatus());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/addPractice")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse addPractice(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        Integer studentId = getCurrentStudent().getId();
        String tg = (String) data.get("term");
        String level = (String) data.get("level");
        String content = (String) data.get("content");
        String award = (String) data.get("award");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(date);
        Integer maxId = practiceRepository.getMaxId();
        Practice practice = new Practice();
        practice.setId(maxId+1);
        practice.setStudent(studentRepository.getById(studentId));
        practice.setAward(award);
        practice.setDate(dateStr);
        practice.setContent(content);
        practice.setLevel(level);
        practice.setStatus("未通过");
        practice.setTerm(GetCurrentTimeNumber(tg)[1]);
        practice.setGrade(GetCurrentTimeNumber(tg)[0]);
        practiceRepository.save(practice);
        return CommonMethod.getReturnMessageOK("success");
    }

//    @PostMapping("/storeFileId")
//    @PreAuthorize(("hasRole('STUDENT')"))
//    public DataResponse storeFileId(@Valid @RequestBody DataRequest dataRequest){
//        Integer id = (Integer) dataRequest.get("id");
//        fileId=id;
//        return CommonMethod.getReturnMessageOK();
//    }

    @PostMapping("/uploadProof")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse uploadProof(@RequestParam Map pars, @RequestParam("file") MultipartFile file) {
        String id = CommonMethod.getString(pars, "id");
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "userProof/" +  id + ".ZIP";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

}
