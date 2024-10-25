package org.fatmansoft.teach.controllers;
import org.fatmansoft.teach.models.MyLog;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.MyLogRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class MyLogController {
    private static Integer studentId;
    @Autowired
    private MyLogRepository myLogRepository;
    @Autowired
    private StudentRepository studentRepository;

    /*log//学生日常花销 多个
    前端通过学生的学号查询，不是学生的外键
    提交信息
    更改
    删除*/
    //查询该id学生的log信息，并转换成Map的数据格式存放到List
    public List getMyLogMapList(Integer Id) {//重载返回String类型
        List dataList = new ArrayList<>();
        //先通过学号查询到对应的学生id 学生对象 找到学生对象
        List<MyLog> myLogList = myLogRepository.findMyLogByStudentIdNative(Id);
        Collections.sort(myLogList);
        Map m;
        //查出来之后以map的形式放入最终返回的数表中
        String StudentDetailsParas;
        for(MyLog s:myLogList){
            m = new HashMap();
            m.put("id", s.getId());
            m.put("consume",s.getConsume());
                //在前端显示学生的姓名学号
            m.put("studentNum",s.getStudent().getStudentNum());
            m.put("studentName",s.getStudent().getStudentName());
            m.put("consumeTime", DateTimeTool.parseDateTime(s.getConsumeTime(),"yyyy-MM-dd"));

            StudentDetailsParas = "model=StudentDetails&studentId="+s.getStudent().getId();
            m.put("StudentDetails","返回");
            m.put("StudentDetailsParas",StudentDetailsParas);
            dataList.add(m);
        }
    //    List DataList = new ArrayList();

        return dataList;
    }

    /* log页面初始化方法
    Table界面初始是请求列表的数据，这里缺省查出所有log的信息，传递字符“ ”给方法getLogMapList，返回所有Log数据，*/
    @PostMapping("/myLogInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse mylogInit(@Valid @RequestBody DataRequest dataRequest) {
        studentId = dataRequest.getInteger("studentId");
        List dataList = getMyLogMapList(studentId);//要用String
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /*
    logEdit初始化方法
    logEdit编辑页面进入时首先请求的一个方法，如果是Edit,在前台会把对应要编辑的那个 ”log信息的id“ 作为参数回传给后端，
    我们通过Integer id = dataRequest.getInteger("id")
    获得对应log信息的id，根据id从数据库中查出数据，存在Map对象里，并返回前端，
    如果是添加，则前端没有id传回，Map 对象数据为空（界面上的数据也为空白）
     */
    @PostMapping("/myLogEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse mylogEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");//id对应的value已知，通过这个方法找出对应的value
        MyLog s= null;
        Optional<MyLog> op;
        if(id != null) {
            op = myLogRepository.findById(id);//log自己的id
            if(op.isPresent()) {//判断查询的类对象是否存在
                s = op.get();//与get方法一起使用
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("consume",s.getConsume());
            form.put("consumeTime",DateTimeTool.parseDateTime(s.getConsumeTime(),"yyyy-MM-dd"));
        //    form.put("studentNum",s.getStudent().getStudentNum());//返回的是student学号
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    //  log信息提交按钮方法
    //相应提交请求的方法，前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
    //实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new log 计算新的id， 复制相关属性，保存，
    // 如果是编辑原来的信息，
    //id 不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
    @PostMapping("/myLogEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse mylogEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        Date consumeTime = CommonMethod.getDate(form,"consumeTime");
     //   String studentNum = CommonMethod.getString(form,"studentNum");//
        String consume = CommonMethod.getString(form,"consume");

        MyLog s= null;
        Optional<MyLog> op;
        if(id != null) {
            op = myLogRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new MyLog();   //不存在 创建实体对象
            id = myLogRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        //System.out.println("studentId"+studentId);
        Optional<Student> stu = studentRepository.findById(studentId);//这个StudentId对应的学生不存在
        //  s.setId(id);  //设置属性
        s.setStudent(stu.get());//
        s.setConsume(consume);
        s.setConsumeTime(consumeTime);
        myLogRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }

    //  log信息删除方法
    //log页面的列表里点击删除按钮则可以删除已经存在的log信息，
    // 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
    @PostMapping("/myLogDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse mylogDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        MyLog s= null;
        Optional<MyLog> op;
        if(id != null) {
            op= myLogRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            myLogRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }


}
