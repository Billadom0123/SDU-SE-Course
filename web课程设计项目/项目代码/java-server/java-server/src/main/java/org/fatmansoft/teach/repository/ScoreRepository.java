package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {
//    @Query(value = "select max(id) from Score  ")
//    Integer getMaxId();
//    List<Score> findByStudentId(Integer studentId);
//    List<Score> findByCourseCourseNum(String courseNum);
//    @Query(value="select s.course from Score s where s.student.id=?1")
//    List<Course> findCourseList(Integer studentId);
//
//    @Query(value="from Score  where course.courseNum like ?1")
//    List<Score> findScoreList(String courNum);
//
//    @Query(value="from Score  where ?1='' or student.studentNum like %?1% or student.studentName like %?1% or course.courseNum like %?1% or course.courseName like %?1%")
//    List<Course> findCourseListByInput(String  input);
//
//    @Query(value="from Score  where student.id = ?1 and( course.courseNum like %?2% or course.courseName like %?2%)")
//    List<Course> findCourseListByInput(Integer studentId, String  input);
    @Query(value = "select max(id) from Score")
    Integer getMaxId();

    @Query(value = "select * from score where student_id=?1",nativeQuery = true)
    List<Score> findScoreByStudentId(Integer id);

    @Query(value = "select * from score where course_id=?1",nativeQuery = true)
    List<Score> findScoreByCourseId(Integer id);

    @Query(value = "select s from Score s where s.student.id=?1 and s.course.grade=?2 and s.course.term=?3")
    List<Score> findScoreByStudentIdAndGradeAndTerm(Integer studentId,Integer grade,Integer term);

    @Query(value = "select s from Score s where s.course.courseName like %?2% and s.student.id=?1")
    List<Score> findScoreByCourseName(Integer studentId,String courseName);

    @Query(value = "select s from Score s where s.student.id=?1 and s.course.grade=?2 and s.course.term=?3 and s.course.courseName like %?4%")
    List<Score> findScoreByStudentIdAndGradeAndTermAndCourseName(Integer studentId,Integer grade,Integer term,String courseName);

}
