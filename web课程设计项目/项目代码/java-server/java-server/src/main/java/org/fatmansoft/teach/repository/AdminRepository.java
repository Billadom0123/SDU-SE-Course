package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    @Query(value = "select max(id) from Admin ")
    Integer getMaxId();

}
