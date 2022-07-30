package com.yandex.practicum.test;

import com.yandex.practicum.manager.HistoryManager;
import com.yandex.practicum.manager.InMemoryTaskManager;
import com.yandex.practicum.manager.Managers;
import com.yandex.practicum.manager.TaskManager;
import com.yandex.practicum.pattern.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;


class HistoryManagerTest {
    private HistoryManager managerHistory;
    private InMemoryTaskManager managerTask;

    @BeforeEach
    public void createHistoryManager() {
        managerHistory = Managers.getDefaultHistory();

    }

    @Test
    public void shouldBeEmptyList() {
        assertTrue(managerHistory.getHistory().isEmpty());
    }



    @Test
    public void shouldBeOneTaskWhenAddTwoCloneTasks() {
        Task cloneTask = new Task("Clone Task", "Description", Status.NEW);

        managerTask.updateTask(cloneTask);
        managerHistory.add(cloneTask);
        managerHistory.add(cloneTask);
        assertEquals(1, managerHistory.getHistory().size());
    }




    @Test
    public void shouldBe2TaskAfterRemoveBeginningEndMiddle() {
        Task First =new Task("First Task", "Description", Status.NEW);
        Task Second =new Task("Second Task", "Description", Status.NEW);
        Task Third =new Task("Third Task", "Description", Status.NEW);
        Task Fourth =new Task("Fourth Task", "Description", Status.NEW);
        Task Fifth =new Task("Fifth Task", "Description", Status.NEW);

        managerHistory.add(First);
        managerHistory.add(Second);
        managerHistory.add(Third);
        managerHistory.add(Fourth);
        managerHistory.add(Fifth);

        managerHistory.remove(1);
        managerHistory.remove(3);
        managerHistory.remove(5);
        assertEquals(2, managerHistory.getHistory().size());
    }


}