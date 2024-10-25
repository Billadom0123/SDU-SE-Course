package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.StuCourse;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StuCourseRepository extends JpaRepository<StuCourse,Integer> {
    @Query(value = "select max(id) from StuCourse ")
    Integer getMaxId();

    @Query(value = "select * from stu_course where student_id=?1",nativeQuery = true)
    List<StuCourse> findCourseByStudentId(Integer Id);

    @Query(value="select * from stu_course where student_id=?1 and course_id=?2",nativeQuery = true)
    Optional<StuCourse> findStudentCourse(Integer studentId, Integer courseId);

    @Query(value="select sc from StuCourse sc where sc.course.id=?1")
    List<StuCourse> findCourseByCourseId(Integer courseId);
}
