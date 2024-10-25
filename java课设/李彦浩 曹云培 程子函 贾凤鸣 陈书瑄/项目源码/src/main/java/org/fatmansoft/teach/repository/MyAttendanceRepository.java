package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.MyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;


@Repository
public interface MyAttendanceRepository extends JpaRepository<MyAttendance,Integer> {
    @Query(value="select max(id) from MyAttendance")
    Integer getMaxId();

    @Query(value = "select ma from MyAttendance ma where ma.myCourse.student.id=?1 and ma.myCourse.course.id=?2")
    List<MyAttendance> findMyAttendanceByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value = "select ma from MyAttendance ma where ma.myCourse.student.id=?1")
    List<MyAttendance> findMyAttendanceByStudentId(Integer studentId);

    @Query(value = "select ma from MyAttendance ma where ma.myCourse.id=?1")
    List<MyAttendance> findMyAttendanceByCourseId(Integer courseId);
}
