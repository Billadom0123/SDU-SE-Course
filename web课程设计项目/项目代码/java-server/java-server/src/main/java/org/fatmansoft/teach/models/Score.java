package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(	name = "score",
        uniqueConstraints = {
        })
public class Score implements Comparable<Score>{
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @NotNull
    private Integer regular;

    @NotNull
    private Integer exam;

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

    public Integer getRegular() {
        return regular;
    }

    public void setRegular(Integer regular) {
        this.regular = regular;
    }

    public Integer getExam() {
        return exam;
    }

    public void setExam(Integer exam) {
        this.exam = exam;
    }

    public double getTotal(){
        return exam*0.7+regular*0.3;
    }

    @Override
    public int compareTo(Score o) {
        if(getTotal()>o.getTotal()){
            return 1;
        }
        else if(getTotal()==o.getTotal()){
            if(exam>o.exam){
                return 1;
            }
            else if(exam==o.exam){
                return 0;
            }
            else{
                return -1;
            }
        }
        return -1;
    }
}