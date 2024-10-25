package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "user",
        uniqueConstraints = {
        })
public class User {
    @Id
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @OneToOne
    @JoinColumn(name="personId")
    private Person person;

    @NotBlank
    @Size(max = 20)
    private String userName;


    @NotBlank
    @Size(min=6,max = 60)
    private String password;


    public User(){}

    public User(String username, String password) {
        this.userName = username;
        this.password = password;
    }


    public Integer getId() {
        return userId;
    }

    public void setId(Integer id) {
        this.userId = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
