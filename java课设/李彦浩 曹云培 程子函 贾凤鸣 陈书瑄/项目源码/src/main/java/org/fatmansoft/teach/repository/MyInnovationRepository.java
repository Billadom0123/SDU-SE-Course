package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.MyCourse;
import org.fatmansoft.teach.models.MyInnovation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface MyInnovationRepository extends JpaRepository<MyInnovation,Integer> {
    @Query(value="select max(id) from MyInnovation")
    Integer getMaxId();

    @Query(value="select * from my_innovation where student_id=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByStudentId(Integer studentId);

    @Query(value = "select mc from MyInnovation mc where mc.student.clazz.grade.major.majorNum like ?1 and mc.student.clazz.grade.gradeNum=?2 and mc.student.clazz.classNum=?3 and mc.student.studentNum like ?4 ")
    Optional<MyCourse> OPFindMyInnovationByMajorGradeClazzStudentCourseNum(String majorNum,Integer gradeNum,Integer classNum,String studentNum);

    @Query(value = "select * from my_innovation where practice=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByPractice(String practice);

    @Query(value = "select * from my_innovation where competition=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByCompetition(String competition);

    @Query(value = "select * from my_innovation where sci_achieve=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationBySciAchieve(String sciAchieve);

    @Query(value = "select * from my_innovation where ino_project=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByInoProject(String inoProject);

    @Query(value = "select * from my_innovation where lecture=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByLecture(String lecture);

    @Query(value = "select * from my_innovation where internship=?1",nativeQuery = true)
    List<MyInnovation> findMyInnovationByInternship(String internship);

    //@Query(value="select mi from MyInnovation mi where mi.student.id=?1")
    //List<MyInnovation> findMyInnovationByStudentId(Integer studentId);

    @Query(value="select s from MyInnovation s where s.student.clazz.id=?1")
    List<MyInnovation> findMyInnovationByclazzId(Integer studentId);
}
