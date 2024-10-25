package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyActivity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.MyActivityRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Style;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class clazzStudentPEController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyActivityRepository myActivityRepository;

    public List getClazzStudentPEMap(String PE){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        //参数传不进来
        List<MyActivity> PEList = myActivityRepository.findMyActivityByPE(PE);
        if(PEList.isEmpty()){

        }
        else{
            for(MyActivity pe:PEList){
                m = new HashMap();
                Student stu = pe.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("PE",PE);
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentPEMapByClazzId(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        //参数传不进来
        List<MyActivity> PEList = myActivityRepository.FindMyActivityByClazzIdNative(clazzId);
            for(MyActivity pe:PEList){
                m = new HashMap();
                if(pe.getPE()==null) continue;
                Student stu = pe.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("PE",pe.getPE());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
        }

        return dataList;
    }
    @PostMapping("/clazzStudentPEInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPEInit(@Valid@RequestBody DataRequest dataRequest){
        Integer  clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentPEMapByClazzId(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentPEQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPEQuery(@Valid @RequestBody DataRequest dataRequest) {
        String PE = dataRequest.getString("PE");
        List dataList = getClazzStudentPEMap(PE);
        System.out.println(PE);
        return CommonMethod.getReturnData(dataList);
    }
}
