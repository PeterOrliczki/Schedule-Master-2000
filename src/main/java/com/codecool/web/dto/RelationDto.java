package com.codecool.web.dto;

public class RelationDto {

    private final int taskId;
    private final int columnNumber;
    private final int scheduleId;

    public RelationDto(int taskId, int columnNumber, int scheduleId) {
        this.taskId = taskId;
        this.columnNumber = columnNumber;
        this.scheduleId = scheduleId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
