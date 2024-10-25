package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Grade;
import org.fatmansoft.teach.models.Honor;
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

public class GradeStudentHonorController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private HonorRepository honorRepository;

    public List getAllHonorMapList(Integer gradeId){
        List dataList = new ArrayList();

        Optional<Grade> grade = gradeRepository.findById(gradeId);
        List<Honor> honorList = honorRepository.FindHonorByGradeIdNative(gradeId);
        if(honorList==null||honorList.size()==0){
            return dataList;
        }

        Map m;
        String titleParas,gradeStatisticParas;
        for(Honor h:honorList){
            m = new HashMap();
            //m.put("classNum",clazz.get().getClassNum());
            m.put("gradeNum",grade.get().getGradeNum());
            m.put("studentName",h.getStudent().getStudentName());
            m.put("title",h.getTitle());
            titleParas = "model=gradeStudentHonorDetail&honorId="+h.getId()+"&gradeId="+gradeId;
            m.put("title",h.getTitle());
            m.put("titleParas",titleParas);
            //m.put("reward",H.getReward());
            gradeStatisticParas="model=gradeStatistic&gradeId="+gradeId;
            m.put("gradeStatistic","返回");
            m.put("gradeStatisticParas",gradeStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/gradeStudentHonorInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHonorInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer gradeId = dataRequest.getInteger("gradeId");
        List dataList = getAllHonorMapList(gradeId);
        return CommonMethod.getReturnData(dataList);
    }

}
