package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
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
@RequestMapping("/api/teach")
public class MyActivityController {
    private static  Integer studentId;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MyActivityRepository myActivityRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;

    public List getMyActivityListByStudentId(Integer Id) {
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        List<MyActivity> tempList = myActivityRepository.findMyActivityByStudentId(Id);

        Map m;
        String StudentDetailsParas,myPEParas,myPerformanceParas,myPartyParas,myTravelParas;
        m = new HashMap();
        myPEParas = "model=myPE&studentId="+studentId;
        m.put("myPE", "我的体育运动");
        m.put("myPEParas",myPEParas);
        myPerformanceParas = "model=myPerformance&studentId="+studentId;
        m.put("myPerformance", "我的文艺表演");
        m.put("myPerformanceParas",myPerformanceParas);

        myPartyParas="model=myParty&studentId="+studentId;
        m.put("myParty","我的聚会");
        m.put("myPartyParas",myPartyParas);

        myTravelParas="model=myTravel&studentId="+studentId;
        m.put("myTravel", "我的研学旅行");
        m.put("myTravelParas",myTravelParas);

        StudentDetailsParas = "model=StudentDetails&studentId=" + studentId;
        m.put("StudentDetails", "返回");
        m.put("StudentDetailsParas", StudentDetailsParas);
        dataList.add(m);

        return dataList;
    }

    @PostMapping("/myActivityInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myActivityInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        List<MyActivity> dataList = getMyActivityListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }
}
