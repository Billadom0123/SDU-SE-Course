package org.fatmansoft.teach.controllers;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/score")
public class ScoreController {
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
    private ScoreRepository scoreRepository;

    //找到当前用户
    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

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

    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

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

    //对当前学期的成绩计算排名,班级(同一个老师下的)平均分
    public List getReturnScore(List<Score> scoreList){
        List dataList = new ArrayList();
        Student student = getCurrentStudent();
        Integer studentId = getCurrentStudent().getId();
        //对当前学期的成绩计算排名,班级(同一个老师下的)平均分
        Integer rank=1,total=0,max=0;
        double average=0;
        for (Score score : scoreList) {
            //对每一门成绩，先查哪门课，把那门课的所有成绩调出来，计算排名，总分，平均分
            Integer courseId = score.getCourse().getId();
            List<Score> scoreByCourseId = scoreRepository.findScoreByCourseId(courseId);
            Collections.sort(scoreByCourseId);
            max=(int)(scoreByCourseId.get(scoreByCourseId.size()-1).getTotal()+0.5);
            double sum=0;
            for (Score s : scoreByCourseId) {
                //如果这个成绩是你的，那么就找到了你的排名
                if(Objects.equals(s.getStudent().getId(), studentId)){
                    break;
                }
                rank++;
            }
            //计算总分
            for (Score s : scoreByCourseId) {
                sum+=s.getTotal();
            }
            average=sum/scoreByCourseId.size();
            Map m = new HashMap();
            m.put("term",GetCourseYear(student.getGrade(),student.getTerm()));
            m.put("name",courseRepository.findCourseByCourseId(courseId).get().getCourseName());
            m.put("regular",score.getRegular());
            m.put("exam",score.getExam());
            m.put("total",(int)(score.getTotal()+0.5));
            m.put("rank",(scoreByCourseId.size()-rank)+"/"+scoreByCourseId.size());
            m.put("top",max);
            m.put("average",average);
            dataList.add(m);
        }
        return dataList;
    }

