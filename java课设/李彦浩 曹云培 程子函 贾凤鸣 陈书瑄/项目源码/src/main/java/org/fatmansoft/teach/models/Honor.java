package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "honor",
        uniqueConstraints = {
        })
public class Honor {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @NotBlank
    private String title;

    @NotBlank
    private String reward;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
