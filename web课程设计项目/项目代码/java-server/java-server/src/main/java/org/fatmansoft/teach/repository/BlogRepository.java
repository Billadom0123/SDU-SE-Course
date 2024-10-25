package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Integer> {

    @Query(value = "select max(id) from Blog ")
    Integer getMaxId();

    @Query(value = "select b from Blog b where b.student.id=?1")
    List<Blog> findBlogByStudentId(Integer studentId);

    @Query(value = "select b from Blog b where b.id=?1")
    Optional<Blog> findBlogByBlogId(Integer BlogId);
}
