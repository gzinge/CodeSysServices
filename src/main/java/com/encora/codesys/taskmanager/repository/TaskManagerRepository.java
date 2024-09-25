package com.encora.codesys.taskmanager.repository;

import com.encora.codesys.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagerRepository extends CrudRepository<Task, Long> {
    Task findByTitle(String title);
}
