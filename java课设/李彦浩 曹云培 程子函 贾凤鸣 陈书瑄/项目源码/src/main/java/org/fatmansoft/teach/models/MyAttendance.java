package org.fatmansoft.teach.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "my_Attendance",uniqueConstraints = {})
public class MyAttendance implements Comparable<MyAttendance>{
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="myCourseId")
    private MyCourse myCourse;

    private Date classTime;

    private String attendance;

    public MyCourse getMyCourse() {
        return myCourse;
    }

    public void setMyCourse(MyCourse myCourse) {
        this.myCourse = myCourse;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getClassTime() {
        return classTime;
    }

    public void setClassTime(Date classTime) {
        this.classTime = classTime;
    }



    @Override
    public int compareTo(MyAttendance myAttendance){
        Date thisDate = this.getClassTime();
        Date myAttendanceDate = myAttendance.getClassTime();
        SimpleDateFormat ATime=new SimpleDateFormat("yyyyMMdd");
        String thisDateString = ATime.format(thisDate);
        String myAttendanceDateString = ATime.format(myAttendanceDate);
        return thisDate.compareTo(myAttendanceDate);
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
