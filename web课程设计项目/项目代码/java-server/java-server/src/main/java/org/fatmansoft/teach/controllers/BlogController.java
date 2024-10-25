package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Blog;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/blog")
public class BlogController {
    static int blogId=0;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    //找到当前用户
    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    public Teacher getCurrentTeacher(){
        return teacherRepository.findTeacherByUserId(getCurrentUser());
    }

    @PostMapping("/getBlogger")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse getBlogger(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        //blogger三个参数:博主名字(用户名),博客数量,博主简介
        Integer currentUser = getCurrentUser();
        Optional<User> byUserId = userRepository.findByUserId(currentUser);
        String userName = byUserId.get().getUserName();
        Student currentStudent = getCurrentStudent();
        Integer studentId = currentStudent.getId();
        List<Blog> blogByStudentId = blogRepository.findBlogByStudentId(studentId);
        Map m = new HashMap<>();
        m.put("name",userName);
        m.put("blogNum",blogByStudentId.size());
        m.put("introduce",currentStudent.getIntroduce());
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/getBlogs")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse getBlogs(@Valid @RequestBody DataRequest dataRequest){
        //博客的三个参数:标题 介绍 日期
        List dataList = new ArrayList();
        Integer studentId = getCurrentStudent().getId();
        List<Blog> blogByStudentId = blogRepository.findBlogByStudentId(studentId);
        for (Blog blog : blogByStudentId) {
            Map m = new HashMap();
            m.put("id",blog.getId());
            m.put("title",blog.getTitle());
            m.put("introduction",blog.getIntroduction());
            m.put("date",blog.getDate());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/fetchBlogId")
    @PreAuthorize(("hasRole('STUDENT')"))
    public void fetchBlogId(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        Integer id = (Integer) data.get("id");
        blogId=id;
    }

    @PostMapping("/showBlog")
    @PreAuthorize(("hasRole('STUDENT')"))
    //跳转页面后的初始化方法
    public DataResponse showBlog(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        //现在已经抓取到了需要展示的博客的id，直接查即可
        Optional<Blog> blogByBlogId = blogRepository.findBlogByBlogId(blogId);
        Blog blog = blogByBlogId.get();
        Map m = new HashMap();
        m.put("id",blogId);
        m.put("title",blog.getTitle());
        m.put("introduction",blog.getIntroduction());
        m.put("article",blog.getArticle());
        m.put("date",blog.getDate());
        dataList.add(m);
        return  CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/showOther")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse showOther(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer studentId = getCurrentStudent().getId();
        List<Blog> blogByStudentId = blogRepository.findBlogByStudentId(studentId);
        for (Blog blog : blogByStudentId) {
            if(blog.getId()==blogId){
                continue;
            }
            Map m = new HashMap();
            m.put("id",blog.getId());
            m.put("title",blog.getTitle());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/getTeacher")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse getTeacher(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer currentUser = getCurrentUser();
        Optional<User> byUserId = userRepository.findByUserId(currentUser);
        String perName = byUserId.get().getPerson().getPerName();
//        Teacher teacherByUserId = teacherRepository.findTeacherByUserId(currentUser);
//        String research = teacherByUserId.getResearch();
//        String[] researches = research.split("/");
//        String paper = teacherByUserId.getPaper();
//        String[] papers = paper.split("/");
        Map m = new HashMap();
        m.put("teacherName",perName);
//        dataList.add(m);
        return CommonMethod.getReturnData(m);
    }

    @PostMapping("/getResearch")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse getResearch(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer currentUser = getCurrentUser();
        Teacher teacherByUserId = teacherRepository.findTeacherByUserId(currentUser);
        String research = teacherByUserId.getResearch();
        String[] researches = research.split("/");
        for (String s : researches) {
            Map m = new HashMap();
            m.put("research",s);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/getPaper")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse getPaper(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        Integer currentUser = getCurrentUser();
        Teacher teacherByUserId = teacherRepository.findTeacherByUserId(currentUser);
        String paper = teacherByUserId.getPaper();
        String[] papers = paper.split("/");
        for (String s : papers) {
            Map m = new HashMap();
            m.put("paper",s);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/addResearch")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse addResearch(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        String newResearch = (String) data.get("newResearch");
        Teacher currentTeacher = getCurrentTeacher();
        String research = currentTeacher.getResearch();
        if(research==""){
            research+=newResearch;
        }
        else{
            research+="/"+newResearch;
        }
        currentTeacher.setResearch(research);
        teacherRepository.save(currentTeacher);
        return CommonMethod.getReturnMessageOK("success");
    }

    @PostMapping("/deleteResearch")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse deleteResearch(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        String deleteResearch = (String) data.get("deleteResearch");
        Teacher currentTeacher = getCurrentTeacher();
        String research = currentTeacher.getResearch();
        String[] researches = research.split("/");
        List<String> researchList = new ArrayList();
        for (String s : researches) {
            if(Objects.equals(s, deleteResearch)){
                continue;
            }
            else {
                researchList.add(s);
            }
        }
        String result="";
        int count=0;
        for (String s : researchList) {
            if (count==0){
                result+=s;
                count++;
            }
            else{
                result+="/"+s;
            }
        }
        currentTeacher.setResearch(result);
        teacherRepository.save(currentTeacher);
        return CommonMethod.getReturnMessageOK("success");
    }

    @PostMapping("/addPaper")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse addPaper(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        String newResearch = (String) data.get("newPaper");
        Teacher currentTeacher = getCurrentTeacher();
        String research = currentTeacher.getPaper();
        if(research==""){
            research+=newResearch;
        }
        else{
            research+="/"+newResearch;
        }
        currentTeacher.setPaper(research);
        teacherRepository.save(currentTeacher);
        return CommonMethod.getReturnMessageOK("success");
    }

    @PostMapping("/deletePaper")
    @PreAuthorize(("hasRole('TEACHER')"))
    public DataResponse deletePaper(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        String deletePaper = (String) data.get("deletePaper");
        Teacher currentTeacher = getCurrentTeacher();
        String paper = currentTeacher.getPaper();
        String[] papers = paper.split("/");
        List<String> paperList = new ArrayList();
        for (String s : papers) {
            if(Objects.equals(s, deletePaper)){
                continue;
            }
            else {
                paperList.add(s);
            }
        }
        String result="";
        int count=0;
        for (String s : paperList) {
            if (count==0){
                result+=s;
                count++;
            }
            else{
                result+="/"+s;
            }
        }
        currentTeacher.setPaper(result);
        teacherRepository.save(currentTeacher);
        return CommonMethod.getReturnMessageOK("success");
    }

    @PostMapping("/addBlog")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse addBlog(@Valid @RequestBody DataRequest dataRequest){
        String title = dataRequest.getString("title");
        String introduction = dataRequest.getString("introduction");
        String article = dataRequest.getString("article");
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String time = dateFormat.format(date);
        Blog blog = new Blog();
        Integer maxId = blogRepository.getMaxId();
        blog.setId(maxId+1);
        blog.setStudent(getCurrentStudent());
        blog.setTitle(title);
        blog.setIntroduction(introduction);
        blog.setArticle(article);
        blog.setDate(time);
        blogRepository.save(blog);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteBlog")
    @PreAuthorize(("hasRole('STUDENT')"))
    public DataResponse deleteBlog(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");
        Blog byId = blogRepository.getById(id);
        blogRepository.delete(byId);
        return CommonMethod.getReturnMessageOK();
    }

}
