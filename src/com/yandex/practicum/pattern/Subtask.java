package com.yandex.practicum.pattern;

import java.util.Objects;

public class Subtask extends Task {

    private final Integer epicId;


    public Integer getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override


    public String toString() {
        return "com.yandex.practicum.pattern.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ",  status='" + getStatus() + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Subtask subtask = (Subtask) obj;
        return epicId.equals(subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
