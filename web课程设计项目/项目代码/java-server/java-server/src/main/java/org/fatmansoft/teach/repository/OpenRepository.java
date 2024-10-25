package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Open;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenRepository extends JpaRepository<Open,Integer> {
    @Query(value = "select max(id) from Open ")
    Integer getMaxId();
}
