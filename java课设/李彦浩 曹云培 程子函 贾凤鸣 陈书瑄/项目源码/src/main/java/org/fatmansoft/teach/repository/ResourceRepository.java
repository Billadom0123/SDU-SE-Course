package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource,Integer>  {
    @Query(value = "select max(id) from Honor  ")
    Integer getMaxId();
    /* @Query(value="select * from Resource where course_id like %?1%",nativeQuery = true)
    List<Resource> findByCourseId(Integer courseId);在Resource中用courseId查询textbook(课本）、courseware（课件）、reference（参考资料）*/
    @Query(value="select * from Resource",nativeQuery = true)
    List<Resource> findResource();

    @Query(value="select a from Resource a where a.course.id=?1 ")
    List<Resource> FindResourceByCourseIdNative(Integer ID);//在Resource中用courseId查询textbook(课本）、courseware（课件）、reference（参考资料）

    @Query(value ="select * from resource where course_id=?1 and text_books=?2 and course_ware=?3 and reference=?4",nativeQuery = true)
    Optional<Resource> checkMyResource(Integer studentId, String textBooks, String courseWare,String reference);
}
