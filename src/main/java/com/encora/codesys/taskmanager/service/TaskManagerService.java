package com.encora.codesys.taskmanager.service;

import com.encora.codesys.taskmanager.entity.Task;
import com.encora.codesys.taskmanager.repository.TaskManagerRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskManagerService {

    @Autowired
    private TaskManagerRepository taskRepository;

    public List<Task> getAllTask() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task updatedTask) {
        if (updatedTask.getId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null for update operation");
        }
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    private Set<Task> testData() {
        return Collections.asSet(
                Arrays.asList(
                        new Task(1L, "T1", "Pending", "test", new Date()),
                        new Task(2L, "T2", "Pending", "test", new Date()),
                        new Task(3L, "T3", "Pending", "test", new Date())
                )
        );
    }

}
