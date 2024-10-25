package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "activity",
        uniqueConstraints = {
        })
public class Activity {
    @Id
    private Integer id;


    @NotBlank
    @Size(max=50)
    private String PE;



    @NotBlank
    @Size(max=50)
    private String party;


    @NotBlank
    @Size(max=50)
    private String performance;

    @NotBlank
    @Size(max=50)
    private String travel;




    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }



    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
