package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import sun.misc.FpUtils;
//import sun.net.util.IPAddressUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class TotalInnovationController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public InnovationRepository innovationRepository;

    public List getTotalInnovationList(String innovationDescription){
        List dataList = new ArrayList();
        List<Innovation> inoList;
        inoList = innovationRepository.findStudentByPractice(innovationDescription);
        List<Student> sList = null;
        for(Innovation inn : inoList){
            Student s = inn.getStudent();
            sList.add(s);
        }
        Map m;
        String clazzParas;
        for(int i=0;i<sList.size();i++){
            Student s = sList.get(i);
            m = new HashMap();
            m.put("id",s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("studetnName",s.getStudentName());
            m.put("sex",s.getSex());
            m.put("age",s.getAge());
            m.put("major",s.getClazz().getGrade().getMajor().getMajorName());
            m.put("grade",s.getClazz().getGrade().getGradeNum());
            m.put("class",s.getClazz().getClassNum());

            m.put("clazz","返回");
            dataList.add(m);
        }

        return dataList;
    }

}
