package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Integer> {


    @Query(value = "select * from StudentInfo where student_id like %?1%", nativeQuery = true)
    List<StudentInfo> findStudentInfoById1 (Integer studentId);//直接获取学生家庭信息
    @Query(value = "select * from StudentInfo where student_id like %?1%", nativeQuery = true)
    Optional<StudentInfo> findStudentInfoById2 (Integer studentId);//直接获取学生家庭信息
    @Query(value = "select max(id) from StudentInfo")
    Integer getMaxId();
    @Query(value="select a from StudentInfo a where a.student.id=?1 ")
    List<StudentInfo> FindStudentInfoByStudentIdNative(Integer ID);

    @Query(value="select a from StudentInfo a where a.student.id=?1 ")
    Optional<StudentInfo> OPFindStudentInfoByStudentIdNative(Integer ID);
    @Query(value="select a from StudentInfo a where a.student.clazz.grade.major.id=?1")
    List<StudentInfo> findStudentInfoByMajorId(Integer MajorId);

    @Query(value = "select a from StudentInfo a where a.student.clazz.grade.id=?1")
    List<StudentInfo> findStudentInfoByGradeId(Integer GradeId);

    //根据学生Id找出所有符合的MyCourse实体
    @Query(value = "select * from student_info where student_id=?1", nativeQuery = true)
    List<StudentInfo> findMyInfoByStudentIdNative(Integer studentId);

    //判重方法
    @Query(value ="select * from student_info where student_id=?1",nativeQuery = true)
    Optional<StudentInfo> checkStudentInfo(Integer studentId);

    @Query(value="select a from StudentInfo a where a.student.id=?1")
    Optional<StudentInfo> OPFindStudentInfoByStudentId(Integer studentId);

}



