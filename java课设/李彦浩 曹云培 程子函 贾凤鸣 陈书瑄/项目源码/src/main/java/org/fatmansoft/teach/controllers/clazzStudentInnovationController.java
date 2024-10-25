package org.fatmansoft.teach.controllers;

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
public class clazzStudentInnovationController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MyInnovationRepository myInnovationRepository;
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;

    public List getAllInnovationMapList(Integer clazzId) {
        List dataList = new ArrayList();
        Map m = new HashMap();
        String clazzStudentPracticeParas,competitionParas,sciAchieveParas,inoProjectParas,lectureParas,internshipParas,clazzStatisticParas;
        clazzStudentPracticeParas = "model=clazzStudentPractice&clazzId="+clazzId;
        m.put("clazzStudentPractice","社会实践");
        m.put("clazzStudentPracticeParas",clazzStudentPracticeParas);

        competitionParas = "model=clazzStudentCompetition&clazzId="+clazzId;
        m.put("competition","学科竞赛");
        m.put("competitionParas",competitionParas);
        sciAchieveParas = "model=clazzStudentSciAchieve&clazzId="+clazzId;
        m.put("sciAchieve","科研成果");
        m.put("sciAchieveParas",sciAchieveParas);
        inoProjectParas = "model=clazzStudentInoProject&clazzId="+clazzId;
        m.put("inoProject","创新项目");
        m.put("inoProjectParas",inoProjectParas);
        lectureParas = "model=clazzStudentLecture&clazzId="+clazzId;
        m.put("lecture","讲座");
        m.put("lectureParas",lectureParas);
        internshipParas = "model=clazzStudentInternship&clazzId="+clazzId;
        m.put("internship","实习经历");
        m.put("internshipParas",internshipParas);


        clazzStatisticParas = "model=clazzStatistic&clazzId="+clazzId;
        m.put("clazzStatistic","返回");
        m.put("clazzStatisticParas",clazzStatisticParas);

        dataList.add(m);
        return dataList;
    }
    @PostMapping("/clazzStudentInnovationInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse clazzStudentInnovationInit(@Valid@RequestBody DataRequest dataRequest){
        Integer clazzId = dataRequest.getInteger("clazzId");
        List dataList = getAllInnovationMapList(clazzId);
        return CommonMethod.getReturnData(dataList);
    }
}
