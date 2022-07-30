package com.yandex.practicum.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.yandex.practicum.exception.ManagerDataTimeException;
import com.yandex.practicum.pattern.*;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.parse;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 2;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager managerHistory = Managers.getDefaultHistory();

    public HistoryManager getManagerHistory() {
        return managerHistory;
    }

    @Override
    public long getTaskDuration(Task task) {
        return task.getDuration();
    }

    @Override
    public int getTaskId() {
        while (getTaskAll().contains(id)) {
            id++;
        }
        return id;
    }

    @Override
    public Task newTask(Task task) {
        Task newTasks = new Task(task.getName(), task.getDescription(), getTaskId(), task.getStatus(), task.getType());
        newTasks.setStatus(Status.NEW);
        Integer temp = createId();
        task.setId(temp);
        if (task.getStartTime() != null) {
            setTaskAndSubTaskStartDateTime(newTasks, task.getStartTime().format(dateTimeFormatter));
            setTaskAndSubTaskDuration(newTasks, task.getDuration());
        }
        listOfPrioritizedTasks.add(task);
        return newTasks;
    }


    private void setTaskAndSubTaskStartDateTime(Task task, String startDateTime) {
        if (task != null & startDateTime != null) {
            LocalDateTime dateTimeFromSting = LocalDateTime.parse(startDateTime, dateTimeFormatter);
            if (!dateTimeFromSting.isAfter(LocalDateTime.now())) {
                try {
                    throw new ManagerDataTimeException("Вы указали прошедшее время");
                } catch (ManagerDataTimeException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            if (checkIsStartTimeFree(dateTimeFromSting)) {
                switch (task.getType()) {
                    case TASK:
                        task.setStartTime(dateTimeFromSting);
                        updateTask(task);

                    case SUBTASK:
                        Subtask subTask = (Subtask) task;
                        subTask.setStartTime(dateTimeFromSting);
                        updateSubtask(subTask);

                    default:
                        try {
                            throw new ManagerDataTimeException("Время для задач типа Epic определяется временем " +
                                    "их подзадач");
                        } catch (ManagerDataTimeException e) {
                            System.out.println(e.getMessage());
                        }
                }
            }
        }
    }


    @Override
    public List<Task> getTaskAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskId(Integer id) {
        if (tasks.containsKey(id)) {
            managerHistory.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.replace(task.getId(), task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteTaskAll() {
        for (var key : tasks.keySet()) {
            managerHistory.remove(key);
        }
        tasks.clear();
    }

    @Override
    public boolean deleteTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            managerHistory.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void moveSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            Integer temp = createId();
            subtasks.put(temp, subtask);
            subtask.setId(temp);
            epics.get(subtask.getEpicId()).setIdSubtaskValue(subtask.getId());
            checkStatusEpic(epics.get(subtask.getEpicId()));
            getEndTime(epics.get(subtask.getEpicId()));
            listOfPrioritizedTasks.add(subtask);
        }
    }

    @Override
    public List<Subtask> getSubtaskAll() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskId(Integer id) {
        if (subtasks.containsKey(id)) {
            managerHistory.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.replace(subtask.getId(), subtask);
            checkStatusEpic(epics.get(subtask.getEpicId()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteSubtaskAll() {
        for (var key : subtasks.keySet()) {
            managerHistory.remove(key);
            subtasks.clear();
        }
    }


    @Override
    public boolean deleteSubtaskId(Integer id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            managerHistory.remove(id);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void moveEpic(Epic epic) {
        Integer temp = createId();
        epics.put(temp, epic);
        epic.setId(temp);
        epic.setStatus(Status.NEW);
    }


    @Override
    public List<Epic> getEpicAll() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public Epic getEpicById(Integer id) {
        if (epics.containsKey(id)) {
            managerHistory.add(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }


    @Override
    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.replace(epic.getId(), epic);
            checkStatusEpic(epics.get(epic.getId()));
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void deleteEpicAll() {
        for (var key : subtasks.keySet()) {
            managerHistory.remove(key);
            subtasks.clear();
        }
    }

    @Override
    public boolean deleteEpicById(Integer id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
            epics.remove(id);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void setTaskAndSubTaskDuration(Task task, long duration) {
        if (task != null) {
            if (duration < 1) {
                try {
                    throw new ManagerDataTimeException("Длительность задачи должна бать больше 0 минут");
                } catch (ManagerDataTimeException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            if (getStartDataTime(task) == null) {
                try {
                    throw new ManagerDataTimeException("Сперва установите время начала задачи");
                } catch (ManagerDataTimeException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            if (checkIsDurationFree(task.getStartTime(), duration)) {
                switch (task.getType()) {
                    case TASK -> {
                        task.setDuration(duration);
                        updateTask(task);
                    }
                    case SUBTASK -> {
                        Subtask subTask = (Subtask) task;
                        subTask.setDuration(duration);
                        updateSubtask(subTask);
                    }
                    default -> {
                        try {
                            throw new ManagerDataTimeException("Время для задач типа Epic определяется временем " +
                                    "их подзадач");
                        } catch (ManagerDataTimeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Subtask> getSubtaskInEpicAll(Epic epic) {
        List<Subtask> temp = new ArrayList<>();
        for (int i = 0; i < epic.getIdSubtask().size(); i++) {
            if (subtasks.get(epic.getIdSubtaskValue(i)) == null) {
                continue;
            } else
                temp.add(subtasks.get(epic.getIdSubtaskValue(i)));
        }
        return temp;
    }

    @Override
    public LocalDateTime getEpicEndTime(List<Integer> listOfSubTaskId) {
        return getEpicEndTime(listOfSubTaskId);
    }

    @Override
    public LocalDateTime getStartDataTime(Task task) {
        return task.getStartTime();
    }

    @Override
    public List<Task> getHistory() {
        return managerHistory.getHistory();
    }

    private Integer createId() {
        id += 1;
        return id;
    }

    private void checkStatusEpic(Epic epic) {
        int summ = 0;
        int summ1 = 0;
        if (epic.getIdSubtask().isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Integer integer : epic.getIdSubtask()) {
            if (subtasks.get(integer).getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subtasks.get(integer).getStatus().equals(Status.NEW)) {
                summ++;
                if (summ == epic.getIdSubtask().size()) {
                    epic.setStatus(Status.NEW);
                    break;
                }
            } else if (subtasks.get(integer).getStatus().equals(Status.DONE)) {
                summ1++;
                if (summ1 == epic.getIdSubtask().size()) {
                    epic.setStatus(Status.DONE);
                    break;
                }
            }
        }
    }

    public boolean checkIsStartTimeFree(LocalDateTime startTime) {
        boolean isStartTimeFree = true;
        if (getTaskAll().size() == 1) {
            return isStartTimeFree;
        } else {
            for (Task task : getTaskAll()) {
                if ((!task.getType().equals(Type.EPIC)) & getStartDataTime(task) != null) {
                    if (getTaskDuration(task) == 0) {
                        if (startTime.isEqual(getStartDataTime(task))) {
                            isStartTimeFree = false;
                            try {
                                throw new ManagerDataTimeException("Время начала задачи пересекается с ранее " +
                                        "запланированной задачей " + task.getId());
                            } catch (ManagerDataTimeException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else {
                        if ((startTime.isEqual(getStartDataTime(task)) || (startTime.isAfter(getStartDataTime(task))) &
                                ((startTime.isEqual(getStartDataTime(task).
                                        plus(ofMinutes(task.getDuration())))) ||
                                        startTime.isBefore(getStartDataTime(task).
                                                plus(ofMinutes(task.getDuration())))))) {
                            isStartTimeFree = false;
                            try {
                                throw new ManagerDataTimeException("Время начала задачи пересекается с ранее " +
                                        "запланированной задачей " + task.getId());
                            } catch (ManagerDataTimeException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            }
            return isStartTimeFree;
        }
    }


    private final Set<Task> listOfPrioritizedTasks = new TreeSet<>((task1, task2) -> {
        if ((task1.getStartTime() != null) && (task2.getStartTime() != null)) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (null == task2.getStartTime()) {
            return -1;
        } else {
            return 0;
        }
    });

    public Set<Task> getPrioritizedTasks() {

        if (listOfPrioritizedTasks.isEmpty()) {
            System.out.println("Задачи отсутсвуют");
        }
        return listOfPrioritizedTasks;

    }

    public boolean checkIsDurationFree(LocalDateTime startTime, long duration) {
        boolean isDurationFree = true;
        if (getTaskAll().size() == 1) {
            return isDurationFree;
        } else {
            for (Task task : getTaskAll()) {
                if ((!task.getType().equals(Type.EPIC)) & getStartDataTime(task) != null &
                        getTaskDuration(task) != 0) {
                    if (startTime.isBefore(task.getStartTime().plus(ofMinutes(task.getDuration()))) &
                            startTime.plus(ofMinutes(duration)).isAfter(task.getStartTime())) {
                        isDurationFree = false;
                        try {
                            throw new ManagerDataTimeException("Время выполнения задачи пересекается с ранее " +
                                    "запланированной задачей " + task.getId());
                        } catch (ManagerDataTimeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            return isDurationFree;
        }
    }


    public void getEndTime(Epic epic) {
        epic.setStartTime(LocalDateTime.MAX);
        epic.setDuration(0);
        epic.setEndTime(LocalDateTime.MIN);
        if (epic.getIdSubtask().size() != 0) {
            for (Integer integer : epic.getIdSubtask()) {
                if (subtasks.get(integer).getStartTime() == null || subtasks.get(integer).getDuration()==0) {
                    continue;
                } else {
                    if (epic.getIdSubtask().size() == 1) {
                        epic.setStartTime(subtasks.get(integer).getStartTime());
                        epic.setDuration(subtasks.get(integer).getDuration());
                        epic.setEndTime(epic.getStartTime().plus(ofMinutes(epic.getDuration())));
                        break;
                    } else {

                        if (epic.getStartTime().isAfter(subtasks.get(integer).getStartTime())) {
                            epic.setStartTime(subtasks.get(integer).getStartTime());
                        }
                        if (epic.getStartTime().isBefore(subtasks.get(integer).getEndTime())) {
                            epic.setEndTime(subtasks.get(integer).getStartTime().
                                    plus(ofMinutes(subtasks.get(integer).getDuration())));
                        }
                    }
                }
            }
        }
        if (epic.getEndTime().equals(LocalDateTime.MIN) || epic.getStartTime().equals(LocalDateTime.MAX)) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        }
    }
}