package com.yandex.practicum.pattern;

import java.util.Objects;

public class Task {

    private Status status;
    private String name;
    private String description;
    private Integer id;


    public Task(String name, String description, Status status) {
        this.status = status;
        this.name = name;
        this.description = description;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "com.yandex.practicum.pattern.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Task task = (Task) obj;
        return id.equals(task.id) && name.equals(task.name) && description.equals(task.description) && status == task.status;
    }


}