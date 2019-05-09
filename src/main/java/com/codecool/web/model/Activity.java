package com.codecool.web.model;

import java.util.Objects;

public final class Activity {

    private String eventName;
    private String tableName;
    private String userName;
    private String eventDate;

    public Activity(String eventName, String tableName, String userName, String eventDate) {
        this.eventName = eventName;
        this.tableName = tableName;
        this.userName = userName;
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEventDate() {
        return eventDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Activity activity = (Activity) o;
        return userName == activity.userName &&
            eventDate == activity.eventDate &&
            Objects.equals(eventName, activity.eventName) &&
            Objects.equals(tableName, activity.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventName, tableName, userName, eventDate);
    }
}
