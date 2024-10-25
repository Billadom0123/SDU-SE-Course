package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="resource",uniqueConstraints ={})

public class Resource {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="courseId")
    private Course course;

    @NotBlank
    private String textBooks;

    @NotBlank
    private String courseWare;//课件

    @NotBlank
    private String reference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTextBooks() {
        return textBooks;
    }

    public void setTextBooks(String textBooks) {
        this.textBooks = textBooks;
    }

    public String getCourseWare() {
        return courseWare;
    }

    public void setCourseWare(String courseWare) {
        this.courseWare = courseWare;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) { this.course = course;}

}
