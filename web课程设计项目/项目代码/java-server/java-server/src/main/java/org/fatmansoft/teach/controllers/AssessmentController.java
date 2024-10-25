package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Assessment;
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
@RequestMapping("/api/assessment")
public class AssessmentController {
    @Autowired
    private StudentRepository studentRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    //查询评价频率前三的词
    public String[] getRank3(){
        Integer studentId = getCurrentStudent().getId();
        //把当前学生得到的所有评价全部找出来
        List<Assessment> assessmentByReceive = assessmentRepository.findAssessmentByReceive(studentId);
        //遍历次数放入map中
        Map<String,Integer> rank = new HashMap<>();
        for (Assessment assessment : assessmentByReceive) {
            //没有这个词
            if(rank.get(assessment.getDescribe())==null){
                rank.put(assessment.getDescribe(),1);
            }
            //有这个词就+1
            else{
                rank.put(assessment.getDescribe(),rank.get(assessment.getDescribe())+1);
            }
        }
        String[] result=new String[3];
        //遍历map,更新最大值
        int count=0;
        String max="";
        for(int i=0;i<3;i++) {
            for (String describe : rank.keySet()) {
                if(rank.get(describe)>count){
                    count = rank.get(describe);
                    max=describe;
                }
            }
            result[i]=max;//选取该字符串
            rank.remove(max);//map中删掉这个已经选取出的字符串
            max="";//如果评价甚至凑不满三种，比如两种。最后删除的一个max会保留
            count=0;//最大值计数器置零
        }
        return result;
    }

    @PostMapping("/showWordCloud")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showWordCloud(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        String[] rank3 = getRank3();

        for (String s : rank3) {
            Map m = new HashMap();
            //根据评价用语查询
            List<Assessment> assessmentByAccurateDescribe = assessmentRepository.findAssessmentByDescribeAndAndReceive(s,getCurrentStudent().getId());
            String names="";
            //不要把名字全拼上去
            int count=0;
            for (Assessment assessment : assessmentByAccurateDescribe) {
                if(count==3){
                    names+="等"+assessmentByAccurateDescribe.size()+"人,";
                    break;
                }
                names+=assessment.getDeliver().getStudentName()+",";
                count++;
            }
            if(names.length()==0){
                m.put("names","");
            }
            else{
                m.put("names",names.substring(0,names.length()-1));
            }
            m.put("assessment",s);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showAssessment")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showAssessment(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        Integer studentId = getCurrentStudent().getId();
        List<Assessment> checkedFalse = assessmentRepository.findAssessmentByReceiveAndChecked(studentId,"false");
        List<Assessment> checkedTrue = assessmentRepository.findAssessmentByReceiveAndChecked(studentId, "true");
        for (Assessment assessment : checkedFalse) {
            Map m = new HashMap();
                m.put("name",assessment.getDeliver().getStudentName());
                m.put("assessment",assessment.getDescribe());
                m.put("checked",assessment.getChecked());
                dataList.add(m);
        }
        for (Assessment assessment : checkedTrue) {
            Map m = new HashMap();
            m.put("name",assessment.getDeliver().getStudentName());
            m.put("assessment",assessment.getDescribe());
            m.put("checked",assessment.getChecked());
            dataList.add(m);
        }

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/changeChecked")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse changeChecked(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = getCurrentStudent().getId();
        List<Assessment> assessmentByReceive = assessmentRepository.findAssessmentByReceive(studentId);
        for (Assessment assessment : assessmentByReceive) {
            Assessment assessment1 = new Assessment();
            assessment1.setId(assessment.getId());
            assessment1.setDescribe(assessment.getDescribe());
            assessment1.setChecked("true");
            assessment1.setDeliver(assessment.getDeliver());
            assessment1.setReceive(assessment.getReceive());
            assessmentRepository.save(assessment1);
        }
        return CommonMethod.getReturnMessageOK();
    }


    @PostMapping("/showRecentDeliver")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showRecentDeliver(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer studentId = getCurrentStudent().getId();
        List<Assessment> assessmentByReceive = assessmentRepository.findAssessmentByReceive(studentId);
        Set<Student> s = new HashSet();
        for (Assessment assessment : assessmentByReceive) {
            s.add(assessment.getDeliver());
        }
        int count=0;
        for (Student student : s) {
            if(count==10){
                break;
            }
            Map m = new HashMap();
            m.put("id",student.getId());
            m.put("num",student.getStudentNum());
            m.put("name",student.getStudentName());
            dataList.add(m);
            count++;
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showRecentReceive")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showRecentReceive(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer studentId = getCurrentStudent().getId();
        List<Assessment> assessmentByDeliver = assessmentRepository.findAssessmentByDeliver(studentId);
        Set<Student> s = new HashSet<>();
        for (Assessment assessment : assessmentByDeliver) {
            s.add(assessment.getReceive());
        }
        int count=0;
        for (Student student : s) {
            if(count==10){
                break;
            }
            Map m = new HashMap();
            m.put("id",student.getId());
            m.put("num",student.getStudentNum());
            m.put("name",student.getStudentName());
            dataList.add(m);
            count++;
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/searchStudent")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse searchStudent(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Map data = dataRequest.getData();
        String numname = (String) data.get("numname");
        List<Student> studentListByNumName = studentRepository.findStudentListByNumName(numname);
        for (Student student : studentListByNumName) {
            if (student.getId()==getCurrentStudent().getId()){
                continue;
            }
            Map m = new HashMap();
            m.put("id",student.getId());
            m.put("num",student.getStudentNum());
            m.put("name",student.getStudentName());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/addAssessment")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse addAssessment(@Valid @RequestBody DataRequest dataRequest){
        String tags = (String) dataRequest.getData().get("tags");
        Integer id = (Integer) dataRequest.getData().get("id");
        Integer studentId = getCurrentStudent().getId();
        String[] tag = tags.split("/");
        for (String s : tag) {
            Assessment assessment = new Assessment();
            assessment.setId(assessmentRepository.getMaxId()+1);
            assessment.setReceive(studentRepository.getById(id));
            assessment.setDeliver(getCurrentStudent());
            assessment.setDescribe(s);
            assessment.setChecked("false");
            assessmentRepository.save(assessment);
        }
        return CommonMethod.getReturnData("success");
    }

    @PostMapping("/showUnChecked")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showUnChecked(@Valid @RequestBody DataRequest dataRequest){
        Map m = new HashMap();
        int unChecked=0;
        List<Assessment> assessmentByReceive = assessmentRepository.findAssessmentByReceive(getCurrentStudent().getId());
        for (Assessment assessment : assessmentByReceive) {
            if(Objects.equals(assessment.getChecked(), "false")){
                unChecked++;
            }
        }
        m.put("unChecked",unChecked);
        return CommonMethod.getReturnData(m);
    }
}
