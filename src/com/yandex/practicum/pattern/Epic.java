package com.yandex.practicum.pattern;


import java.util.ArrayList;


import java.util.Objects;


public class Epic extends Task {


    private final ArrayList<Integer> idSubtask = new ArrayList<>();
    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    public Integer getIdSubtaskValue(Integer value) {
        return idSubtask.get(value);
    }

    public void setIdSubtaskValue(Integer value) {
        idSubtask.add(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtask);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Epic epic = (Epic) obj;
        return idSubtask.equals(epic.idSubtask);
    }

    @Override
    public String toString() {
        return "com.yandex.practicum.pattern.Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ",  idSubtask='" + idSubtask + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';

    }
}