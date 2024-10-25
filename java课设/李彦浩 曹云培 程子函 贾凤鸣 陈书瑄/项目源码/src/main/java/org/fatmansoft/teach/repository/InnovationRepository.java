package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Innovation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InnovationRepository extends JpaRepository<Innovation,Integer>{

    @Query(value = "select max(id) from Innovation ")
    Integer getMaxId();

    //根据学生找对应的Innovation,“小明的创新项目信息为balabala”
    @Query(value = "select * from Innovation where ?1=' ' or student_id like %?1% " , nativeQuery = true)
    List<Innovation> findInnovationByStudentId(Integer num);

    @Query(value = "select a from Innovation a where a.student.id=?1 ")
    List<Innovation> findInnovationByStudentIdNative(Integer ID);

    @Query(value = "select i from Innovation i where i.student.clazz.grade.major.majorNum like ?1 and i.student.clazz.grade.gradeNum=?2 ")
    Optional<Innovation> OPFindInnovationByMajorNumAndGradeNumAndCourseNum(String MajorNum, Integer GradeNum);

    @Query(value = "select a from Innovation a where ?1=' ' or a.student.id=?1 ")
    Optional<Innovation> OpFindInnovationByStudentIdNative(Integer ID);
    //根据各种活动找学生
    @Query(value = "select * from Innovation where ?1='' or practice like %?1%  ",nativeQuery = true )
    List<Innovation> findStudentByPractice(String practice);

    @Query(value = "select * from Innovation where ?1='' or competition like %?1% " ,nativeQuery = true)
    List<Innovation> findStudentByCompetition(String competition);

    @Query(value = "select * from Innovation where ?1='' or sciAchieve like %?1% " ,nativeQuery = true)
    List<Innovation> findStudentBySciAchieve(String sciAchieve);

    @Query(value = "select * from Innovation where ?1='' or lecture like %?1% " ,nativeQuery = true)
    List<Innovation> findStudentByLecture(String lecture);

    @Query(value = "select * from Innovation where ?1='' or inoProject like %?1% " ,nativeQuery = true)
    List<Innovation> findStudentByInoProject(String inoProject);

    @Query(value = "select * from Innovation where ?1='' or internship like %?1% " ,nativeQuery = true)
    List<Innovation> findStudentByInternship(String internship);
}
