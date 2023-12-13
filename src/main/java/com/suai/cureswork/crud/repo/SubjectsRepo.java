package com.suai.cureswork.crud.repo;

import com.suai.cureswork.crud.entity.Subjects;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsRepo  extends CrudRepository<Subjects, Integer> {
    List<Subjects> findAll();
    List<Subjects> findByGroup(String group);

    Subjects findByGroupAndSubject(String group, String subject);
}
