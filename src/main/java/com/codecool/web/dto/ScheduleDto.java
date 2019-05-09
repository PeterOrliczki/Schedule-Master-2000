package com.codecool.web.dto;

import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;

import java.util.List;

public final class ScheduleDto {

    private final Schedule schedule;
    private final List<Task> tasks;

    public ScheduleDto(Schedule schedule, List<Task> tasks) {
        this.schedule = schedule;
        this.tasks = tasks;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
