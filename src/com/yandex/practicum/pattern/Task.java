package com.yandex.practicum.pattern;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Task  {
    private Type type;
    protected Status status;
    private String name;
    private String description;
    private Integer id;
    private LocalDateTime startTime;
    private long duration;
    private LocalDateTime endTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    public Task(String name, String description, long duration, String starTime,Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(starTime, formatter);
    }

    public Task(String name, String description, Integer id, Status status,Type type){
        this.status = status;
        this.name = name;
        this.id = id;
        this.description = description;

    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration!=0) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    public Type getType() {
        return type;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "com.yandex.practicum.pattern.Task{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", status=" + status +
                ", type=" + type +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, duration, startTime);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj))
            return false;
        Task task = (Task) obj;
        return id.equals(task.id) && duration==(task.duration)&& name.equals(task.name) &&
                description.equals(task.description) && status == task.status&&startTime==(task.startTime);
    }
}