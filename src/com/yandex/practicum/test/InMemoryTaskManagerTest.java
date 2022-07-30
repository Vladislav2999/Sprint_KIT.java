package com.yandex.practicum.test;

import com.yandex.practicum.manager.InMemoryTaskManager;


import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private InMemoryTaskManager managerTask;
    @BeforeEach
    public void createManager() {
        super.manager = new InMemoryTaskManager();
    }
    @Test
    void shouldReturnListOfPrioritizedTasks() {
        Task task1 = new Task("testTask", "testDescription", Status.NEW);
        Task task2 = new Task("testTask", "testDescription", Status.NEW);
        managerTask.updateTask(task1);
        managerTask.updateTask(task2);
        Set<Task> sorted = managerTask.getPrioritizedTasks();

        assertEquals(sorted.stream().findFirst(), task1);
        assertEquals(sorted.stream().findAny(), task2);
    }



}