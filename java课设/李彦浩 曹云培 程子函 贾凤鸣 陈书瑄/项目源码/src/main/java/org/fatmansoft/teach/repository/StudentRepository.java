package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    @Query(value = "select max(id) from Student  ")
    Integer getMaxId();

    @Query(value = "select * from Student  where ?1='' or student_num like ?1 or student_name like ?1 ", nativeQuery = true)
    List<Student> findStudentListByNumNameNative(String numName);

    @Query(value = "select s from Student s where s.clazz.id=?1")
    List<Student> findStudentListByClazzId(Integer ClazzId);

    @Query(value = "select * from Student  where ?1='' or student_num like ?1 or student_name like ?1 ", nativeQuery = true)
    Optional<Student> OPFindStudentListByNumNameNative(String numName);

    @Query(value="select s from Student s where s.clazz.grade.major.id=?1 and s.clazz.grade.id=?2 and s.clazz.id=?3")
    Optional<Student> checkMajorAndGradeAndClassNative(Integer MajorId,Integer GradeId,Integer ClazzId);

    @Query(value ="select s from Student s where s.clazz.grade.major.id=?1 and s.clazz.grade.id=?2 and s.clazz.id=?3 and s.id=?4")
    Optional<Student> checkMajorAndGradeAndClassAndIdNative(Integer MajorId,Integer GradeId,Integer ClazzId,Integer StudentId);

    @Query(value="select s from Student s where s.clazz.grade.major.id=?1")
    List<Student> findStudentByMajorId(Integer MajorId);

    @Query(value = "select s from Student s where s.clazz.grade.id=?1")
    List<Student> findStudentByGradeId(Integer GradeId);

    @Query(value="select s from Student s where s.clazz.grade.major.majorNum like ?1 and s.clazz.grade.gradeNum=?2 and s.clazz.classNum=?3 and s.studentNum like ?4")
    Optional<Student> OPFindStudentByMajorGradeClazzStudentNum(String MajorNum,Integer GradeNum,Integer ClassNum,String StudentNum);

    @Query(value="select s from Student s where s.clazz.id=?1 and s.studentNum like ?2")
    List<Student> findStudentByClazzIdAndNumName(Integer clazzId,String numName);

    @Query(value="select s from Student s where s.clazz.id=?1 and s.studentNum like ?2")
    Optional<Student> OPFindStudentByClazzIdAndStudentNum(Integer clazzId,String studentNum);

    @Query(value="select s from Student s where s.clazz.grade.id=?1 and s.studentNum like ?2")
    Optional<Student> OPFindStudentByGradeIdAndStudentNum(Integer gradeId,String studentNum);

    @Query(value = "select * from Student where id = ?1 ",nativeQuery = true)
    Optional<Student> OPFindStudentById(Integer id);

    @Query(value = "select s from Student s where s.clazz.grade.major.id=?1 and s.clazz.grade.id=?2 and s.clazz.classNum=?3 and s.studentNum like ?4")
    Optional<Student> OPFindStudentByMajorAndGradeIdAndClazzAndStudentNum(Integer majorId,Integer gradeId,Integer classNum,String studentNum);
}
