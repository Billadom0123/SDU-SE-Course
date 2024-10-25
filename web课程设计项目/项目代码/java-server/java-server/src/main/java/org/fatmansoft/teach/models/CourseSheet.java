package org.fatmansoft.teach.models;

import com.alibaba.excel.annotation.ExcelProperty;

public class CourseSheet {
    @ExcelProperty("课程号")
    private Integer CourseId;
    @ExcelProperty("姓名")
    private String studentName;
    @ExcelProperty("学号")
    private String studentNum;


    public CourseSheet(String studentName, String studentNum, Integer courseId) {
        this.studentName = studentName;
        this.studentNum = studentNum;
        CourseId = courseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public Integer getCourseId() {
        return CourseId;
    }

    public void setCourseId(Integer courseId) {
        CourseId = courseId;
    }
}
