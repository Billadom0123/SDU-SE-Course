package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name="admin",
        uniqueConstraints = {})
public class Admin {
    @Id
    private Integer id;

    private String grade;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @OneToOne
    @JoinColumn(name="personId")
    private Person person;
}
