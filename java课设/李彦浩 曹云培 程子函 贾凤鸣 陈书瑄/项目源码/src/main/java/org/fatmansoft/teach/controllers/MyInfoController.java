package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.StudentInfo;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentInfoRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyInfoController {
    private static Integer studentId;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentInfoRepository studentInfoRepository;

    public List getMyInfoListByStudentId(Integer Id) {
        List dataList = new ArrayList<>();
        List<StudentInfo> tempList = studentInfoRepository.FindStudentInfoByStudentIdNative(Id);
        Map m;
        for (StudentInfo s : tempList) {
            m = new HashMap();
            m.put("id", s.getId());
            m.put("majorNum", s.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName", s.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum", s.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum", s.getStudent().getClazz().getClassNum());
            m.put("studentNum", s.getStudent().getStudentNum());
            m.put("studentName", s.getStudent().getStudentName());
            m.put("hometown", s.getHometown());
            m.put("contact", s.getContact());
            m.put("political",s.getPolitical());
            m.put("major", s.getMajor());
            m.put("clazz", s.getClazz());
            m.put("ethnic", s.getEthnic());
            String StudentDetailsParas = "model=StudentDetails&studentId="+s.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/myInfoInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myInfoInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyInfoListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/myInfoEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myInfoEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        StudentInfo s = null;
        Optional<StudentInfo> op;

        if (id != null) {
            op = studentInfoRepository.findById(id);
            if (op.isPresent()) {
                s = op.get();
            }
        }

        Map m = new HashMap();
        if (s != null) {
            m.put("id", s.getId());
            //m.put("majorNum", s.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            //m.put("gradeNum", s.getStudent().getClazz().getGrade().getGradeNum());
            //m.put("classNum", s.getStudent().getClazz().getClassNum());
            //m.put("studentNum",s.getStudent().getStudentNum());
            //m.put("studentName", s.getStudent().getStudentName());
            m.put("hometown", s.getHometown());
            m.put("contact", s.getContact());
            m.put("political",s.getPolitical());
            m.put("ethnic", s.getEthnic());
        }
        return CommonMethod.getReturnData(m);
    }

    @PostMapping("/myInfoEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = dataRequest.getInteger("id");
        //Integer studentNum = CommonMethod.getInteger(form, "studentNum");
        String hometown= CommonMethod.getString(form,"hometown");
        String contact= CommonMethod.getString(form,"contact");
        String political= CommonMethod.getString(form,"political");
        String ethnic= CommonMethod.getString(form,"ethnic");
        StudentInfo mc = null;
        Optional<StudentInfo> op;
        if (id != null) {
            op = studentInfoRepository.findById(id);
            if (op.isPresent()) {
                mc = op.get();
            }
        }

        if (mc == null) {
            mc = new StudentInfo();
            id = studentInfoRepository.getMaxId();
            //设置主键
            if (id == null) {
                id = 1;
            } else {
                id = id + 1;
            }
            mc.setId(id);
        }
        Optional<Student> s = studentRepository.findById(studentId);
        Integer gradeId = null;
        if(s.isPresent()){
            gradeId = s.get().getClazz().getGrade().getId();
        }
        if (s.isPresent()) {
            Optional<StudentInfo> studentInfoCheck = studentInfoRepository.OPFindStudentInfoByStudentId(studentId);
            studentInfoCheck.ifPresent(studentInfo -> studentInfoRepository.delete(studentInfo));

                mc.setStudent(s.get());
                mc.setHometown(hometown);
                mc.setPolitical(political);
                mc.setContact(contact);
                mc.setEthnic(ethnic);
                studentInfoRepository.save(mc);

        } else {
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }
        return CommonMethod.getReturnData(mc.getId());
    }

    @PostMapping("/myInfoDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myInfoDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        StudentInfo mc = null;
        Optional<StudentInfo> op;

        if (id != null) {
            op = studentInfoRepository.findById(id);
            if (op.isPresent()) {
                mc = op.get();
            }
        }
        if (mc != null) {
            studentInfoRepository.delete(mc);
        }
        return CommonMethod.getReturnMessageOK();
    }
}
