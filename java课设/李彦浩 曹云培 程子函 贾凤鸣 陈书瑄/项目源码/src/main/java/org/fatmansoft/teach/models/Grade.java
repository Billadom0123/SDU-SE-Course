package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name="grade")
public class Grade implements Comparable<Grade> {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="majorId")
    private Major major;

    private Integer gradeNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Integer getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(Integer gradeNum) {
        this.gradeNum = gradeNum;
    }

    @Override
    public int compareTo(Grade grade){
        Integer thisNum = this.getGradeNum();
        Integer GradeNum = grade.getGradeNum();
        if(thisNum<GradeNum){
            return -1;
        }
        return 1;
    }
}
