package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Assessment;
import org.fatmansoft.teach.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Integer> {

    @Query(value = "select max(id) from Assessment ")
    Integer getMaxId();
    @Query(value = "select a from Assessment a where a.receive.id=?1")
    List<Assessment> findAssessmentByReceive(Integer studentId);

    @Query(value="select a from Assessment a where a.deliver.id=?1")
    List<Assessment> findAssessmentByDeliver(Integer studentId);

    @Query(value="select a from Assessment a where a.describe=?1")
    List<Assessment> findAssessmentByAccurateDescribe(String describe);

    @Query(value = "select a from Assessment a where a.describe=?1 and a.receive.id=?2")
    List<Assessment> findAssessmentByDescribeAndAndReceive(String describe,Integer receive);

    @Query(value="select a from Assessment a where a.receive.id=?1 and a.checked=?2")
    List<Assessment> findAssessmentByReceiveAndChecked(Integer studentId,String checked);
}
