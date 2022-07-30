package com.yandex.practicum.test;

import com.yandex.practicum.exception.ManagerSaveException;
import com.yandex.practicum.manager.Managers;
import com.yandex.practicum.manager.TaskManager;
import com.yandex.practicum.pattern.*;

import org.junit.jupiter.api.Test;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
protected T manager;

        @Test
        public void shouldBeHaveSubtaskEpic() {
            Epic epic = new Epic("Name", "Description", Status.NEW );
            Subtask subtask = new Subtask("nameSub", "desSub", 60,
                    "06.05.2022 05:00", Status.NEW, 1);
            manager.moveEpic(epic);
            manager.moveSubtask(subtask);
            Subtask forCheck = manager.getSubtaskInEpicAll(epic).get(0);
            assertEquals(epic.getId(), forCheck.getEpicId());
        }

        @Test
        public void shouldBeHaveEpicStatusInProgress() {
            Epic epic = new Epic("Name", "Description", Status.NEW);
            Subtask newSubtask = new Subtask("nameSubNew", "desSubNew", 60,
                    "06.05.2022 05:00", Status.NEW, 1);
            Subtask subtaskDone = new Subtask("nameSubDone", "desSubDone", 60,
                    "06.05.2022 06:00", Status.DONE, 1);
            manager.moveEpic(epic);
            manager.moveSubtask(newSubtask);
            manager.moveSubtask(subtaskDone);
            assertEquals(Status.NEW, epic.getStatus());
        }

        @Test
        public void shouldBeHave1Task() {
            manager.updateTask(new Task("Name", "Description", Status.NEW));
            assertEquals(0, manager.getTaskAll().size());
        }



        @Test
        public void shouldBeEmptyTasks() {
            assertEquals(0, manager.getTaskAll().size());
        }

        @Test
        public void shouldBeHave1Epic() {
            manager.moveEpic(new Epic("Name", "Description",Status.NEW));
            assertEquals(1, manager.getEpicAll().size());
        }

        @Test
        public void shouldBeEmptyEpics() {
            assertEquals(0, manager.getEpicAll().size());
        }

        @Test
        public void shouldBe1Subtask() {
            Epic epic = new Epic("Name", "Description",Status.NEW);
            Subtask subtask = new Subtask("Name", "Description", 60,
                    "06.05.2022 05:00", Status.NEW, 1);
            manager.moveEpic(epic);
            manager.moveSubtask(subtask);
            assertEquals(0, manager.getSubtaskAll().size());
        }

        @Test
        public void shouldBeEmptySubtasks() {
            assertEquals(0, manager.getSubtaskAll().size());
        }

        @Test
        public void shouldBe1SubtaskInAddedEpic() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            Subtask subtask = new Subtask("SubName", "SubDescription", 60,
                    "06.05.2022 05:00", Status.NEW, 1);
            manager.moveEpic(epic);
            manager.moveSubtask(subtask);
            assertEquals(0, epic.getIdSubtask().size());
        }

        @Test
        public void shouldBeEmptyInEpicList() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            manager.moveEpic(epic);
            assertEquals(0, epic.getIdSubtask().size());
        }

        @Test
        public void shouldBeEmptyWhenHaveAndRemoveAll() {
            manager.updateTask(new Task("Name", "Des", Status.NEW));
            manager.deleteTaskAll();
            assertEquals(0, manager.getTaskAll().size());
        }

        @Test
        public void shouldBeEmptyWhenRemoveEmptyManager() {
            manager.deleteTaskAll();
            assertEquals(0, manager.getTaskAll().size());
        }

        @Test
        public void shouldBeNormalWhenManagerHaveThisTask() {
            Task task = new Task("Name", "Description",Status.NEW);
            manager.updateTask(task);
            assertEquals(null, manager.getTaskId(1));
        }

        @Test
        public void shouldBeNullWhenGetNumberNotHaveManager() {
            assertNull(manager.getTaskId(14));
        }

        @Test
        public void shouldBeNormalRemoving() {
            Task task = new Task("Name", "Description", Status.NEW);
            manager.updateTask(task);
            manager.deleteTaskById(1);
            assertNull(manager.getTaskId(1));
        }

        @Test
        public void shouldBeErrorWhenRemoveIncorrectId() {
            Error ex = assertThrows(Error.class, () -> manager.deleteTaskById(14));
            assertEquals("Такой задачи нет", ex.getMessage());
        }
        @Test
        public void shouldBeErrorValidation(){
            Task testTask = new Task("Name1", "Description1", 60,
                    "06.05.2022 05:00", Status.NEW);
            manager.updateTask(testTask);
            Task testTaskError = new Task("Name1", "Description1", 60,
                    "06.05.2022 05:00", Status.NEW);
            assertThrows(ManagerSaveException.class,
                    () -> manager.updateTask(testTaskError),
                    "Новая задача не входит внутрь существующей");
        }

        @Test
        public void shouldBeNullWhenCheckEndTimeWithEmptySubtasks() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            assertNull(epic.getEndTime());
        }

        @Test
        public void shouldBeHave60MinutesDurationEpicWithOneSubtask() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            manager.moveEpic(epic);
            Subtask one = new Subtask("name1", "des1", 60,
                    "06.05.2022 05:00", Status.NEW, 1);
            manager.moveSubtask(one);
            assertEquals("06.05.2022 06:00", epic.getEndTime().format(epic.getFormatter()));
        }

        @Test
        public void shouldBeHave60MinutesDurationEpicWithThreeSubtasksAndOneStartTime() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            manager.moveEpic(epic);
            Subtask one =new Subtask("name1", "des1", 20,
                    "06.05.2022 05:00", Status.NEW, 1);
            Subtask two =new Subtask("name2", "des2", 20,
                    "06.05.2022 06:00", Status.NEW, 1);
            Subtask three =new Subtask("name3", "des3", 20,
                    "06.05.2022 07:00", Status.NEW, 1);
            manager.moveSubtask(one);
            manager.moveSubtask(two);
            manager.moveSubtask(three);
            assertEquals("06.05.2022 07:20", epic.getEndTime().format(epic.getFormatter()));
        }

        @Test
        public void shouldBeHaveMinimalStartTimeWithTreeSubtasks() {
            Epic epic = new Epic("EpicName", "EpicDescription",Status.NEW);
            manager.moveEpic(epic);
            Subtask one =new Subtask("name1", "des1", 20,
                    "07.05.2022 05:00", Status.NEW, 1);
            Subtask two =new Subtask("name2", "des2", 20,
                    "07.05.2022 06:00", Status.NEW, 1);
            Subtask three =new Subtask("name3", "des3", 20,
                    "07.05.2022 07:00", Status.NEW, 1);

            manager.moveSubtask(one);
            manager.moveSubtask(two);
            manager.moveSubtask(three);
            assertEquals("07.05.2022 07:20", (epic.getEndTime()).format(epic.getFormatter()));
        }
    }