package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Practice;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository  extends JpaRepository<Practice, Integer> {

    @Query(value = "select max(id) from Practice ")
    Integer getMaxId();

    @Query(value = "select p from Practice p where p.student.id=?1")
    List<Practice> findPracticeByStudentId(Integer studentId);

    @Query(value = "select p from Practice p where p.term=?1 and p.grade=?2 and p.level like %?3% and p.status like %?4% and p.date like %?5%")
    List<Practice> findPracticeByTermAndGradeAndLevelAndStatusAndDate(Integer term,Integer grade,String level,String status,String date);

    @Query(value="select p from Practice p where p.level like %?1% and p.status like %?2% and p.date like %?3%")
    List<Practice> findPracticeByLevelAndStatusAndDate(String level,String status,String date);

}
