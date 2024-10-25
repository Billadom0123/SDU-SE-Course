package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "my_innovation",uniqueConstraints = {})
public class MyInnovation {
    @Id
    private Integer id;
    @OneToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @Size(max = 50)
    private String practice;

    @Size(max = 50)
    private String  competition;

    @Size(max = 50)
    private String sciAchieve;

    @Size(max = 50)
    private String lecture;

    @Size(max = 50)
    private String inoProject;

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getSciAchieve() {
        return sciAchieve;
    }

    public void setSciAchieve(String sciAchieve) {
        this.sciAchieve = sciAchieve;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getInoProject() {
        return inoProject;
    }

    public void setInoProject(String inoProject) {
        this.inoProject = inoProject;
    }

    public String getInternship() {
        return internship;
    }

    public void setInternship(String internship) {
        this.internship = internship;
    }

    @Size(max = 50)
    private String internship;


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
}
