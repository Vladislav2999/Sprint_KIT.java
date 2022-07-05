package com.yandex.practicum.manager;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yandex.practicum.exception.ManagerSaveException;
import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Task;
import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Type;




public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {


     File file ;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public FileBackedTasksManager(File file) {
        this.file= file;
    }


    public static void main(String[] args) throws IOException {
        File fileForExample = new File("src/service/save.csv");
        final TaskManager managerDefault = Managers.getDefault();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForExample);

        fileBackedTasksManager.newTask(new Task("Таск 1", "Сделать Яндекс.Практикум", Status.NEW));
        fileBackedTasksManager.getTaskId(1);
        fileBackedTasksManager.newTask(new Task("Таск 2", "Пропылесосить", Status.NEW));
        fileBackedTasksManager.getTaskId(2);

        fileBackedTasksManager.getTaskId(1);
        fileBackedTasksManager.newTask(new Task("Таск 3","Купить еды", Status.NEW));
        fileBackedTasksManager.newTask(new Task("Таск 4", "Купить еды", Status.NEW));

        fileBackedTasksManager.getTaskId(4);
        fileBackedTasksManager.getTaskId(3);


        fileBackedTasksManager.moveEpic(new Epic("Эпик 1", "Покупка квартиры", Status.NEW));
        fileBackedTasksManager.moveEpic(new Epic("Эпик 2", "Продажа дачи", Status.NEW));
        File fileForExmaple2 = new File("src/service/InFile/saveReports2.csv");
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileForExmaple2);
        System.out.println(fileBackedTasksManager2.getTaskId(1));
    }


        /**
         * Метод для восстановления данных менеджера из файла
         */
        public static FileBackedTasksManager loadFromFile(File file)  throws IOException {

            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
            String s = Files.readString(Path.of(String.valueOf(file)));
            if (s.isEmpty())
                return fileBackedTasksManager;
            String[] lines = s.split("\n");
            int i = 1;
            while (!lines[i].isEmpty()) {
                Task task = fileBackedTasksManager.taskFromString(lines[i]);
                ++i;
                fileBackedTasksManager.newTask(task);

                if (lines.length == i)
                    break;
            }
            if (i == lines.length) {
                return fileBackedTasksManager;
            } else {
                List<Integer> history = historyFromString(lines[lines.length - 1]);
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
            return fileBackedTasksManager;
        }




    private File fileExists (File file) throws ManagerSaveException {
            if (file.exists()) {
                System.out.println("Файл " + file.getName() + " создан");
                if (file.canRead()) {
                    System.out.println("Доступен для чтения");
                } else {
                    throw new ManagerSaveException("Недоступен для чтения");
                }

                if (file.canWrite()) {
                    System.out.println("Доступен для записи");
                } else {
                    throw new ManagerSaveException("Недоступен для записи");
                }
            } else {
                file = new File(file.getPath());
            }
            return file;
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
                obj = new Task(name, description, status);
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

    public static List<Integer> historyFromString(String line) {
        List<Integer> historyList = new ArrayList<>();
        String[] split = line.split(",");
        for (String value : split) {
            int id = Integer.parseInt(value);
            if (tasks.containsKey(id)) {
                 historyList.add(tasks.get(id));
                return historyList;
            } else if (subtasks.containsKey(id)) {
                return historyList.add(subtasks.get(id));
            } else if (epics.containsKey(id)) {
                return historyList.add(epics.get(id));
            }
        }
        return historyList;
    }


        @Override
        public String toString() {
            return id + "," +
                    getType() + "," +
                    name + "," +
                    getStatus() + "," +
                    description;
        }


    static List<Integer> fromString(String value) {
        String[] mas = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String str : mas) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }


        /**
         * Метод для сохранения менеджера истории в CSV
         */
        static String toString(HistoryManager manager) {
            List<Task> list = manager.getHistory();
            StringBuilder builder = new StringBuilder();
            for (Task task : list) {
                builder.append(task.getId()).append(",");
            }
            return builder.toString();
        }

        private void save () {
            // сохранять текущее состояние менеджера в указанный файл
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("ID,Type,Name,Status,Description,Epic \n");

                Collection<Task> tasks = super.getTaskAll();

                for (Task task : tasks) {
                    writer.write(task.toString());
                }

                Collection<Epic> epics = super.getEpicAll();
                for (Epic epic : epics) {
                    writer.write(epic.toString());
                }

                Collection<Subtask> subtasks = super.getSubtaskAll();
                for (Subtask subtask : subtasks) {
                    writer.write(subtask.toString());
                }

                String history = toString(historyManager);
                writer.write('\n');
                writer.write(history);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void newTask (Task task){
            super.newTask(task);
            save();
        }

        @Override
        public List<Task> getTaskAll () {
            return super.getTaskAll();
        }

        @Override
        public List<Subtask> getSubtaskAll () {
            return super.getSubtaskAll();
        }

        @Override
        public Task getTaskId (Integer taskId){
            return super.getTaskId(taskId);
        }

        @Override
        public Epic getEpicById (Integer epicId){
            return super.getEpicById(epicId);
        }

        @Override
        public Subtask getSubtaskId (Integer subtaskId){
            return super.getSubtaskId(subtaskId);
        }

        @Override
        public List<Task> getHistory () {
            return super.getHistory();
        }

        @Override
        public void moveEpic (Epic epic){
            super.moveEpic(epic);
            save();
        }

        @Override
        public List<Epic> getEpicAll () {
            return super.getEpicAll();
        }

        @Override
        public void moveSubtask (Subtask subtask){
            super.moveSubtask(subtask);
            save();
        }

        @Override
        public List<Subtask> getSubtaskInEpicAll (Epic epicId){
            return super.getSubtaskInEpicAll(epicId);
        }

        @Override
        public void deleteTaskAll () {
            super.deleteTaskAll();
            save();
        }

        @Override
        public boolean deleteSubtaskId (Integer subtaskId){
            if (super.deleteSubtaskId(subtaskId)) {
                save();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void deleteSubtaskAll () {
            super.deleteSubtaskAll();
            save();
        }

        @Override
        public void deleteEpicAll () {
            super.deleteEpicAll();
            save();
        }

        @Override
        public boolean updateTask (Task task){
            if (super.updateTask(task)) {
                save();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean updateEpic (Epic epic){
            if (super.updateEpic(epic)) {
                save();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean updateSubtask (Subtask subtask){
            if (super.updateSubtask(subtask)) {
                save();
                return true;
            }
            return false;
        }

        @Override
        public boolean deleteTaskById (Integer taskId){
            if (super.deleteTaskById(taskId)) {
                save();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean deleteEpicById (Integer epicId){
            if (super.deleteEpicById(epicId)) {
                save();
                return true;
            } else {
                return false;
            }
        }

        }