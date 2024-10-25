package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.Resource;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.ResourceRepository;
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
public class TeachResourceController {
    private static Integer courseId;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CourseRepository courseRepository;


    public List getResourceListByCourseId(Integer Id){
        List dataList = new ArrayList<>();

        //这里的Id是上个页面(也即学生页面)回传的学生Id
        List<Resource> tempList = resourceRepository.FindResourceByCourseIdNative(Id);

        Map m;
        String courseParas;
        for(Resource r:tempList){
            m = new HashMap();
            m.put("id",r.getId());
            m.put("courseNum",r.getCourse().getCourseNum());
            m.put("courseName",r.getCourse().getCourseName());
            m.put("textBooks",r.getTextBooks());
            m.put("courseWare",r.getCourseWare());
            m.put("reference",r.getReference());
            courseParas = "model=course&gradeId="+r.getCourse().getGrade().getId();
            m.put("course","返回");
            m.put("courseParas",courseParas);
            dataList.add(m);
        }

        return dataList;
    }

    @PostMapping("/resourceInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse resourceInit(@Valid @RequestBody DataRequest dataRequest){
        courseId = dataRequest.getInteger("courseId");
        List dataList = getResourceListByCourseId(courseId);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/resourceEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse resourceEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Resource r = null;
        Optional<Resource> op;

        if(id!=null){
            op = resourceRepository.findById(id);
            if(op.isPresent()){
                r = op.get();
            }
        }

        Map form = new HashMap();
        if(r!=null){
            form.put("id",r.getId());
            //form.put("courseNum",r.getCourse().getCourseNum());
            form.put("textBooks",r.getTextBooks());
            form.put("courseWare",r.getCourseWare());
            form.put("reference",r.getReference());
        }

        return CommonMethod.getReturnData(form);
    }

    @PostMapping("/resourceEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse resourceEditSubmit(@Valid @RequestBody DataRequest dataRequest){
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        //String courseNum = CommonMethod.getString(form,"courseNum");
        String textBooks=CommonMethod.getString(form,"textBooks");
        String courseWare=CommonMethod.getString(form,"courseWare");
        String reference=CommonMethod.getString(form,"reference");

        Resource r = null;
        Optional<Resource> op;

        //编辑
        if(id != null){
            op = resourceRepository.findById(id);
            if(op.isPresent()){
                r = op.get();
            }
        }

        //添加
        if(r==null){
            r=new Resource();
            id = resourceRepository.getMaxId();
            //设置主键
            if(id==null){
                id = 1;
            }
            else{
                id = id+1;
            }
            r.setId(id);
        }

        Optional<Course> c= courseRepository.findById(courseId);

        if(c.isPresent()){
            Optional<Resource> check = resourceRepository.checkMyResource(c.get().getId(),textBooks,courseWare,reference);
            if(check.isPresent()){
                return CommonMethod.getReturnMessageError("提交失败，不能提交重复的信息");
            }
            else{
                r.setCourse(c.get());
                r.setTextBooks(textBooks);
                r.setCourseWare(courseWare);
                r.setReference(reference);
                resourceRepository.save(r);
            }
        }
        else{
            return CommonMethod.getReturnMessageError("提交失败，不存在该学生或课程");
        }

        return CommonMethod.getReturnData(r.getId());
    }

    @PostMapping("/resourceDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse resourceDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Resource r = null;
        Optional<Resource> op;

        if(id!=null){
            op = resourceRepository.findById(id);
            if(op.isPresent()){
                r = op.get();
            }
        }

        if(r!=null){
            resourceRepository.delete(r);
        }

        return CommonMethod.getReturnMessageOK();
    }
}
