package com.suai.cureswork.crud.repo;

import com.suai.cureswork.crud.entity.Groups;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepo extends CrudRepository<Groups, String> {
    List<Groups> findAll();

    Groups findByGroup(String group);

   // @Query(value = "delete from groups g where g.group = :group", nativeQuery = true)
   // void deleteByGroupNative(@Param("group") String group);
}
