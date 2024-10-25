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
import org.springframework.data.relational.core.sql.In;
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
public class clazzStudentTravelController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyActivityRepository myActivityRepository;

    public List getClazzStudentTravelMap(String travel){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> travelList = myActivityRepository.findMyActivityByTravel(travel);
        if(travelList.size()==0){

        }
        else{
            for(MyActivity pe:travelList){
                m = new HashMap();
                Student stu = pe.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("travel",pe.getTravel().toString());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentTravelMapByClazzId(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> travelList = myActivityRepository.FindMyActivityByClazzIdNative(clazzId);
        if(travelList.size()==0){

        }
        else{
            for(MyActivity pe:travelList){
                m = new HashMap();
                if(pe.getTravel()==null) continue;
                Student stu = pe.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("travel",pe.getTravel());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    @PostMapping("/clazzStudentTravelInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentTravelInit(@Valid@RequestBody DataRequest dataRequest){
        Integer  clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentTravelMapByClazzId(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentTravelQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentTravelQuery(@Valid @RequestBody DataRequest dataRequest) {
        String travel = dataRequest.getString("travel");
        List dataList = getClazzStudentTravelMap(travel);
        return CommonMethod.getReturnData(dataList);
    }
}
