package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByStudentNum(String studentNum);
    List<Student> findByStudentName(String studentName);

    @Query(value = "select max(id) from Student  ")
    Integer getMaxId();

    @Query(value = "from Student where ?1='' or studentNum like %?1% or studentName like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    @Query(value = "select * from student  where ?1='' or student_num like %?1% or student_name like %?1% ", nativeQuery = true)
    List<Student> findStudentListByNumNameNative(String numName);

    //根据用户id找到学生用户
    @Query(value="select * from student where user_id=?1",nativeQuery = true)
    Optional<Student> findStudentByUserId(Integer userId);

    @Query(value="select s from Student s where s.studentNum=?1 and s.studentName=?2")
    Student findStudentByStudentNumAndStudentName(String studentNum,String studentName);

    @Query(value = "select s from Student s where s.grade=?1")
    List<Student> findStudentByGrade(Integer grade);



}
