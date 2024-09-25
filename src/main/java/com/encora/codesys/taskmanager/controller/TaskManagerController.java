package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.Task;
import com.encora.codesys.taskmanager.service.TaskManagerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-manager")
public class TaskManagerController {

    @Autowired
    private TaskManagerService service;

    @GetMapping("/tasks")
    public List<Task> getAllTask() {
        return service.getAllTask();
    }

    @GetMapping("/tasks/{title}")
    public ResponseEntity<Task> getTaskByTitle(String title) {
        return new ResponseEntity<>(service.getTaskByTitle(title), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = service.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        Task updatedTask = service.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<Void> deleteTask(@RequestBody Task task) {
        service.deleteTask(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
