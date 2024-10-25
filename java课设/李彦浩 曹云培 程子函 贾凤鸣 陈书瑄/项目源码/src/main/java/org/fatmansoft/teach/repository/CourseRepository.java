//作者:李彦浩
package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    //找课程最大号
    @Query(value = "select max(id) from Course ")
    Integer getMaxId();

    //找课程号或名字对应的课程对象
    @Query(value = "select * from Course where ?1='' or course_num like %?1% or course_name like %?1% ", nativeQuery = true)
    List<Course> findCourseListByNumNameNative(String numName);

    @Query(value = "select * from Course where ?1='' or course_num like %?1% or course_name like %?1% ", nativeQuery = true)
    Optional<Course> OPFindCourseListByNumNameNative(String numName);

    @Query(value = "select c from Course c where c.grade.id=?1")
    List<Course> findCourseListByGradeId(Integer GradeId);

    @Query(value = "select c from Course c where c.grade.major.majorNum like ?1 and c.grade.gradeNum=?2 and c.courseNum like ?3")
    Optional<Course> OPFindCourseByMajorNumAndGradeNumAndCourseNum(String MajorNum,Integer GradeNum,String CourseNum);

    @Query(value="select c from Course c where c.grade.major.id =?1")
    List<Course> findCourseByMajorId(Integer id);

    @Query(value = "select c from Course c where c.grade.id=?1 and c.courseNum like ?2")
    Optional<Course> OPFindCourseByGradeIdAndCourseNum(Integer gradeId,String courseNum);


}
