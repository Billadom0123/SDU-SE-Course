package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "person",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "perNum"),
        })

public class Person {
    @Id
    private Integer personId;

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    @NotBlank
    private String perName;

    @NotBlank
    @Size(max = 20)
    private String perNum;


    @Email
    private String email;

    @Size(max=20)
    private String phone;


    private String sex;


    private Integer age;


    private String major;


    private String birthday;

    @ManyToOne
    @JoinColumn(name = "userTypeId")
    private UserType userType;

    public Integer getId() {
        return personId;
    }

    public void setId(Integer id) {
        personId = id;
    }

    public String getNum() {
        return perNum;
    }

    public void setNum(String num) {
        this.perNum = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
