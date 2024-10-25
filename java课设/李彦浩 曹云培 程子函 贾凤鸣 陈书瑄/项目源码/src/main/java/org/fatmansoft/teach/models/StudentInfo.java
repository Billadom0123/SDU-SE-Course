package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "studentInfo"
)
public class StudentInfo {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name="studentId")
    private Student student;

    @Size(max = 50)
    private String hometown;

    @Size(max = 50)
    private String contact;

    @Size(max = 50)
    private String political;

    @Size(max = 50)
    private String major;

    @Size(max = 50)
    private String clazz;

    @Size(max = 50)
    private String ethnic;


    /*@OneToOne
    @JoinColumn(name="PERSON")
    @Size(max = 50)
    private Person person;
*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }



    /*public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
*/
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

