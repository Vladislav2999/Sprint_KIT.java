package com.yandex.practicum.pattern;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description,status);
    }



@Override
    public Status getStatus() {
    return status;
    }

    @Override
    public Type getType(){
        return Type.EPIC;
    }


    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
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
    public long getDuration() {
        return super.getDuration();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;    }

    @Override
    public LocalDateTime getStartTime( ) {
        return super.getStartTime();
    }

        @Override
        public int hashCode () {
            return Objects.hash(super.hashCode(), idSubtask);
        }

        @Override
        public boolean equals (Object obj){
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            if (!super.equals(obj)) return false;
            Epic epic = (Epic) obj;
            return idSubtask.equals(epic.idSubtask);
        }

        @Override
        public String toString () {
            return "com.yandex.practicum.pattern.Epic{" +
                    "id=" + getId() +
                    ", type=" + getType() +
                    ", name=" + getName() + '\'' +
                    ", description=" + getDescription() +
                    ", status=" + getStatus() +
                    ", idSubtask=" + idSubtask +
                    ", startTime=" + getStartTime() +
                    ", duration=" + getDuration() +
                    '}';

        }
    }
    
