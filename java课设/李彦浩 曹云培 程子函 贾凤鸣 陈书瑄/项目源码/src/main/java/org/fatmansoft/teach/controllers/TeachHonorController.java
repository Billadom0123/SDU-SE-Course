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

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class TeachHonorController {
    private static Integer studentId;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;


    //根据学生Id获取所有的MyCourse
    public List getHonorListByStudentId(Integer Id){
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        List<Honor> tempList = honorRepository.FindHonorByStudentIdNative(Id);

        Map m;
        String StudentDetailsParas;
        for(Honor h:tempList){
            m = new HashMap();
            m.put("id",h.getId());
            m.put("majorNum",h.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",h.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum",h.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum",h.getStudent().getClazz().getClassNum());
            m.put("studentNum",h.getStudent().getStudentNum());
            m.put("studentName",h.getStudent().getStudentName());
            m.put("title",h.getTitle());
            m.put("reward",h.getReward());
            StudentDetailsParas = "model=StudentDetails&studentId="+h.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/honorInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse honorInit(@Valid @RequestBody DataRequest dataRequest){
        studentId = dataRequest.getInteger("studentId");
        List dataList = getHonorListByStudentId(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/honorEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse honorEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Honor h = null;
        Optional<Honor> op;

        if(id!=null){
            op = honorRepository.findById(id);
            if(op.isPresent()){
                h = op.get();
            }
        }

        Map form = new HashMap();
        if(h!=null){
            form.put("id",h.getId());
            form.put("title",h.getTitle());
            form.put("reward",h.getReward());
        }

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/honorEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse honorEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String title=CommonMethod.getString(form,"title");
        String reward=CommonMethod.getString(form,"reward");

        Honor h = null;
        Optional<Honor> op;

        //编辑
        if(id != null){
            op = honorRepository.findById(id);
            if(op.isPresent()){
                h = op.get();
            }
        }

        //添加
        if(h==null){
            h=new Honor();
            id = honorRepository.getMaxId();
            //设置主键
            if(id==null){
                id = 1;
            }
            else{
                id = id+1;
            }
            h.setId(id);
        }

        Optional<Student> s = studentRepository.findById(studentId);

        if(s.isPresent()){
            Optional<Honor> check = honorRepository.checkMyHonor(s.get().getId(),title,reward);
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            }
            else{
                h.setStudent(s.get());
                h.setTitle(title);
                h.setReward(reward);
                honorRepository.save(h);
            }
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }

        return CommonMethod.getReturnData(h.getId());
    }

    @PostMapping("/honorDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse honorDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Honor h = null;
        Optional<Honor> op;

        if(id!=null){
            op = honorRepository.findById(id);
            if(op.isPresent()){
                h = op.get();
            }
        }

        if(h!=null){
            honorRepository.delete(h);
        }

        return CommonMethod.getReturnMessageOK();
    }
}
