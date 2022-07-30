package com.yandex.practicum.test;

import com.yandex.practicum.manager.FileBackedTasksManager;
import com.yandex.practicum.manager.Managers;
import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    public void createManager() {
        super.manager = Managers.getDefaultFileBackedManager("src/save.csv");
        file =new File("src/save.csv");
    }

    @AfterEach
    public void remove() {
        assertTrue(file.delete());
    }

    @Test
    public void shouldBeEmptyWhenloadFromFileEmptyAndloadFromFileEmptyTasks() {
        manager.deleteTaskAll();
        manager.deleteEpicAll();
        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertTrue(loadFromFile.getTaskAll().isEmpty() && loadFromFile.getEpicAll().isEmpty());
    }

    @Test
    public void shouldBeEpicTaskSubtaskInloadFromFileFile() {
        manager.deleteEpicAll();
        manager.deleteSubtaskAll();
        manager.deleteTaskAll();
        Epic epicTest = new Epic("NameEpic", "Description",Status.NEW);
        manager.moveEpic(epicTest);
        Subtask subtaskTest = new Subtask("Name1", "Description1", 60,
                "06.05.2022 05:00", Status.NEW, 1);
        manager.moveSubtask(subtaskTest);
        Task taskTest=new Task("NameTask","Description",Status.NEW);
        manager.updateTask(taskTest);
        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(epicTest, loadFromFile.getEpicById(1));
        assertEquals(subtaskTest, loadFromFile.getSubtaskId(2));
        assertEquals(taskTest,loadFromFile.getTaskId(3));
    }


    @Test
    public void shouldBeEmptyWhenloadFromFileEmptyAndloadFromFileEmptyHistory() {
        manager.deleteSubtaskAll();
        manager.deleteTaskAll();
        manager.deleteEpicAll();
        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, loadFromFile.getHistory().size());
    }

    @Test
    public void shouldBeWhenloadFromFileAndloadFromFileHistory() {
        Task taskTest=new Task("Name","desc", Status.NEW);
        manager.updateTask(taskTest);
        List<Task> temp=new ArrayList<>();
        temp.add(manager.getTaskId(1));
        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(temp,loadFromFile.getHistory());
    }
    @Test
    public void shouldBeWhenloadFromFileAndSortedList() {

        Task one=new Task("name1", "des1", 20,
                "07.05.2022 05:00", Status.NEW);
        Task two =new Task("name2", "des2", 20,
                "08.05.2022 05:00", Status.NEW);
        Task three =new Task("name3", "des3", 20,
                "06.05.2022 05:00", Status.NEW);

        manager.updateTask(one);
        manager.updateTask(two);
        manager.updateTask(three);


        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(manager.getPrioritizedTasks(),loadFromFile.getPrioritizedTasks());
    }


}