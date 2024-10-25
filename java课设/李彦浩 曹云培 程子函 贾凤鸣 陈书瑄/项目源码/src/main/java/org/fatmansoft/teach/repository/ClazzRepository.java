package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Integer> {
    @Query(value="select max(id) from Clazz")
    Integer getMaxId();

    @Query(value="select * from Clazz where class_num=?1",nativeQuery = true)
    List<Clazz> FindClazzByClazzNumNative(Integer ClassNum);

    @Query(value="select * from Clazz where class_num=?1",nativeQuery = true)
    Optional<Clazz> OPFindClazzByClazzNumNative(Integer ClassNum);

    @Query(value="select c from Clazz c where c.grade.major.id=?1")
    List<Clazz> findClazzByMajorId(Integer MajorId);

    @Query(value = "select c from Clazz c where c.grade.id=?1")
    List<Clazz> findClazzByGradeId(Integer GradeId);

    @Query(value = "select c from Clazz c where c.grade.major.majorNum like ?1 and c.grade.gradeNum=?2 and c.classNum=?3")
    Optional<Clazz> OPFindClazzByMajorNumAndGradeNumAndClazzNum(String MajorNum,Integer GradeNum,Integer ClazzNum);

    @Query(value="select c from Clazz c where c.grade.id=?1 and c.classNum=?2")
    Optional<Clazz> OPFindClazzByGradeIdAndClazzNum(Integer gradeId,Integer clazzNum);
}
