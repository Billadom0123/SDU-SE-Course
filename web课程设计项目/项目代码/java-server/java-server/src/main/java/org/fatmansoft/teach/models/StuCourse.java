package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(	name = "stuCourse",
        uniqueConstraints = {
        })
public class StuCourse {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @OneToOne
    @JoinColumn(name="courseId")
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
}
