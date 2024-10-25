package org.fatmansoft.teach.controllers;

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
public class ClazzStudentActivityController {
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

    public List getAllActivityMapList(Integer clazzId) {
        List dataList = new ArrayList();
        Map m = new HashMap();
        String clazzStudentPEParas,performanceParas,travelParas,partyParas,clazzStatisticParas;
        clazzStudentPEParas = "model=clazzStudentPE&clazzId="+clazzId;
        m.put("clazzStudentPE","体育活动");
        m.put("clazzStudentPEParas",clazzStudentPEParas);

        performanceParas = "model=clazzStudentPerformance&clazzId="+clazzId;
        m.put("performance","文艺表演");
        m.put("performanceParas",performanceParas);
        travelParas = "model=clazzStudentTravel&clazzId="+clazzId;
        m.put("travel","研学旅行");
        m.put("travelParas",travelParas);
        partyParas = "model=clazzStudentParty&clazzId="+clazzId;
        m.put("party","聚会活动");
        m.put("partyParas",partyParas);

        clazzStatisticParas = "model=clazzStatistic&clazzId="+clazzId;
        m.put("clazzStatistic","返回");
        m.put("clazzStatisticParas",clazzStatisticParas);

        dataList.add(m);
        return dataList;
    }
    @PostMapping("/clazzStudentActivityInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentActivityInit(@Valid@RequestBody DataRequest dataRequest){
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllActivityMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }
}
