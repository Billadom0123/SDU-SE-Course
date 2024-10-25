package org.fatmansoft.teach.models;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity//改完在看数据库
@Table(	name = "my_log",
        uniqueConstraints = {
        })
public class MyLog implements Comparable<MyLog>{
    @Id
    private Integer id;
    //记录学生日常花销
    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @NotBlank
    @Size(max = 50)
    private String Consume;//一个学生多次花销

    private Date ConsumeTime;//每笔花销的时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsume() {
        return Consume;
    }

    public void setConsume(String consume) {
        this.Consume = consume;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Date getConsumeTime() {
        return ConsumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        ConsumeTime = consumeTime;
    }

    //排序规则：不同时间，时间靠近现在的放在前面；相同时间，花销小的放在前面
    @Override
    public int compareTo(MyLog A){
        Date AconsumeTime=A.getConsumeTime();
        Date thisConsumeTime=this.getConsumeTime();
        SimpleDateFormat ATime=new SimpleDateFormat("yyyyMMdd");
        String AcTime=ATime.format(AconsumeTime);
        String thisTime=ATime.format(thisConsumeTime);
        String Aconsume=A.getConsume();
        String thisconsume=this.getConsume();
        int thisStudentId=this.getStudent().getId();
        int AStudentId=A.getStudent().getId();
        if(this.getStudent().getClazz().getClassNum()>A.getStudent().getClazz().getClassNum()){
            return 1;
        }
        else
        {
            if(thisStudentId>AStudentId) return 1;
            else if(thisStudentId==AStudentId){
                if(Integer.parseInt(thisTime)>Integer.parseInt(AcTime)){//时间小放前面
                    return 1;
                }
                else if(Integer.parseInt(thisTime)==Integer.parseInt(AcTime)){
                    if(Integer.parseInt(thisconsume)>Integer.parseInt(Aconsume)) {
                        return 1;
                    }
                }
            }
        }

        return -1;
    }
}

