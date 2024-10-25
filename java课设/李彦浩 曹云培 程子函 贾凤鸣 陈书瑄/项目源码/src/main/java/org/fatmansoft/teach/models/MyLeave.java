package org.fatmansoft.teach.models;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(	name = "my_leave",
        uniqueConstraints = {
        })
public class MyLeave implements Comparable<MyLeave>{
    @Id
    private Integer id;
    private Date OutTime;//外出时间
    private Date BackTime;//返回时间

    //记录学生外出请假信息，与log分开管理
    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    @NotBlank
    @Size(max = 50)
    private String Reason;//一个原因对应一个外出返回时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOutTime() {
        return OutTime;
    }

    public void setOutTime(Date outTime) {
        this.OutTime = outTime;
    }

    public Date getBackTime() {
        return BackTime;
    }

    public void setBackTime(Date backTime) {
        this.BackTime = backTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    //排序规则：外出时间离当前时间越近放在越前面，外出时间相同，返回时间越大放在越后面
    @Override
    public int compareTo(MyLeave A){
        Date AOutTime=A.getOutTime();
        Date ABackTime=A.getOutTime();
        Date thisOutTime=this.getOutTime();
        Date thisBackTime=this.getBackTime();
        SimpleDateFormat ATime=new SimpleDateFormat("yyyyMMdd");
        String AoutTime=ATime.format(AOutTime);
        String AbackTime=ATime.format(ABackTime);
        String thisoutTime=ATime.format(thisOutTime);
        String thisbackTime=ATime.format(thisBackTime);
        if(this.getStudent().getClazz().getClassNum()>A.getStudent().getClazz().getClassNum()){
            return 1;
        }
        else{
            if(Integer.parseInt(thisoutTime)<Integer.parseInt(AoutTime)){//时间小放前面
                return 1;
            }
            else if(Integer.parseInt(thisoutTime)==Integer.parseInt(AoutTime)){
                if(Integer.parseInt(thisbackTime)<Integer.parseInt(AbackTime)){
                    return 1;
                }
            }
        }
        return -1;
    }
}

