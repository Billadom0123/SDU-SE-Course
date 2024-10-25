package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.MyLeave;
import org.fatmansoft.teach.models.MyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyLeaveRepository extends JpaRepository<MyLeave,Integer>{
    @Query(value = "select max(id) from MyLeave")
    Integer getMaxId();

    //根据studentId查询该学生对应的leave信息，查询对应的reason和外出返回时间信息
    @Query(value="select a from MyLeave a where a.student.id=?1 ")
    List<MyLeave> findMyLeaveByStudentId(Integer studentId);

    @Query(value="select * from Leave where student_id like %?%",nativeQuery = true)
    List<MyLeave> findLeaveByString(String studentId);

    @Query(value = "select c from MyLeave c where c.student.clazz.id=?1 ")
    List<MyLeave> findClassLeaveListByClazzId(Integer clazzId);//like用于字符串 “=”用于数字

    @Query(value = "select c from MyLeave c where c.student.clazz.grade.id=?1 ")
    List<MyLeave> findGradeLeaveListByGradeId(Integer gradeId);

}
