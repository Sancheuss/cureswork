package com.suai.cureswork.crud.repo;

import com.suai.cureswork.crud.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {
    List<User> findAll();
    Optional<User> findByName(String name);
}
