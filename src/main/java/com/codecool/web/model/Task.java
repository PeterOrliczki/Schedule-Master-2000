package com.codecool.web.model;

import java.util.Objects;

public final class Task extends AbstractModel {

    private String title;
    private String content;
    private int start;
    private int end;
    private int userId;
    private int columnNumber;

    public Task(int id, int userId, String title, String content, int start, int end) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.start = start;
        this.end = end;
    }

    public Task() {

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return start == task.start &&
            end == task.end &&
            userId == task.userId &&
            Objects.equals(title, task.title) &&
            Objects.equals(content, task.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),title, content, start, end, userId);
    }
}
