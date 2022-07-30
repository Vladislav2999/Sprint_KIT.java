package com.yandex.practicum.test;

import com.yandex.practicum.manager.InMemoryTaskManager;
import com.yandex.practicum.manager.TaskManager;
import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;



import static org.junit.jupiter.api.Assertions.*;


class EpicTest {


    private Epic epic;
    private TaskManager manager;

    @BeforeEach
    public void createTaskManager() {
        epic = new Epic("Name", "Description",Status.NEW);
        manager = new InMemoryTaskManager();
        manager.moveEpic(epic);
    }


    @Test
    public void shouldBeEmptySubtasksMap() {

        List<Subtask> subtasks = manager.getSubtaskInEpicAll(epic);
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldBeNewEpicWhenAllSubtaskNew() {
        Epic epicOne = new Epic("testEpic", "testEpic1Description", Status.NEW);
        Epic epicTwo = new Epic("testEpic", "testEpic1Description", Status.NEW);
        Subtask one = new Subtask("NameSubtask1", "DesSubtask1", Status.NEW, epicOne.getId());
        Subtask two = new Subtask("NameSubtask2", "DesSubtask2", Status.NEW, epicTwo.getId());
        manager.moveSubtask(one);
        manager.moveSubtask(two);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeDoneEpicWhenAllSubtaskDone() {
        Epic epicOne = new Epic("testEpic", "testEpic1Description", Status.DONE);
        Epic epicTwo = new Epic("testEpic", "testEpic1Description", Status.DONE);
        Subtask one = new Subtask("NameSubtask1", "DesSubtask1", Status.DONE, epicOne.getId());
        Subtask two = new Subtask("NameSubtask2", "DesSubtask2", Status.DONE, epicTwo.getId());

        manager.moveSubtask(one);
        manager.moveSubtask(two);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeInProgressWhenSubtaskNewAndDone() {
        Epic epicOne = new Epic("testEpic", "testEpic1Description",Status.IN_PROGRESS);
        Epic epicTwo = new Epic("testEpic", "testEpic1Description", Status.IN_PROGRESS);
        Subtask one = new Subtask("NameSubtask1", "DesSubtask1", Status.IN_PROGRESS, epicOne.getId());
        Subtask two = new Subtask("NameSubtask2", "DesSubtask2", Status.IN_PROGRESS, epicTwo.getId());
        manager.moveSubtask(one);
        manager.moveSubtask(two);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldBeInProgressEpicWhenAllSubtaskInProgress() {
        Epic epicOne = new Epic("testEpic", "testEpic1Description", Status.IN_PROGRESS);
        Epic epicTwo = new Epic("testEpic", "testEpic1Description", Status.IN_PROGRESS);
        Subtask one = new Subtask("NameSubtask1", "DesSubtask1", Status.IN_PROGRESS, epicOne.getId());
        Subtask two = new Subtask("NameSubtask2", "DesSubtask2", Status.IN_PROGRESS, epicTwo.getId());
        manager.moveSubtask(one);
        manager.moveSubtask(two);
        assertEquals(Status.NEW, epic.getStatus());
    }
}

