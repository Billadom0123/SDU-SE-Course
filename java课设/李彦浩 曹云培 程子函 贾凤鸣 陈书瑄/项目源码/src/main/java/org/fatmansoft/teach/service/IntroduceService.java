package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

@Service
public class IntroduceService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private MyCourseRepository myCourseRepository;
    @Autowired
    private StudentInfoRepository studentInfoRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private MyInnovationRepository myInnovationRepository;
    @Autowired
    private MyActivityRepository myActivityRepository;

    //个人简历信息数据准备方法  请同学修改这个方法，请根据自己的数据的希望展示的内容拼接成字符串，放在Map对象里，attachList 可以方多段内容，具体内容有个人决定
    public Map getIntroduceDataMap(Integer studentId){
        Map data = new HashMap();
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isPresent()){
            Student s = student.get();
            data.put("myName",student.get().getStudentName());   // 学生信息
            String sex;
            if(s.getSex().equals("1")){
                sex="男";
            }
            else{
                sex="女";
            }
            data.put("overview",s.getClazz().getGrade().getMajor().getMajorName()+" "+s.getClazz().getGrade().getGradeNum()+"年级 "+s.getClazz().getClassNum()+"班 "+s.getStudentNum()+"号 "+s.getStudentName()+" "+sex+" "+s.getAge()+"岁 "+" 生日:"+s.getBirthday());  //学生基本信息综述
            List attachList = new ArrayList();
            Map m;

            m=new HashMap();
            m.put("title","其他基本信息");
            Optional<StudentInfo> studentInfo = studentInfoRepository.OPFindStudentInfoByStudentId(studentId);
            StudentInfo temp;
            if(studentInfo.isPresent()){
                temp = studentInfo.get();
                m.put("content","联系方式:"+temp.getContact()+" 籍贯:"+temp.getHometown()+" 民族:"+temp.getEthnic()+" 政治面貌:"+temp.getPolitical());
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            List<Achievement> achievementList = achievementRepository.FindScoreByStudentIdNative(studentId);
            m.put("title","学习成绩");
            String achievementInfo="";
            if(achievementList.size()==0) {
                m.put("content", "无");
            }
            else{
                for(Achievement achievement:achievementList){
                    achievementInfo+=achievement.getCourse().getCourseName()+" "+achievement.getScore()+" ";
                }
                m.put("content",achievementInfo);
            }
            attachList.add(m);

            m=new HashMap();
            m.put("title","平均学分绩点");
            List<Homework> homeworkList = homeworkRepository.findHomeworkByStudentId(studentId);
            List<MyCourse> myCourseList = myCourseRepository.findMyCourseByStudentId(studentId);
            if(myCourseList==null||myCourseList.size()==0){
                m.put("content","无,该学生没有选择必修课程");
            }
            else{
                //计算公式:课程成绩乘以课程学分加和后除以课程总学分
                Double ScoreSum=0.0;
                Double creditSum=0.0;
                for(Homework h:homeworkList){
                    ScoreSum+=h.getHomeworkScore()*0.3*h.getCourse().getCredit();
                }
                for(Achievement achievement:achievementList){
                    ScoreSum+=achievement.getScore()*0.7*achievement.getCourse().getCredit();
                }
                for(MyCourse myCourse:myCourseList){
                    creditSum+=myCourse.getCourse().getCredit();
                }
                m.put("content",String.format("%.2f",ScoreSum/creditSum));
            }
            attachList.add(m);

            m=new HashMap();
            m.put("title","荣誉称号");
            List<Honor> honorList = honorRepository.FindHonorByStudentIdNative(studentId);
            if(honorList==null||honorList.size()==0){
                m.put("content","无");
            }
            else{
                String honorString="";
                for(Honor honor:honorList){
                    honorString+=honor.getTitle()+" ";
                }
                m.put("content",honorString);
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","社会实践");
            List<MyInnovation> myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            if(myInnovationList.size()!=0){
                String myInnovationString = "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getPractice()==null) continue;//
                    myInnovationString+=myInnovation.getPractice()+" ";
                }
                m.put("content",myInnovationString);  // 社会实践综述
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","学科竞赛");
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            String myInnovationString;
            if(myInnovationList.size()!=0){
                myInnovationString= "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getCompetition()==null) continue;
                    myInnovationString+=myInnovation.getCompetition()+" ";
                }
                m.put("content",myInnovationString);
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","创新项目");
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            if(myInnovationList.size()!=0){
                myInnovationString= "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getInoProject()==null) continue;
                    myInnovationString+=myInnovation.getInoProject()+" ";
                }
                m.put("content",myInnovationString);
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","实习经历");
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            if(myInnovationList.size()!=0){
                myInnovationString= "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getInternship()==null) continue;
                    myInnovationString+=myInnovation.getInternship()+" ";
                }
                m.put("content",myInnovationString);
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","科研成果");
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            if(myInnovationList.size()!=0){
                myInnovationString= "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getSciAchieve()==null) continue;
                    myInnovationString+=myInnovation.getSciAchieve()+" ";
                }
                m.put("content",myInnovationString);
            }
            else{
                m.put("content","无");
            }
            attachList.add(m);

            m = new HashMap();
            m.put("title","讲座");
            myInnovationList = myInnovationRepository.findMyInnovationByStudentId(studentId);
            if(myInnovationList.size()!=0){
                myInnovationString= "";
                for(MyInnovation myInnovation:myInnovationList){
                    if(myInnovation.getLecture()==null) continue;
                    myInnovationString+=myInnovation.getLecture()+" ";
                }
                m.put("content",myInnovationString);
            }
           else{
               m.put("content","无");
            }
            attachList.add(m);

            data.put("attachList",attachList);
        }

        return data;
    }
}
