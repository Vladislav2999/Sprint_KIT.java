
package com.yandex.practicum;


import com.yandex.practicum.manager.Managers;
import com.yandex.practicum.manager.TaskManager;
import com.yandex.practicum.pattern.*;


public class Main {
    public static void main(String[] args) {
        final TaskManager managerDefault = Managers.getDefault();
        Task task = new Task("ТЗ", "Сделать Яндекс.Практикум", Status.NEW, Type.TASK);
        Task task1 = new Task("Уборка", "Пропылесосить", Status.NEW, Type.TASK);
        Epic epic = new Epic("Крупная покупка", "Купить новую машину", Status.IN_PROGRESS,Type.EPIC);
        Subtask subtask = new Subtask("Купить кровать", "Кровать в спальню", Status.NEW, 3, Type.SUBTASK);
        Subtask subtask1 = new Subtask("Купить матрас", "Матрас для кровати", Status.DONE, 3, Type.SUBTASK);
        Epic epic1 = new Epic("Стать java разработчиком", "Устроиться на работу", Status.IN_PROGRESS,Type.EPIC);
        Subtask subtask2 = new Subtask("Закончить курсы", "Успешно окончить курсы Практикума",
                Status.DONE, 6, Type.SUBTASK);
        managerDefault.newTask(task);
        managerDefault.newTask(task1);
        managerDefault.moveSubtask(subtask);
        managerDefault.moveSubtask(subtask1);

        /**
         * правильно тут? последняя правка из 5 спринта, которая уже в комментарии была
         */

        managerDefault.getTaskId(task1.getId());
        managerDefault.getTaskId(task.getId());


        managerDefault.getTaskId(1);
        managerDefault.getTaskId(2);
        managerDefault.getTaskId(2);
        managerDefault.getTaskId(2);


        System.out.println(managerDefault.getHistory());

    }

}
