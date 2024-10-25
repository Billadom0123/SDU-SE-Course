package org.fatmansoft.teach.models;

/*
作为一个将一个学生和多个课程链接在一起的类（页面） MyCourse没有任何自带的属性
 */

import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;

@Entity
@Table(name = "my_course",uniqueConstraints = {})
public class MyCourse implements Comparable<MyCourse> {
    @Id
    private Integer id;

    //MyCourse的多条记录关联某一个学生实体
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    //一个MyCourse实体对应一个Course实体
    @OneToOne
    @JoinColumn(name = "courseId")
    private Course course;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    //按课程号排
    @Override
    public int compareTo(MyCourse mc){
        Integer thisNum = Integer.parseInt(this.getCourse().getCourseNum());
        Integer mcNum = Integer.parseInt(mc.getCourse().getCourseNum());
        if(thisNum<mcNum){
            return -1;
        }
        return 1;
    }
}
