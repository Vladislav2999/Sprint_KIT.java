package com.yandex.practicum.manager;



import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Task;

public interface TaskManager   {

    Task newTask(Task task);

    List<Task> getTaskAll();

    Task getTaskId(Integer id);

    int getTaskId();

    boolean updateTask(Task task);

    void deleteTaskAll();

    boolean deleteTaskById(Integer id);

    void moveSubtask(Subtask subtask);

    List<Subtask> getSubtaskAll();

    Subtask getSubtaskId(Integer id);

    boolean updateSubtask(Subtask subtask);

    void deleteSubtaskAll();

    boolean deleteSubtaskId(Integer id);

    void moveEpic(Epic epic);

    void setTaskAndSubTaskDuration(Task task, long duration);


    LocalDateTime getStartDataTime(Task task);

    public long getTaskDuration(Task task);

    LocalDateTime getEpicEndTime(List<Integer> listOfSubTaskId);

    List<Epic> getEpicAll();

    Epic getEpicById(Integer id);

    boolean updateEpic(Epic epic);

    void deleteEpicAll();

    boolean deleteEpicById(Integer id);

    List<Subtask> getSubtaskInEpicAll(Epic epic);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();

    void getEndTime(Epic epic);
}