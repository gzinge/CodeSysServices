package com.encora.codesys.taskmanager.repository;

import com.encora.codesys.taskmanager.entity.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {
    Users findByUsername(String username);
}
