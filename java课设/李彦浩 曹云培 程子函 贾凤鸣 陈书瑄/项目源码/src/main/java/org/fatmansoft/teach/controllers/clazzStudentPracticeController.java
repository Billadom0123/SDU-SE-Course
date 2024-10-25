package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyActivity;
import org.fatmansoft.teach.models.MyInnovation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.MyActivityRepository;
import org.fatmansoft.teach.repository.MyInnovationRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class clazzStudentPracticeController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyInnovationRepository myInnovationRepository;
    @Autowired
    public InnovationRepository innovationRepository;

    public List getClazzStudentPracticeMap(String practice){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> practiceList = myInnovationRepository.findMyInnovationByPractice(practice);
        if(practiceList.size()==0){

        }
        else{
            for(MyInnovation myInno:practiceList){
                m = new HashMap();
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("practice",myInno.getPractice().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentPracticeMap(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> practiceList = myInnovationRepository.findMyInnovationByclazzId(clazzId);
        if(practiceList.size()==0){

        }
        else{
            for(MyInnovation myInno:practiceList){
                m = new HashMap();
                if(myInno.getPractice()==null) continue;
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("practice",myInno.getPractice().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    @PostMapping("/clazzStudentPracticeInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPracticeInit(@Valid@RequestBody DataRequest dataRequest){
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentPracticeMap(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentPracticeQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPracticeQuery(@Valid @RequestBody DataRequest dataRequest) {
        String practice = dataRequest.getString("practice");
        List dataList = getClazzStudentPracticeMap(practice);

        return CommonMethod.getReturnData(dataList);
    }
}
