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
public class clazzStudentSciAchieveController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyInnovationRepository myInnovationRepository;
    @Autowired
    public InnovationRepository innovationRepository;

    public List getClazzStudentSciAchieveMap(String sciAchieve){
        System.out.println(sciAchieve);
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> sciAchieveList = myInnovationRepository.findMyInnovationBySciAchieve(sciAchieve);
        if(sciAchieveList.size()==0){

        }
        else{
            for(MyInnovation myInno:sciAchieveList){
                m = new HashMap();
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("sciAchieve",myInno.getSciAchieve().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentSciAchieveMap(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> sciAchieveList = myInnovationRepository.findMyInnovationByclazzId(clazzId);
        if(sciAchieveList.size()==0){

        }
        else{
            for(MyInnovation myInno:sciAchieveList){
                m = new HashMap();
                if(myInno.getSciAchieve()==null) continue;
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("sciAchieve",myInno.getSciAchieve().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    @PostMapping("/clazzStudentSciAchieveInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentSciAchieveInit(@Valid@RequestBody DataRequest dataRequest){
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentSciAchieveMap(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentSciAchieveQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentSciAchieveQuery(@Valid @RequestBody DataRequest dataRequest) {
        String sciAchieve = dataRequest.getString("sciAchieve");
        System.out.println(sciAchieve);
        List dataList = getClazzStudentSciAchieveMap(sciAchieve);
        return CommonMethod.getReturnData(dataList);
    }
}
