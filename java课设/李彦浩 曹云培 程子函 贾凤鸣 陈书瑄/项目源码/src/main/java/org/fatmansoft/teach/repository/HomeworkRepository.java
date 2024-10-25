package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Integer> {

    @Query(value = "select max(id) from Homework")
    Integer getMaxId();
    @Query(value="select a from Homework a where a.student.id=?1 ")
    List<Homework> FindHomeworkScoreByStudentIdNative(Integer ID);
    @Query(value="select * from Homework where student_id=?1 and course_id=?2",nativeQuery = true)
    Optional<Homework> checkHomework(Integer sId,Integer cId);
    @Query(value="select a from Homework a where a.student.id=?1 ")
    Optional<Homework> OPFindHomeworkScoreByCourseIdOrStudentIdNative(Integer ID);
    @Query(value="select a from Homework a where a.student.clazz.grade.major.id=?1")
    List<Homework> findHomeworkByMajorId(Integer MajorId);

    @Query(value = "select a from Homework a where a.student.clazz.grade.id=?1")
    List<Homework> findHomeworkByGradeId(Integer GradeId);

    @Query(value="select a from Homework a where a.student.clazz.grade.major.majorNum like ?1 and a.student.clazz.grade.gradeNum =?2 and a.student.clazz.classNum =?3 and a.student.studentNum like ?4 and a.course.grade.major.majorNum like ?1 and a.course.grade.gradeNum =?2 and a.course.courseNum like ?5")
    Optional<Homework> OPFindHomeworkByMajorGradeClassStudentCourseNum(String majorNum, Integer gradeNum, Integer classNum, String StudentNum, String CourseNum);

    @Query(value="select a from Homework a where a.student.id=?1 and a.course.id=?2")
    Optional<Homework> OPFindHomeworkByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value  ="select a from Homework a where a.student.id=?1 and a.course.id=?2")
    List<Homework> findHomeworkByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value = "select a from Homework a where a.course.id=?1 and a.student.clazz.id=?2")
    List<Homework> findHomeworkByCourseIdAndClazzId(Integer courseId,Integer clazzId);
    @Query(value="select a from Homework a where a.course.id=?1 and a.student.clazz.grade.id=?2")
    List<Homework> findHomeworkByCourseIdAndGradeId(Integer courseId,Integer gradeId);

    @Query(value = "select h from Homework h where h.student.id=?1")
    List<Homework> findHomeworkByStudentId(Integer studentId);
}
