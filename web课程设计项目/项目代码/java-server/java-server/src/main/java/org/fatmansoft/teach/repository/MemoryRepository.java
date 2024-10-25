package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory,Integer> {
    @Query(value = "select max(id) from Memory ")
    Integer getMaxId();

    @Query(value = "select m from Memory m where m.user.userId=?1")
    List<Memory> findMemoryByUserId(Integer userId);
}
