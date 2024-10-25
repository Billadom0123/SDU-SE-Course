package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    @Query(value = "select max(id) from Teacher ")
    Integer getMaxId();

    @Query(value = "select * from teacher where person_id = ?1",nativeQuery = true)
    Optional<Teacher> findTeacherByPersonId(Integer PersonId);

    @Query(value="select t from Teacher t where t.user.userId=?1")
    Teacher findTeacherByUserId(Integer id);
}
