package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "course")
public class Course implements Comparable<Course>{
    @Id
    private Integer id;

    @NotBlank
    @Size(max = 20)
    private String courseNum;

    @Size(max = 50)
    private String courseName;

    private Integer credit;

    @ManyToOne
    @JoinColumn(name="gradeId")
    private Grade grade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public int compareTo(Course course){
        String thisNum = this.getCourseNum();
        String courseNum = course.getCourseNum();
        return thisNum.compareTo(courseNum);
    }
}
