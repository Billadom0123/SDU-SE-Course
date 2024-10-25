package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.MyLeave;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ClazzRepository;
import org.fatmansoft.teach.repository.MyLeaveRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class ClassStudentLeaveController {
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private MyLeaveRepository myLeaveRepository;

    public List getAllLogMapList(Integer clazzId){
        List dataList = new ArrayList();

        Optional<Clazz> clazz = clazzRepository.findById(clazzId);//知道这个课程的在数据库里的id，可以得到对应的grade
        List<MyLeave> myLeaveList = myLeaveRepository.findClassLeaveListByClazzId(clazzId);
        if(myLeaveList==null||myLeaveList.size()==0){
            return dataList;
        }
        Collections.sort(myLeaveList);
        Map m;
        String clazzStatisticParas;
        for(MyLeave s:myLeaveList){
            m=new HashMap();
            m.put("id", s.getId());
            m.put("classnum",clazz.get().getClassNum());
            //在前端显示学生的姓名学号
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());
            m.put("outtime", DateTimeTool.parseDateTime(s.getOutTime(),"yyyy-MM-dd"));//外出时间
            m.put("backtime",DateTimeTool.parseDateTime(s.getBackTime(),"yyyy-MM-dd"));//返回时间
            m.put("reason",s.getReason());
            clazzStatisticParas="model=clazzStatistic&clazzId="+clazz.get().getId();
            m.put("clazzStatistic","返回");
            m.put("clazzStatisticParas",clazzStatisticParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/classStudentLeaveInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse classStudentLeaveInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllLogMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

}
