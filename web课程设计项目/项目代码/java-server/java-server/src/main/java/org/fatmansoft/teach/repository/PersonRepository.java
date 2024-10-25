package org.fatmansoft.teach.repository;

import org.aspectj.apache.bcel.classfile.Module;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query(value = "select max(personId) from Person ")
    Integer getMaxId();

    @Query(value = "select * from person where person_id=?1",nativeQuery = true)
    Optional<Person> findPersonById(Integer id);

    @Query(value = "select * from person where (per_name='' or per_name like %?1%) and user_type_id=3",nativeQuery = true)
    List<Person> findTeacherByName(String teacherName);



}
