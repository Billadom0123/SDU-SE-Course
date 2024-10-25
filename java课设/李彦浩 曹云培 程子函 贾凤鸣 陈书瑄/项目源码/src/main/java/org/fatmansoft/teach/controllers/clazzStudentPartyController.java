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
public class clazzStudentPartyController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyActivityRepository myActivityRepository;

    public List getClazzStudentPartyMap(String party){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> partyList = myActivityRepository.findMyActivityByParty(party);
        if(partyList.size()==0){

        }
        else{
            for(MyActivity par:partyList){
                m = new HashMap();
                Student stu = par.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("party",par.getParty().toString());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentPartyMapByClazzId(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentActivityParas;
        List<MyActivity> partyList = myActivityRepository.FindMyActivityByClazzIdNative(clazzId);
            for(MyActivity par:partyList){
                m = new HashMap();
                if(par.getParty()==null) continue;
                Student stu = par.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("party",par.getParty().toString());
                clazzStudentActivityParas = "model=clazzStudentActivity&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentActivity","返回");
                m.put("clazzStudentActivityParas",clazzStudentActivityParas);
                dataList.add(m);
        }

        return dataList;
    }
    @PostMapping("/clazzStudentPartyInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPartyInit(@Valid@RequestBody DataRequest dataRequest){
        Integer  clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentPartyMapByClazzId(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentPartyQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentPartyQuery(@Valid @RequestBody DataRequest dataRequest) {
        String party = dataRequest.getString("party");
        List dataList = getClazzStudentPartyMap(party);
        return CommonMethod.getReturnData(dataList);
    }
}
