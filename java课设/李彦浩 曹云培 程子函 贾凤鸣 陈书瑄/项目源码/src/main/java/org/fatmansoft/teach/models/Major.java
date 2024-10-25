package org.fatmansoft.teach.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="major")
public class Major implements Comparable<Major>{
    @Id
    private Integer id;

    private String majorNum;
    private String majorName;

    public String getMajorNum() {
        return majorNum;
    }

    public void setMajorNum(String majorNum) {
        this.majorNum = majorNum;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(Major mj){
        String thisNum = this.getMajorNum();
        String mjNum = mj.getMajorNum();
        if(thisNum.compareTo(mjNum)<0){
            return -1;
        }
        return 1;
    }
}
