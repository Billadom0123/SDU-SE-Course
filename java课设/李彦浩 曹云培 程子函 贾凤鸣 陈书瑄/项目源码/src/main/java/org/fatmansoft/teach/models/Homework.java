package org.fatmansoft.teach.models;



import javax.persistence.*;

@Entity
@Table(name=" homework")
public class Homework implements Comparable<Homework>{
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name="courseId")
    private Course course;

    private Double homeworkScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Double getHomeworkScore() {
        return homeworkScore;
    }

    public void setHomeworkScore(Double homeworkScore) {
        this.homeworkScore = homeworkScore;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public String judegecondition(){
        String condition;
        if(homeworkScore<60){
            condition="未按时提交";
            return condition;
        }
        else{
            condition="已提交";
            return condition;
        }
    }
    @Override
    public int compareTo(Homework H){
        Double thisScore=this.getHomeworkScore();
        Double HScore=H.getHomeworkScore();
        if(thisScore>HScore){
            return 1;
        }
        else return -1;
    }
}
