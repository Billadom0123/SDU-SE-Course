package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
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

public class MyInnovationController {
    private static Integer studentId;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private MyInnovationRepository myInnovationRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;

    public List getMyInnovationListByStudentId(Integer Id) {
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        List<MyInnovation> tempList = myInnovationRepository.findMyInnovationByStudentId(Id);

        Map m;
        String StudentDetailsParas,myPracticeParas,myCompetitionParas,mySciAchieveParas,myInoProjectParas,myLectureParas,myInternshipParas;

        m = new HashMap();
        myPracticeParas="model=myPractice&studentId="+studentId;
        m.put("myPractice","我的社会实践");
        m.put("myPracticeParas",myPracticeParas);
        myCompetitionParas="model=myCompetition&studentId="+studentId;
        m.put("myCompetition","我的学科竞赛");
        m.put("myCompetitionParas",myCompetitionParas);
        mySciAchieveParas="model=mySciAchieve&studentId="+studentId;
        m.put("mySciAchieve","我的科研成果");
        m.put("mySciAchieveParas",mySciAchieveParas);
        myInoProjectParas="model=myInoProject&studentId="+studentId;
        m.put("myInoProject","我的创新项目");
        m.put("myInoProjectParas",myInoProjectParas);
        myLectureParas="model=myLecture&studentId="+studentId;
        m.put("myLecture","我的讲座");
        m.put("myLectureParas",myLectureParas);
        myInternshipParas="model=myInternship&studentId="+studentId;
        m.put("myInternship","我的实习经历");
        m.put("myInternshipParas",myInternshipParas);
        StudentDetailsParas = "model=StudentDetails&studentId=" + studentId;
        m.put("StudentDetails", "返回");
        m.put("StudentDetailsParas", StudentDetailsParas);
        dataList.add(m);


        return dataList;
    }

    @PostMapping("/myInnovationInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myInnovationInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        System.out.println(studentId);
        List dataList = getMyInnovationListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }
}