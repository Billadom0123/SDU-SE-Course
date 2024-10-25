package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(	name = "achievement"
)
public class Achievement implements Comparable<Achievement>{
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name="courseId")
    private Course course;

    private Double score;

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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Achievement A){
        Double thisScore=this.getScore();
        Double AScore=A.getScore();
        if(thisScore<AScore){
            return 1;
        }
        else return -1;
    }
}
