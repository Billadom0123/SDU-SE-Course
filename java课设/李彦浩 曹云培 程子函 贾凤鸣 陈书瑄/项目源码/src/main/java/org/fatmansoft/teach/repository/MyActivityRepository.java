//作者:曹云培
package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.MyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyActivityRepository extends JpaRepository<MyActivity,Integer> {

    @Query(value = "select max(id) from MyActivity")
    Integer getMaxId();

    //根据学生找对应的Innovation,“小明的创新项目信息为balabala”
    @Query(value = "select * from my_activity where student_id=?1",nativeQuery = true)
    List<MyActivity> findMyActivityByStudentId(Integer num);

    @Query(value = "select * from my_activity where pe=?1 ",nativeQuery = true)
    List<MyActivity> findMyActivityByPE(String PE);

    @Query(value = "select * from my_activity where performance=?1 ",nativeQuery = true)
    List<MyActivity> findMyActivityByPerformance(String performance);

    @Query(value = "select * from my_activity where travel=?1 ",nativeQuery = true)
    List<MyActivity> findMyActivityByTravel(String travel);

    @Query(value = "select * from my_activity where party=?1 ",nativeQuery = true)
    List<MyActivity> findMyActivityByParty(String party);

    @Query(value="select a from MyActivity a where a.student.clazz.id=?1 ")
    List<MyActivity> FindMyActivityByClazzIdNative(Integer clazzId);


}