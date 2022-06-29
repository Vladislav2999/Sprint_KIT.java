
package com.yandex.practicum;


import com.yandex.practicum.manager.Managers;
import com.yandex.practicum.manager.TaskManager;
import com.yandex.practicum.pattern.Epic;
import com.yandex.practicum.pattern.Status;
import com.yandex.practicum.pattern.Subtask;
import com.yandex.practicum.pattern.Task;


public class Main {
    public static void main(String[] args) {
        final TaskManager managerDefault = Managers.getDefault();
        Task task = new Task("ТЗ", "Сделать Яндекс.Практикум", Status.NEW);
        Task task1 = new Task("Уборка", "Пропылесосить", Status.NEW);
        Epic epic = new Epic("Крупная покупка", "Купить новую машину", Status.IN_PROGRESS);
        Subtask subtask = new Subtask("Купить кровать", "Кровать в спальню", Status.NEW, 3);
        Subtask subtask1 = new Subtask("Купить матрас", "Матрас для кровати", Status.DONE, 3);
        Epic epic1 = new Epic("Стать java разработчиком", "Устроиться на работу", Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Закончить курсы", "Успешно окончить курсы Практикума",
                Status.DONE, 6);
        managerDefault.newTask(task);
        managerDefault.newTask(task1);
        managerDefault.moveSubtask(subtask);
        managerDefault.moveSubtask(subtask1);


        managerDefault.getTaskId(1);
        managerDefault.getTaskId(2);
        managerDefault.getTaskId(2);
        managerDefault.getTaskId(2);


        System.out.println(managerDefault.getHistory());

    }

}
