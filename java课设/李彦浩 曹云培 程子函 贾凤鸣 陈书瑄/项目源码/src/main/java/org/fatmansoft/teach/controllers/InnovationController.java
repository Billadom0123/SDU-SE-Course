package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.StudentRepository;
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

public class InnovationController {
    //Java 对象的注入 我们定义的这下Java的操作对象都不能自己管理是由有Spring框架来管理的， TeachController 中要使用StudentRepository接口的实现类对象，
    // 需要下列方式注入，否则无法使用， studentRepository 相当于StudentRepository接口实现对象的一个引用，由框架完成对这个引用的复制，
    // TeachController中的方法可以直接使用
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InnovationRepository innovationRepository;


    public List getInnovationMapListByNumName(String NumName) {
        List dataList = new ArrayList();

        List<Student> SList = studentRepository.findStudentListByNumNameNative(NumName);
        if(SList == null || SList.size()==0){
            return dataList;
        }

        Map m;
        for(Student stu : SList){
            Integer ID = stu.getId();
            List<Innovation> IList = innovationRepository.findInnovationByStudentIdNative(ID);

            for(Innovation I:IList){
                m = new HashMap();
                m.put("id",I.getId());
                m.put("studentNum",I.getStudent().getStudentNum());
                m.put("studentName",I.getStudent().getStudentName());

                String practiceParas = "model = innovation "+I.getId();
                m.put("practice",I.getPractice());
                m.put("practiceParas",practiceParas);

                String competitionParas = "model = innovation" + I.getId();
                m.put("competition",I.getCompetition());
                m.put("competitionParas",competitionParas);

                m.put("sciAchieve",I.getSciAchieve());
                m.put("lecture",I.getLecture());
                m.put("inoProject",I.getInoProject());
                m.put("internship",I.getInternship());
                dataList.add(m);
            }
        }

        return dataList;
    }



    //student页面初始化方法
    //Table界面初始是请求列表的数据，这里缺省查出所有学生的信息，传递字符“”给方法getStudentMapList，返回所有学生数据，


    @PostMapping("/innovationInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationInit(@Valid @RequestBody DataRequest dataRequest) {
            List dataList = getInnovationMapListByNumName("");//要用String
            return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
        }

        /*List dataList = new ArrayList();
        List<Innovation> IList = innovationRepository.findAll();

        Innovation I;
        Map m;
        for(int i=0;i<IList.size();i++){
            I = IList.get(i);
            m = new HashMap();
            m.put("id",I.getId());
            m.put("studentNum",I.getStudent().getStudentNum());
            m.put("studentName",I.getStudent().getStudentName());
            m.put("sex",I.getStudent().getSex());
            m.put("practice",I.getPractice());
            m.put("competition",I.getCompetition());
            m.put("sciAchieve",I.getSciAchieve());
            m.put("lecture",I.getLecture());
            m.put("inoProject",I.getInoProject());
            m.put("internship",I.getInternship());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list*/


    @PostMapping("/innovationQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("NumName");

        //入口处传字符串
        List dataList = getInnovationMapListByNumName(numName);

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/innovationEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Innovation I = null;

        Optional<Innovation> op;

        if (id != null) {
            op = innovationRepository.findById(id);
            if (op.isPresent()) {
                I = op.get();
            }
        }

        Map form = new HashMap();
        if (I != null) {
            form.put("id", I.getId());
            form.put("studentNum", I.getStudent().getStudentNum());
            form.put("studentName", I.getStudent().getStudentName());
            form.put("sex", I.getStudent().getSex());  //这里不需要转换
            form.put("practice", I.getPractice());
            form.put("competition", I.getCompetition());
            form.put("sciAchieve", I.getSciAchieve());
            form.put("inoProject", I.getInoProject());
            form.put("lecture", I.getLecture());
            form.put("internship", I.getInternship());
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    @PostMapping("/innovationEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        Integer studentNum = CommonMethod.getInteger(form,"studentNum");
        String practice = CommonMethod.getString(form,"practice");
        String competition = CommonMethod.getString(form,"competition");
        String sciAchieve = CommonMethod.getString(form,"sciAchieve");
        String lecture = CommonMethod.getString(form,"lecture");
        String inoProject = CommonMethod.getString(form,"inoProject");
        String internship = CommonMethod.getString(form,"internship");

        Innovation i= null;
        Optional<Innovation> op ;

        if(id != null) {
            op= innovationRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                i = op.get();
            }
        }

        if(i == null) {
            i = new Innovation();   //不存在 创建实体对象
            id = innovationRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            i.setId(id);  //设置新的id
        }

        Optional<Student> sop = studentRepository.OPFindStudentListByNumNameNative(studentNum.toString());

        if(sop.isPresent()) {
            i.setStudent(sop.get());//设置属性
            i.setInternship(internship);
            i.setLecture(lecture);
            i.setCompetition(competition);
            i.setInoProject(inoProject);
            i.setPractice(practice);
            i.setSciAchieve(sciAchieve);
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，没这个人");
        }
        innovationRepository.save(i);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(i.getId());  // 将记录的id返回前端
    }
    //  学生信息删除方法
    //Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
    @PostMapping("/innovationDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Innovation A = null;
        Optional<Innovation> op;
        if(id !=null){
            op = innovationRepository.findById(id);
            if(op.isPresent()){
                A = op.get();
            }
        }
        if(A != null){
            innovationRepository.delete(A);
        }
        return CommonMethod.getReturnMessageOK();
    }


}
