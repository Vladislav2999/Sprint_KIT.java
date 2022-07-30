package com.yandex.practicum.manager;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yandex.practicum.exception.ManagerSaveException;
import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Task;
import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Type;

import static com.yandex.practicum.pattern.Type.TASK;
import static java.lang.String.valueOf;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    public static void main(String[] args) throws IOException {
        File fileForExample = new File("src/save.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForExample);

        fileBackedTasksManager.newTask(new Task("Таск 1", "Сделать Яндекс.Практикум",1, Status.NEW, Type.EPIC));
        fileBackedTasksManager.getTaskId(1);
        fileBackedTasksManager.newTask(new Task("Таск 2", "Пропылесосить",1, Status.NEW, Type.EPIC));
        fileBackedTasksManager.getTaskId(2);

        fileBackedTasksManager.getTaskId(1);
        fileBackedTasksManager.newTask(new Task("Таск 3", "Купить еды",1, Status.NEW, Type.EPIC));
        fileBackedTasksManager.newTask(new Task("Таск 4", "Купить еды",1, Status.NEW, Type.EPIC));

        fileBackedTasksManager.getTaskId(4);
        fileBackedTasksManager.getTaskId(3);


        fileBackedTasksManager.moveEpic(new Epic("Эпик 1", "Покупка квартиры",Status.NEW));
        fileBackedTasksManager.moveEpic(new Epic("Эпик 2", "Продажа дачи", Status.NEW));
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileForExample);
        System.out.println(fileBackedTasksManager2.getTaskId(1));
    }


    /**
     * Метод для восстановления данных менеджера из файла
     */
    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try {
            String str = Files.readString(Path.of(valueOf(file)));
            if (str.isEmpty())
                return fileBackedTasksManager;
            String[] lines = str.split("\n");
            int i;
            for (i = 1; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    List<Integer> history = historyFromString(lines[i + 1]);
                    for (Integer idHistory : history) {
                        if (fileBackedTasksManager.getEpicById(idHistory) != null) {
                            fileBackedTasksManager.getHistory().add(fileBackedTasksManager.getEpicById(idHistory));

                        }
                        if (fileBackedTasksManager.getSubtaskId(idHistory) != null) {
                            fileBackedTasksManager.getHistory().add(fileBackedTasksManager.getSubtaskId(idHistory));

                        }
                        if (fileBackedTasksManager.getTaskId(idHistory) != null) {
                            fileBackedTasksManager.getHistory().add(fileBackedTasksManager.getTaskId(idHistory));
                        }

                        return fileBackedTasksManager;
                    }
                }

                Task task = fileBackedTasksManager.taskFromString(lines[i]);
                fileBackedTasksManager.newTask(task);
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private Task taskFromString(String value) {
        String[] taskArray = value.split(",");
        Type type = Type.valueOf(taskArray[1]);
        int id = Integer.parseInt(taskArray[0]);
        String name = taskArray[2];
        String description = taskArray[4];
        Status status = Status.valueOf(taskArray[3]);
        Task obj = null;
        switch (type) {
            case TASK: {
                obj = new Task(name, description, id, status,type);
                obj.setId(id);
                break;
            }
            case EPIC: {
                obj = new Epic(name, description, status);
                obj.setId(id);
                obj.setStatus(status);
                break;
            }
            case SUBTASK: {
                int epicId = Integer.parseInt(taskArray[5]);
                obj = new Subtask(name, description, status, epicId);
                obj.setId(id);
                break;
            }
        }
        return obj;
    }

    /**
     *
     */
    public static List<Integer> historyFromString(String value) { // для сохранения и восстановления менеджера истории из CSV.
        List<Integer> history = new ArrayList<>();
        if (value.isEmpty()) {
            return history;
        }
        String[] line = value.split(",");
        for (String str : line) {
            history.add(Integer.parseInt(str));
        }
        return history;
    }


    static String toString(HistoryManager manager) {
        List<Task> list = manager.getHistory();
        StringBuilder builder = new StringBuilder();
        for (Task task : list) {
            builder.append(task.getId()).append(",");
        }
        return builder.toString();


    }

    private void save() {
        // сохранять текущее состояние менеджера в указанный файл
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ID,Type,Name,Status,Description,Epic, Duration, StartTime  \n");
            Collection<Task> tasks = getTaskAll();
            for (Task task : tasks) {
                writer.write(taskToString(task));
            }
            Collection<Epic> epics = getEpicAll();
            for (Epic epic : epics) {
                writer.write(epicToString(epic));
            }
            Collection<Subtask> subtasks = getSubtaskAll();
            for (Subtask subtask : subtasks) {
                writer.write(subtaskToString(subtask));
            }

            String history = toString(getManagerHistory());
            writer.write('\n');
            writer.write(history);

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private String taskToString(Task task) {
        String startTime = "null";
        String endTime = "null";
        String type = String.valueOf(task.getClass()).replace("class task.", "");

        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(formatter);
            endTime = task.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    private String epicToString(Epic epic) {
        String startTime = "null";
        String endTime = "null";
        String type = String.valueOf(epic.getClass()).replace("class task.", "");

        if (epic.getStartTime() != null) {
            startTime = epic.getStartTime().format(formatter);
            if (epic.getEndTime() == null) {
                endTime = getEpicEndTime(epic.getIdSubtask()).format(formatter);
            }
        }

        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", epic.getId(), type, epic.getName()
                , epic.getStatus(), epic.getDescription(), startTime, epic.getDuration(), endTime);
    }


    private String subtaskToString(Subtask subtask) {
        String startTime = "null";
        String endTime = "null";
        String type = String.valueOf(subtask.getClass()).replace("class task.EpicTask$", "");
        if (subtask.getStartTime() != null) {
            startTime = subtask.getStartTime().format(formatter);
            endTime = subtask.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", subtask.getId(), type, subtask.getName()
                , subtask.getStatus(), subtask.getDescription(), subtask.getEpicId(), startTime
                , subtask.getDuration(), endTime);
    }

    @Override
    public Task newTask(Task task) {
        super.newTask(task);
        save();
        return task;
    }

    @Override
    public List<Task> getTaskAll() {
        return super.getTaskAll();
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        return super.getSubtaskAll();
    }

    @Override
    public Task getTaskId(Integer taskId) {
        super.getTaskId(taskId);
        save();
        return getTaskId(taskId);
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        return super.getEpicById(epicId);
    }

    @Override
    public Subtask getSubtaskId(Integer subtaskId) {
        return super.getSubtaskId(subtaskId);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void moveEpic(Epic epic) {
        super.moveEpic(epic);
        save();
    }

    @Override
    public List<Epic> getEpicAll() {
        return super.getEpicAll();
    }

    @Override
    public void moveSubtask(Subtask subtask) {
        super.moveSubtask(subtask);
        save();
    }

    @Override
    public LocalDateTime getEpicEndTime(List<Integer> listOfSubTaskId) {
        return super.getEpicEndTime(listOfSubTaskId);
    }

    @Override
    public List<Subtask> getSubtaskInEpicAll(Epic epicId) {
        return super.getSubtaskInEpicAll(epicId);
    }

    @Override
    public void deleteTaskAll() {
        super.deleteTaskAll();
        save();
    }

    @Override
    public boolean deleteSubtaskId(Integer subtaskId) {
        boolean result = super.deleteSubtaskId(subtaskId);
        save();
        return result;
    }

    @Override
    public void deleteSubtaskAll() {
        super.deleteSubtaskAll();
        save();
    }

    @Override
    public void deleteEpicAll() {
        super.deleteEpicAll();
        save();
    }

    @Override
    public boolean updateTask(Task task) {
        if (super.updateTask(task)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (super.updateEpic(epic)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (super.updateSubtask(subtask)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTaskById(Integer taskId) {
        if (super.deleteTaskById(taskId)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteEpicById(Integer epicId) {
        if (super.deleteEpicById(epicId)) {
            save();
            return true;
        } else {
            return false;
        }
    }

}