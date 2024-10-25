package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface MajorRepository extends JpaRepository<Major,Integer> {
    @Query(value="select max(id) from Major")
    Integer getMaxId();

    @Query(value="select * from Major where ?1='' or major_num like ?1 or major_name like ?1",nativeQuery = true)
    List<Major> findMajorByNumNameNative(String NumName);

    @Query(value="select * from Major where major_num=?1",nativeQuery = true)
    List<Major> findMajorByMajorNumNative(Integer MajorNum);

    @Query(value="select * from Major where major_name like ?1",nativeQuery = true)
    Optional<Major> OPFindMajorByMajorNameNative(String MajorName);

    @Query(value="select mj from Major mj where mj.majorNum like ?1")
    Optional<Major> OPFindMajorByMajorNumNative(String MajorNum);

    @Query(value="select * from Major where major_num=?1",nativeQuery = true)
    Optional<Major> OPFindMajorByMajorNumNative(Integer MajorNum);

}
