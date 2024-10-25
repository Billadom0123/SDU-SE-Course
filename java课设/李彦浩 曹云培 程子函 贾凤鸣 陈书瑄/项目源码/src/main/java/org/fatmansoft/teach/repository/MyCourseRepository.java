package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Clazz;
import org.fatmansoft.teach.models.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
//目标:在点击link后，初始化页面的方法中可以通过接口中提供的方法根据studentId查到所有的Course实体
public interface MyCourseRepository extends JpaRepository<MyCourse,Integer> {

    @Query(value="select max(id) from MyCourse")
    Integer getMaxId();

    //根据学生Id找出所有符合的MyCourse实体
    @Query(value = "select * from my_course where student_id=?1",nativeQuery = true)
    List<MyCourse> findMyCourseByStudentIdNative(Integer studentId);

    //根据课程Id找出符合的MyCourse实体
    @Query(value = "select * from my_course where course_id=?1",nativeQuery = true)
    List<MyCourse> findMyCourseByCourseIdNative(Integer courseId);

    //判重方法
    @Query(value ="select * from my_course where student_id=?1 and course_id=?2",nativeQuery = true)
    Optional<MyCourse> checkMyCourse(Integer studentId,Integer courseId);

    //判重
    @Query(value = "select mc from MyCourse mc where mc.student.clazz.grade.major.majorNum like ?1 and mc.student.clazz.grade.gradeNum=?2 and mc.student.clazz.classNum=?3 and mc.student.studentNum like ?4 and mc.course.grade.major.majorNum like ?1 and mc.course.grade.gradeNum=?2 and mc.course.courseNum like ?5")
    Optional<MyCourse> OPFindMyCourseByMajorGradeClazzStudentCourseNum(String majorNum,Integer gradeNum,Integer classNum,String studentNum,String courseNum);

    @Query(value="select mc from MyCourse mc where mc.student.clazz.grade.major.id=?1")
    List<MyCourse> findMyCourseByMajorId(Integer MajorId);

    @Query(value = "select mc from MyCourse mc where mc.student.clazz.grade.id=?1")
    List<MyCourse> findMyCourseByGradeId(Integer GradeId);

    @Query(value = "select mc from MyCourse mc where mc.student.id=?1 and mc.course.id=?2")
    Optional<MyCourse> OPFindMyCourseByStudentIdAndCourseId(Integer studentId,Integer courseId);

    @Query(value = "select mc from MyCourse mc where mc.student.id=?1")
    List<MyCourse> findMyCourseByStudentId(Integer studentId);

}

