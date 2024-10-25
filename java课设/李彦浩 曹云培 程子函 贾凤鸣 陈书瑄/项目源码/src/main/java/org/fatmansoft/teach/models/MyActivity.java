package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name= "my_activity" ,uniqueConstraints = {})

public class MyActivity {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    private String PE;

    private String travel;

    private String performance;

    private String party;

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

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
