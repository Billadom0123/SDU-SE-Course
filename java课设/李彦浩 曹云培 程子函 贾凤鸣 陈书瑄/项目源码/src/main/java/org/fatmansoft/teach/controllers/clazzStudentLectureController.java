package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyActivity;
import org.fatmansoft.teach.models.MyInnovation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.MyActivityRepository;
import org.fatmansoft.teach.repository.MyInnovationRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class clazzStudentLectureController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public MyInnovationRepository myInnovationRepository;
    @Autowired
    public InnovationRepository innovationRepository;

    public List getClazzStudentLectureMap(String lecture){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> lectureList = myInnovationRepository.findMyInnovationByLecture(lecture);
        if(lectureList.size()==0){

        }
        else{
            for(MyInnovation myInno:lectureList){
                m = new HashMap();
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("lecture",myInno.getLecture().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    public List getClazzStudentLectureMap(Integer clazzId){
        List dataList = new ArrayList();
        Map m ;
        String clazzStudentInnovationParas;
        //参数传不进来
        List<MyInnovation> lectureList = myInnovationRepository.findMyInnovationByclazzId(clazzId);
        if(lectureList.size()==0){

        }
        else{
            for(MyInnovation myInno:lectureList){
                m = new HashMap();
                if(myInno.getLecture()==null) continue;
                Student stu = myInno.getStudent();
                m.put("studentNum",stu.getStudentNum());
                m.put("studentName",stu.getStudentName());
                m.put("sex",stu.getSex());
                m.put("age",stu.getAge());
                m.put("majorName",stu.getClazz().getGrade().getMajor().getMajorName());
                m.put("gradeNum",stu.getClazz().getGrade().getGradeNum());
                m.put("classNum",stu.getClazz().getClassNum());
                m.put("lecture",myInno.getLecture().toString());
                clazzStudentInnovationParas = "model=clazzStudentInnovation&clazzId="+stu.getClazz().getClassNum();
                m.put("clazzStudentInnovation","返回");
                m.put("clazzStudentInnovationParas",clazzStudentInnovationParas);
                dataList.add(m);
            }
        }

        return dataList;
    }
    @PostMapping("/clazzStudentLectureInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentLectureInit(@Valid@RequestBody DataRequest dataRequest){
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getClazzStudentLectureMap(clazzId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/clazzStudentLectureQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentLectureQuery(@Valid @RequestBody DataRequest dataRequest) {
        String lecture = dataRequest.getString("lecture");
        List dataList = getClazzStudentLectureMap(lecture);

        return CommonMethod.getReturnData(dataList);
    }
}
