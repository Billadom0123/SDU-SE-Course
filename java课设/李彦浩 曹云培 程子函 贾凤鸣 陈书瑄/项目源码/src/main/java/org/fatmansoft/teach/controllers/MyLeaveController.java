package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.MyLeave;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.MyLeaveRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyLeaveController {
    private static Integer studentId;
    @Autowired
    private MyLeaveRepository myLeaveRepository;
    @Autowired
    private StudentRepository studentRepository;


    /*leave
    id,backtime,outtime,reason,student_id;
    提交信息
    更改
    删除*/
    //查询所有外键相匹配的学生信息，并转换成Map的数据格式存放到List
    public List getMyLeaveMapList(Integer Id) {//重载返回String类型
        List dataList = new ArrayList();
        //先通过学号查询到对应的学生id 学生对象 找到学生对象
        Map m;
        List<MyLeave> AList = myLeaveRepository.findMyLeaveByStudentId(Id);
        Collections.sort(AList);
        //查出来之后以map的形式放入最终返回的数表中
        //前端要有对应的map
        String StudentDetailsParas;
        for(MyLeave s:AList){
            m = new HashMap();
            m.put("id", s.getId());
            m.put("outtime", DateTimeTool.parseDateTime(s.getOutTime(),"yyyy-MM-dd"));//外出时间
            m.put("backtime",DateTimeTool.parseDateTime(s.getBackTime(),"yyyy-MM-dd"));//返回时间
            m.put("reason",s.getReason());//外出原因
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());

            StudentDetailsParas = "model=StudentDetails&studentId="+s.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }
        return dataList;
    }

    /* leave页面初始化方法
    Table界面初始是请求列表的数据，这里缺省查出所有leave的信息，传递字符“ ”给方法getLeaveMapList，返回所有Leave数据，*/
    @PostMapping("/myLeaveInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyLeaveMapList(studentId);//要用String
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    //leave页面点击查询按钮请求
    //Table界面初始是请求列表的数据，从请求对象里获得前端界面输入的字符串，作为参数传递给方法getLeaveMapList，返回该学生所有leave数据，
  /*  @PostMapping("/myLeaveQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse leaveQuery(@Valid @RequestBody DataRequest dataRequest) {
        String NumName= dataRequest.getString("NumName");//
        List dataList = getMyLeaveMapList(NumName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }*/

    //myLeaveEdit初始化
    @PostMapping("/myLeaveEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");//id对应的value已知，通过这个方法找出对应的value
        MyLeave s= null;
        Optional<MyLeave> op;
        if(id != null) {
            op = myLeaveRepository.findById(id);//log自己的id
            if(op.isPresent()) {//判断查询的类对象是否存在
                s = op.get();//与get方法一起使用
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("outtime", DateTimeTool.parseDateTime(s.getOutTime(),"yyyy-MM-dd"));//外出时间
            form.put("backtime",DateTimeTool.parseDateTime(s.getBackTime(),"yyyy-MM-dd"));//返回时间
            form.put("reason",s.getReason());//外出原因
       //     form.put("studentNum",s.getStudent().getStudentNum());
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    // leave信息提交按钮方法
    @PostMapping("/myLeaveEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse leaveEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        String reason  = CommonMethod.getString(form,"reason");
        Date outtime = CommonMethod.getDate(form,"outtime");
        Date backtime = CommonMethod.getDate(form,"backtime");
        SimpleDateFormat ATime=new SimpleDateFormat("yyyyMMdd");
        String outTime=ATime.format(outtime);
        String backTime=ATime.format(backtime);
        if(Integer.parseInt(backTime)<Integer.parseInt(outTime)){//返回时间要大于出校时间，不然不能添加
            return CommonMethod.getReturnMessageError("");
        }
      //  String studentNum = CommonMethod.getString(form,"studentNum");
        //或者是用id查学生
        MyLeave s= null;
        Optional<MyLeave> op;
        if(id != null) {
            op = myLeaveRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new MyLeave();   //不存在 创建实体对象
            id = myLeaveRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        Optional<Student> stu = studentRepository.findById(studentId);
        s.setId(id);  //设置属性
        s.setOutTime(outtime);
        s.setBackTime(backtime);

        s.setReason(reason);
        s.setStudent(stu.get());
        myLeaveRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }

    //  leave信息删除方法
    //leave页面的列表里点击删除按钮则可以删除已经存在的leave信息，
    // 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
    @PostMapping("/myLeaveDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse leaveDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        MyLeave s= null;
        Optional<MyLeave> op;
        if(id != null) {
            op= myLeaveRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            myLeaveRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

}
