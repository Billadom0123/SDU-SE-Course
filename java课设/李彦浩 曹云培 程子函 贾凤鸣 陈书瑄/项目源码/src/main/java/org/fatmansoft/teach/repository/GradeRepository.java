package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {
    @Query(value="select max(id) from Grade")
    Integer getMaxId();

    @Query(value="select * from Grade where grade_num=?1",nativeQuery = true)
    List<Grade> FindGradeByGradeNumNative(Integer GradeNum);

    @Query(value="select * from Grade where grade_num=?1",nativeQuery = true)
    Optional<Grade> OPFindGradeByGradeNumNative(Integer GradeNum);

    @Query(value="select g from Grade g where g.major.id=?1")
    List<Grade> findGradeByMajorId(Integer MajorId);

    @Query(value = "select g from Grade g where g.major.majorNum like ?1 and g.gradeNum=?2")
    Optional<Grade> OPFindGradeByMajorNumAndGradeNum(String MajorNum,Integer GradeNum);

    @Query(value="select g from Grade g where g.major.id=?1 and g.gradeNum=?2")
    Optional<Grade> OPFindGradeByMajorIdAndGradeNum(Integer majorId,Integer gradeNum);

}
