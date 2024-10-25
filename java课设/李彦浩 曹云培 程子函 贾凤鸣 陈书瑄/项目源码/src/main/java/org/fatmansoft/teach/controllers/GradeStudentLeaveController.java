package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
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
public class GradeStudentLeaveController {
    @Autowired
    private MyLeaveRepository myLeaveRepository;
    @Autowired
    private GradeRepository gradeRepository;

    public List getAllLeaveMapList(Integer gradeId){
        List dataList = new ArrayList();

        Optional<Grade> grade = gradeRepository.findById(gradeId);
        Map m;
        List<MyLeave> AList = myLeaveRepository.findGradeLeaveListByGradeId(gradeId);
        Collections.sort(AList);
        //查出来之后以map的形式放入最终返回的数表中
        //前端要有对应的map
        String gradeStatisticParas;
        for(MyLeave s:AList){
            m = new HashMap();
            m.put("id", s.getId());
            m.put("gradenum",grade.get().getGradeNum());
            m.put("clazznum",s.getStudent().getClazz().getClassNum());
            m.put("outtime", DateTimeTool.parseDateTime(s.getOutTime(),"yyyy-MM-dd"));//外出时间
            m.put("backtime",DateTimeTool.parseDateTime(s.getBackTime(),"yyyy-MM-dd"));//返回时间
            m.put("reason",s.getReason());//外出原因
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());

            gradeStatisticParas="model=gradeStatistic&gradeId="+gradeId;
            m.put("gradeStatistic","返回");
            m.put("gradeStatisticParas",gradeStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/gradeStudentLeaveInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentLeaveInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer gradeId = dataRequest.getInteger("gradeId");
        List dataList = getAllLeaveMapList(gradeId);
        return CommonMethod.getReturnData(dataList);
    }

}
