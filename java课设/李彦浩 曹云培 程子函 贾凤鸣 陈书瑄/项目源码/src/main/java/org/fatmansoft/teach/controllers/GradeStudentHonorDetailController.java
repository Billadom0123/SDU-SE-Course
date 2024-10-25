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
public class GradeStudentHonorDetailController {
    private static Integer honorId;
    private static Integer gradeId;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ClazzRepository clazzRepository;

    public List getAllHonorStudentList(Integer honorId,Integer gradeId){
        List dataList = new ArrayList();
        Optional<Honor> honor=honorRepository.FindHonorById(honorId);
        List<Honor> honorList = honorRepository.findHonorByTitleAndGradeId(honor.get().getTitle(),gradeId);

        if(honorList==null||honorList.size()==0){
            return dataList;
        }

        Map m;
        for(Honor H:honorList){
            m = new HashMap();
            m.put("id",H.getId());
            m.put("majorNum",H.getStudent().getClazz().getGrade().getMajor().getMajorNum());
            m.put("majorName",H.getStudent().getClazz().getGrade().getMajor().getMajorName());
            m.put("gradeNum",H.getStudent().getClazz().getGrade().getGradeNum());
            m.put("classNum",H.getStudent().getClazz().getClassNum());
            m.put("studentNum",H.getStudent().getStudentNum());
            m.put("studentName",H.getStudent().getStudentName());
            m.put("title",H.getTitle());
            m.put("reward",H.getReward());
            String gradeStudentHonorParas = "model=gradeStudentHonor&gradeId="+gradeId;
            m.put("gradeStudentHonor","返回");
            m.put("gradeStudentHonorParas",gradeStudentHonorParas);
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/gradeStudentHonorDetailInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHonorDetailInit(@Valid @RequestBody DataRequest dataRequest) {
        honorId = dataRequest.getInteger("honorId");
        gradeId = dataRequest.getInteger("gradeId");
        List dataList = getAllHonorStudentList(honorId,gradeId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/gradeStudentHonorDetailEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHonorDetailEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Honor h=null;
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
            form.put("studentNum",h.getStudent().getStudentNum());
        }

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/gradeStudentHonorDetailEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHonorDetailEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");

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

        Optional<Honor> c= honorRepository.findById(honorId);
        Optional<Student> s = studentRepository.OPFindStudentByGradeIdAndStudentNum(gradeId,studentNum);

        //此处改成了Optional，为了用s.get（），不知道会有什么后果
        if(s.isPresent()){
            Optional<Honor> check = honorRepository.checkMyHonor(s.get().getId(),c.get().getTitle(),c.get().getReward());
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            }
            else{
                h.setStudent(s.get());
                h.setTitle(c.get().getTitle());
                h.setReward(c.get().getReward());
                honorRepository.save(h);
            }
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }

        return CommonMethod.getReturnData(h.getId());
    }

    @PostMapping("/gradeStudentHonorDetailDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeStudentHonorDetailDelete(@Valid @RequestBody DataRequest dataRequest){
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
