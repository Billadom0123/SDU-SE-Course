package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


public interface HonorRepository extends JpaRepository<Honor,Integer> {
    @Query(value = "select max(id) from Honor  ")
    Integer getMaxId();

    @Query(value = "select * from Honor",nativeQuery = true)
    List<Honor> findHonor();
    @Query(value="select a from Honor a where a.student.id=?1 ")
    List<Honor> FindHonorByStudentIdNative(Integer ID);

    @Query(value="select a from Honor a where a.id=?1 ")
    Optional<Honor> FindHonorById(Integer ID);


    @Query(value = "select a from Honor a where a.title=?1 or a.reward=?1")
    Optional<Honor> OPFindHonorListByTitleRewardNative(String title,String reward);

    @Query(value ="select * from honor where student_id=?1 and title=?2 and reward=?3",nativeQuery = true)
    Optional<Honor> checkMyHonor(Integer studentId, String title,String reward);

    @Query(value="select a from Honor a where a.student.clazz.id=?1 ")
    List<Honor> FindHonorByClazzIdNative(Integer clazzId);

    @Query(value="select a from Honor a where a.student.clazz.grade.id=?1 ")
    List<Honor> FindHonorByGradeIdNative(Integer gradeId);

    @Query(value = "select a from Honor a where a.title=?1 and a.student.clazz.id=?2")
    List<Honor> findHonorByTitleAndClazzId(String title, Integer clazzId);

    @Query(value = "select a from Honor a where a.title=?1 and a.student.clazz.grade.id=?2")
    List<Honor> findHonorByTitleAndGradeId(String title, Integer gradeId);
}