    public double getAverageGPA(Integer grade,Integer term){
        //找到是哪个学生，找到这个学生这学期所有的成绩(不含任选课),根据公式计算平均学分绩点
        Integer studentId = getCurrentStudent().getId();
        List<Score> scoreByStudentIdAndGradeAndTerm = scoreRepository.findScoreByStudentIdAndGradeAndTerm(studentId, grade, term);
        double scoreSum=0,creditSum=0;
        for (Score score : scoreByStudentIdAndGradeAndTerm) {
            if(Objects.equals(score.getCourse().getProp(), "任选课")){
                //任选课不计入平均学分绩点
                continue;
            }
            scoreSum+=score.getTotal()*score.getCourse().getCredit();
            creditSum+=score.getCourse().getCredit();
        }
        return scoreSum/creditSum;
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

    public Teacher getCurrentTeacher() {
     return teacherRepository.findTeacherByUserId(getCurrentUser());
    }

    @PostMapping("/showScore")
    @PreAuthorize(("hasRole('STUDENT')"))
    //展示当前学期已有的成绩信息
    public DataResponse showScore(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Student student = getCurrentStudent();
        Integer studentId = getCurrentStudent().getId();
        List<Score> scoreByStudentId = scoreRepository.findScoreByStudentId(studentId);
        //逐个检查Score是否为当前学期,如果是，放入带查询列表
        List<Score> scoreInCurrentTerm = new ArrayList<>();
        for (Score score : scoreByStudentId) {
            Integer grade = score.getCourse().getGrade();
            Integer term = score.getCourse().getTerm();
            if(Objects.equals(student.getGrade(), grade) && Objects.equals(student.getTerm(), term)){
                scoreInCurrentTerm.add(score);
            }
        }
        //对当前学期的成绩计算排名,班级(同一个老师下的)平均分
        dataList=getReturnScore(scoreInCurrentTerm);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/generateElOption")
    @PreAuthorize(("hasRole('STUDENT')"))
    //展示当前学期已有的成绩信息
    public DataResponse generateElOption(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Student currentStudent = getCurrentStudent();
        Integer term = currentStudent.getTerm();
        Integer grade = currentStudent.getGrade();
        Integer sum = term + grade;

        for(int i=1;i<=grade;i++){
            for(int j=1;j<=2;j++){
                if(j>term&&i==grade){
                    break;
                }
                Map m = new HashMap();
                m.put("term",GetCourseYear(i,j));
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/scoreQuery")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse scoreQuery(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Map data = dataRequest.getData();
        String term = (String) data.get("term");
        String name = (String) data.get("name");
        //相当于没查
        if(Objects.equals(term, "")&& Objects.equals(name, "")){
            showScore(dataRequest);
        }
        //只查学期 要多放一个平均学分绩点
        else if(!Objects.equals(term, "") && Objects.equals(name, "")){
            int[] gradeAndTerm = GetCurrentTimeNumber(term);
            int grade = gradeAndTerm[0];
            int termNumber = gradeAndTerm[1];
            Integer studentId = getCurrentStudent().getId();
            //查询符合grade,termNumber,studentId的成绩
            List<Score> scoreByStudentIdAndGradeAndTerm = scoreRepository.findScoreByStudentIdAndGradeAndTerm(studentId, grade, termNumber);
            dataList=getReturnScore(scoreByStudentIdAndGradeAndTerm);
        }
        //只查课程名
        else if(Objects.equals(term, "") && !Objects.equals(name, "")){
            Integer studentId = getCurrentStudent().getId();
            List<Score> scoreByCourseName = scoreRepository.findScoreByCourseName(studentId,name);
            dataList=getReturnScore(scoreByCourseName);
        }
        else{
            Integer studentId = getCurrentStudent().getId();
            int[] gradeAndTerm = GetCurrentTimeNumber(term);
            int grade = gradeAndTerm[0];
            int termNumber = gradeAndTerm[1];
            List<Score> scoreByStudentIdAndGradeAndTermAndCourseName = scoreRepository.findScoreByStudentIdAndGradeAndTermAndCourseName(studentId, grade, termNumber, name);
            dataList=getReturnScore(scoreByStudentIdAndGradeAndTermAndCourseName);
        }

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/computeAverageGPA")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse computeAverageGPA(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Map data = dataRequest.getData();
        String term = (String) data.get("term");
        int[] gradeAndTerm = GetCurrentTimeNumber(term);
        Integer grade = gradeAndTerm[0];
        int termNumber = gradeAndTerm[1];
        double averageGPA = getAverageGPA(grade, termNumber);
        Map m = new HashMap();
        m.put("averageGPA",averageGPA);
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/uploadScoreExcel")
    @PreAuthorize((" hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse uploadExcel(@RequestParam Map pars, @RequestParam("file") MultipartFile file) throws IOException {
        String change = (String) pars.get("change");
        Integer courseId = Integer.parseInt((String) pars.get("id"));
        //如果点击的是更改
        if(Objects.equals(change, "true")){
            //先给之前的全删了
            List<Score> scoreByCourseId = scoreRepository.findScoreByCourseId(courseId);
            for (Score score : scoreByCourseId) {
                scoreRepository.delete(score);
            }
        }
        MultipartFile file1 = file;
        File fo=MultipartFileToFile(file);
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
                if(cell!=null){
                    cell.setCellType(CellType.STRING);
                }
                data.add(cell.toString());
            }
            dataList.add(data);
        }
        for (List<String> list : dataList) {
            String courseNum = list.get(0);
            String studentNum = list.get(1);
            String studentName = list.get(2);
            String regular = list.get(3);
            String exam = list.get(4);
            int courseNumInt,regularInt,examInt;
            //课序号有问题
            try{
                courseNumInt = Integer.parseInt(courseNum);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return CommonMethod.getReturnMessageError("PLZ check the courseNum column in you sheet.There's a NumberFormatException!");
            }
            //平时成绩有问题
            try{
                regularInt = Integer.parseInt(regular);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return CommonMethod.getReturnMessageError("PLZ check the regular column in you sheet.There's a NumberFormatException!");
            }
            //期末成绩有问题
            try{
                examInt = Integer.parseInt(exam);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return CommonMethod.getReturnMessageError("PLZ check the exam column in you sheet.There's a NumberFormatException!");
            }
            Student studentByStudentNumAndStudentName = studentRepository.findStudentByStudentNumAndStudentName(studentNum, studentName);
            //没这个学生
            if(studentByStudentNumAndStudentName==null){
                return CommonMethod.getReturnMessageError("PLZ check the num/name column in you sheet.The student doesn't exist!");
            }
            Score score = new Score();
            Integer maxId = scoreRepository.getMaxId();
            score.setId(maxId+1);
            score.setStudent(studentByStudentNumAndStudentName);
            score.setCourse(courseRepository.findCourseByCourseId(courseNumInt).get());
            score.setRegular(regularInt);
            score.setExam(examInt);
            scoreRepository.save(score);
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/showTeacherScore")
    @PreAuthorize((" hasRole('TEACHER') or hasRole('ADMIN')"))
    public DataResponse showTeacherScore(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Teacher currentTeacher = getCurrentTeacher();
        List<Course> courseByTeacherId = courseRepository.findCourseByTeacherId(currentTeacher.getId());
        for (Course course : courseByTeacherId) {
            boolean flag = scoreRepository.findScoreByCourseId(course.getId()).size()!=0; //flag 判断这门课录入成绩没有 true代表已录入
            Map m = new HashMap();
            m.put("id",course.getId());
            m.put("name",course.getCourseName());
            m.put("credit",course.getCredit());
            m.put("scope",course.getScope());
            m.put("place",course.getPlace());
            m.put("prop",course.getProp());
            m.put("grade",course.getGrade());
            m.put("time",GetCourseTime(course.getTime()));
            if(flag){
                m.put("checked","true");
            }
            else{
                m.put("checked","false");
            }
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

}
