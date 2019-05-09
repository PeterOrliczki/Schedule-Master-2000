package com.codecool.web.dto;

import com.codecool.web.model.Schedule;

import java.util.List;

public final class ScheduleListDto {

    private final List<Schedule> publicSchedules;
    private final List<Schedule> mySchedules;

    public ScheduleListDto(List<Schedule> publicSchedules, List<Schedule> mySchedules) {
        this.publicSchedules = publicSchedules;
        this.mySchedules = mySchedules;
    }

    public List<Schedule> getPublicSchedules() {
        return publicSchedules;
    }

    public List<Schedule> getMySchedules() {
        return mySchedules;
    }
}
