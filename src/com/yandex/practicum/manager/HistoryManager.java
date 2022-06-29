package com.yandex.practicum.manager;

import com.yandex.practicum.pattern.Task;


import java.util.List;


public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();


}
