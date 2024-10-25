package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name="clazz")
public class Clazz implements Comparable<Clazz>{
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="gradeId")
    private Grade grade;

    private Integer classNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }


    public Integer getClassNum() {
        return classNum;
    }

    public void setClassNum(Integer classNum) {
        this.classNum = classNum;
    }

    @Override
    public int compareTo(Clazz clazz){
        Integer thisNum = this.getClassNum();
        Integer clazzNum = this.getClassNum();
        if(thisNum<clazzNum){
            return -1;
        }
        return 1;
    }
}
