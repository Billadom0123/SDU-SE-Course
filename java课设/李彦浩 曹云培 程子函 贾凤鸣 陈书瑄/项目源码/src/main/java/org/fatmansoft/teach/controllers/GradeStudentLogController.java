package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Grade;
import org.fatmansoft.teach.models.MyLog;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.fatmansoft.teach.repository.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
//上个页面回传的年级id信息
//显示班级所有学生花销
public class GradeStudentLogController {
    @Autowired
    private MyLogRepository myLogRepository;
    @Autowired
    private GradeRepository gradeRepository;

    public List getAllLogMapList(Integer gradeId){
        List dataList = new ArrayList();

        Optional<Grade> grade = gradeRepository.findById(gradeId);
        List<MyLog> myLogs = myLogRepository.findClassLogListByGradeId(gradeId);
        if(myLogs==null||myLogs.size()==0){
            return dataList;
        }
        Collections.sort(myLogs);
        Map m;
        String gradeStatisticParas;
        for(MyLog s:myLogs){
            m=new HashMap();
            m.put("id", s.getId());
            m.put("gradenum",grade.get().getGradeNum());
            m.put("clazznum",s.getStudent().getClazz().getClassNum());
            m.put("consume",s.getConsume());
            //在前端显示学生的姓名学号
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());
            m.put("consumeTime", DateTimeTool.parseDateTime(s.getConsumeTime(),"yyyy-MM-dd"));
            gradeStatisticParas="model=gradeStatistic&gradeId="+gradeId;
            m.put("gradeStatistic","返回");
            m.put("gradeStatisticParas",gradeStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/gradeStudentLogInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentLogInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer gradeId = dataRequest.getInteger("gradeId");
        List dataList = getAllLogMapList(gradeId);
        return CommonMethod.getReturnData(dataList);
    }

}
