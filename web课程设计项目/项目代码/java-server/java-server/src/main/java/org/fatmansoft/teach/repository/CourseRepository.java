package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.StuCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {


    @Query(value = "select * from course where id=?1",nativeQuery = true )
    Optional<Course> findCourseByCourseId(Integer id);

    //找到StuCourse表中的最大值
    @Query(value = "select max(id) from Course ")
    Integer getMaxId();

    //查询某一年级当前学期可以上的课
    @Query(value="select * from course where grade=?1 and term=?2",nativeQuery = true)
    List<Course> findCourseByGradeAndTerm(Integer grade,Integer term);

    //条件查询课程
    @Query(value = "select * from course where (course_name = '' or course_name like %?1%) and (credit=?2) and teacher_id=?3 and (prop='' or prop like %?4%)",nativeQuery = true)
    List<Course> findCourseByCourseNameAndCreditAndCreditAndTeacherIdAndProp(String CourseName,Integer Credit,Integer TeacherId,String prop);

    @Query(value = "select * from course where (course_name = '' or course_name like %?1%) and (prop='' or prop like %?2%)",nativeQuery = true)
    List<Course> findCourseByCourseNameAndProp(String CourseName,String prop);

    @Query(value="select * from course where (course_name = '' or course_name like %?1%) and (credit=?2) and (prop='' or prop like %?3%)",nativeQuery = true)
    List<Course> findCourseByCourseNameAndCreditAndProp(String courseName,Integer credit,String prop);

    @Query(value="select * from course where (course_name = '' or course_name like %?1%) and (teacher_id=?3) and (prop='' or prop like %?2%)",nativeQuery = true)
    List<Course> findCourseByCourseNameAndPropAndTeacherId(String courseName,String prop,Integer teacherId);

    @Query(value = "select c from Course c where c.teacher.id=?1")
    List<Course> findCourseByTeacherId(Integer teacherId);

    @Query(value="select c from Course c where c.teacher.id=?1 and c.term=?2")
    List<Course> findCourseByTeacherIdAndTerm(Integer TeacherId,Integer term);

}
