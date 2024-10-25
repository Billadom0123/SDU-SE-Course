//作者：李彦浩
package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    //主键自增方法
    @Query(value = "select max(id) from Achievement  ")
    Integer getMaxId();

    @Query(value="select a from Achievement a where a.student.id=?1 ")
    List<Achievement> FindScoreByStudentIdNative(Integer StudentId);//更應該說是findAchievementByStudentId

    @Query(value="select * from Achievement where course_id=?1 ",nativeQuery = true)
    List<Achievement> FindScoreByCourseIdNative(Integer CourseId);

    @Query(value="select a from Achievement a where a.student.id=?1 ")
    Optional<Achievement> OPFindScoreByCourseIdOrStudentIdNative(Integer ID);

    @Query(value="select * from Achievement where student_id=?1 and course_id=?2",nativeQuery = true)
    Optional<Achievement> checkAchievement(Integer sId,Integer cId);

    @Query(value="select a from Achievement a where a.student.clazz.grade.major.id=?1")
    List<Achievement> findAchievementByMajorId(Integer MajorId);

    @Query(value = "select a from Achievement a where a.student.clazz.grade.id=?1")
    List<Achievement> findAchievementByGradeId(Integer GradeId);

    @Query(value="select a from Achievement a where a.student.clazz.grade.major.majorNum like ?1 and a.student.clazz.grade.gradeNum =?2 and a.student.clazz.classNum =?3 and a.student.studentNum like ?4 and a.course.grade.major.majorNum like ?1 and a.course.grade.gradeNum =?2 and a.course.courseNum like ?5")
    Optional<Achievement> OPFindAchievementByMajorGradeClassStudentCourseNum(String majorNum,Integer gradeNum,Integer classNum,String StudentNum,String CourseNum);

    @Query(value="select a from Achievement a where a.student.id=?1 and a.course.id=?2")
    Optional<Achievement> OPFindAchievementByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value  ="select a from Achievement a where a.student.id=?1 and a.course.id=?2")
    List<Achievement> findAchievementByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value = "select a from Achievement a where a.course.id=?1 and a.student.clazz.id=?2")
    List<Achievement> findAchievementByCourseIdAndClazzId(Integer courseId,Integer clazzId);

    @Query(value="select a from Achievement a where a.course.id=?1 and a.student.clazz.grade.id=?2")
    List<Achievement> findAchievementByCourseIdAndGradeId(Integer courseId,Integer gradeId);


}
