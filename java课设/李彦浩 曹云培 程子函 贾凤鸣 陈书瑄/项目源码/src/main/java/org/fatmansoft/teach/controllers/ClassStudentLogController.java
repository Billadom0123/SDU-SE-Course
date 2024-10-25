package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
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
//上个页面回传的是班级id信息
//显示班级所有学生花销
public class ClassStudentLogController {
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private MyLogRepository myLogRepository;

    public List getAllLogMapList(Integer clazzId){
        List dataList = new ArrayList();

        Optional<Clazz> clazz = clazzRepository.findById(clazzId);//知道这个课程的在数据库里的id，可以得到对应的grade
        List<MyLog> myLogs = myLogRepository.findClassLogListByClazzId(clazzId);
        if(myLogs==null||myLogs.size()==0){
            return dataList;
        }
        Collections.sort(myLogs);
        Map m;
        String clazzStatisticParas;
        for(MyLog s:myLogs){
            m=new HashMap();
            m.put("id", s.getId());
            m.put("classnum",clazz.get().getClassNum());
            m.put("consume",s.getConsume());
            //在前端显示学生的姓名学号
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());
            m.put("consumeTime", DateTimeTool.parseDateTime(s.getConsumeTime(),"yyyy-MM-dd"));
            clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.get().getId();
            m.put("clazzStatistic","返回");
            m.put("clazzStatisticParas",clazzStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentLogInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentLogInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllLogMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

}
