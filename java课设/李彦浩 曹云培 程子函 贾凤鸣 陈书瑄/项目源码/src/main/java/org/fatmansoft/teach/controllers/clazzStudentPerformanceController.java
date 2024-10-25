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
public class clazzStudentPerformanceController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyActivityRepository myActivityRepository;

    public List getClazzStudentPerformanceMap(String performance){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> performanceList = myActivityRepository.findMyActivityByPerformance(performance);
        if(performanceList.size()==0){

        }
        else{
            for(MyActivity perform:performanceList){
                m = new HashMap();
                Student stu = perform.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("performance",perform.getPerformance().toString());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentPerformanceMapByClazzId(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> performanceList = myActivityRepository.FindMyActivityByClazzIdNative(clazzId);
        if(performanceList.size()==0){

        }
        else{
            for(MyActivity perform:performanceList){
                m = new HashMap();
                if(perform.getPerformance()==null) continue;
                Student stu = perform.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("performance",perform.getPerformance());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    @PostMapping("/clazzStudentPerformanceInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPerformanceInit(@Valid@RequestBody DataRequest dataRequest){
        Integer  clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentPerformanceMapByClazzId(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentPerformanceQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPerformanceQuery(@Valid @RequestBody DataRequest dataRequest) {
        String performance = dataRequest.getString("performance");
        List dataList = getClazzStudentPerformanceMap(performance);
        return CommonMethod.getReturnData(dataList);
    }
}
