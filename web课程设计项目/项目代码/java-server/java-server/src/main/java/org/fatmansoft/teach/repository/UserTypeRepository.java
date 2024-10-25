package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.EUserType;
import org.fatmansoft.teach.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(EUserType name);

    @Query(value = "select max(id) from UserType")
    Integer getMaxId();

    @Query(value="select ut from UserType ut where ut.id=?1")
    UserType findById(Integer id);
}