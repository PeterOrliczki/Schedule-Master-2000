package com.codecool.web.model;

import java.util.Objects;

public final class Task extends AbstractModel {

    private final String title;
    private final String content;
    private final int start;
    private final int end;
    private final int userId;

    public Task(int id, String title, String content, int start, int end, int userId) {
        super(id);
        this.title = title;
        this.content = content;
        this.start = start;
        this.end = end;
        this.userId = userId;
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