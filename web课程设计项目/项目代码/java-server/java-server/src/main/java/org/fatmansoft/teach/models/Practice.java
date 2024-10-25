package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "practice",
        uniqueConstraints = {
        })
public class Practice {
    @Id
    private Integer id;

//    @NotBlank
//    @Size(max=20)
//    private String teamName;


    @Size(max=500)
    private String content;

    @NotBlank
    private String date;

    @Size(max=20)
    private String level;

    @Size(max=50)
    private String award;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    private Integer term;

    private Integer grade;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public String getTeamName() {
//        return teamName;
//    }

//    public void setTeamName(String teamName) {
//        this.teamName = teamName;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
