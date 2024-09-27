package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.Task;
import com.encora.codesys.taskmanager.repository.TaskManagerRepository;
import com.encora.codesys.taskmanager.service.TaskManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskManagerService taskManagerService;

    @Mock
    private TaskManagerRepository taskRepository;

    @InjectMocks
    private TaskManagerController taskManagerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskManagerController).build();
    }

    @Test
    void testCreateTask() throws Exception {
        Task task = new Task(1L, "Task1", "Description 1", "user1",Date.from(Instant.now()));
        when(taskManagerService.createTask(any(Task.class))).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders.post("/task-manager/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = new Task(1L, "Task1", "Description 1", "user1",Date.from(Instant.now()));
        when(taskManagerService.updateTask(any(Task.class))).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders.put("/task-manager/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task1"))
                .andDo(print());
    }

    @Test
    void testGetAllTask() throws Exception {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "Task1", "Description 1", "user1", Date.from(Instant.now())));
        taskList.add(new Task(2L, "Task2", "Description 2", "user1",Date.from(Instant.now())));

        when(taskManagerService.getAllTask()).thenReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders.get("/task-manager/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    void testGetTaskByTitle() throws Exception {
        Task task = new Task(1L, "Task1", "Description 1", "user1",Date.from(Instant.now()));
        when(taskManagerService.getTaskByTitle("Task1")).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders.get("/task-manager/tasks/Task1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testDeleteTaskById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/task-manager/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = new Task(1L, "Task1", "Description 1", "user1",Date.from(Instant.now()));
        mockMvc.perform(MockMvcRequestBuilders.delete("/task-manager/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
