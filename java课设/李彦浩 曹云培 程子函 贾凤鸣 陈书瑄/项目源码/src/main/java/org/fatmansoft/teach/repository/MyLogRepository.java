package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.MyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//目标:在点击link后，初始化页面的方法中可以通过接口中提供的方法根据studentId查到所有的Course实体
public interface MyLogRepository extends JpaRepository<MyLog,Integer> {

    @Query(value="select max(id) from MyLog")
    Integer getMaxId();

    //根据学生Id找出所有符合的MyLog实体
    @Query(value = "select * from my_log where student_id like %?%",nativeQuery = true)
    List<MyLog> findMyLogByStudentIdNative(Integer studentId);

    @Query(value = "select * from my_log where student_id=?1",nativeQuery = true)
    Optional<MyLog> OPfindMyLogByStudentIdNative(Integer studentId);

    @Query(value = "select c from MyLog c where c.student.clazz.id=?1 ")
    List<MyLog> findClassLogListByClazzId(Integer clazzId);//like用于字符串 “=”用于数字

    @Query(value = "select c from MyLog c where c.student.clazz.grade.id=?1 ")
    List<MyLog> findClassLogListByGradeId(Integer gradeId);//like用于字符串 “=”用于数字

    //判重方法
 /*   @Query(value ="select * from my_log where student_id=?1 and course_id=?2",nativeQuery = true)
    Optional<MyCourse> checkMyCourse(Integer studentId,Integer courseId);*/
}

