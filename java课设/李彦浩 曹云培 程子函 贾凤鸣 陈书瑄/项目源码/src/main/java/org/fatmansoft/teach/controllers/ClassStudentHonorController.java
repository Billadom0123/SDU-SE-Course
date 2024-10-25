package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Honor;
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
@RequestMapping("/api/teach")
public class ClassStudentHonorController {
    private static Integer clazzId;
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

    //上一个页面传下来的是班级id
    public List getAllHonorMapList(Integer clazzId){
        List dataList = new ArrayList();
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        List<Honor> honorList = honorRepository.FindHonorByClazzIdNative(clazzId);
        //也许要添加遍历，取出所有的荣誉信息
        //测试注意会不会有重复输出荣誉信息的情况
        if(honorList==null||honorList.size()==0){
            return dataList;
        }

        Map m;
        String titleParas,clazzStatisticParas;
        for(Honor H:honorList){
            m = new HashMap();
            m.put("classNum",H.getStudent().getClazz().getClassNum());
            //m.put("studentNum",H.getStudent().getStudentNum());
            m.put("studentName",H.getStudent().getStudentName());
            m.put("title",H.getTitle());
            titleParas = "model=classStudentHonorDetail&honorId="+H.getId()+"&clazzId="+clazz.get().getId();
            m.put("title",H.getTitle());
            m.put("titleParas",titleParas);
            //m.put("reward",H.getReward());
            clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.get().getId();
            m.put("clazzStatistic","返回");
            m.put("clazzStatisticParas",clazzStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentHonorInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentHonorInit(@Valid @RequestBody DataRequest dataRequest) {
        clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllHonorMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

}
