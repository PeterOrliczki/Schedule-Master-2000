package com.codecool.web.dto;

import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;

import java.util.List;

public final class ScheduleDto {

    private final Schedule schedule;
    private final List<Task> tasks;
    private final List<Task> allTasks;

    public ScheduleDto(Schedule schedule, List<Task> tasks, List<Task> allTasks) {
        this.schedule = schedule;
        this.tasks = tasks;
        this.allTasks = allTasks;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }
}
