package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;

import com.codecool.web.service.TaskService;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public final class SimpleTaskService implements TaskService {

    private final TaskDao taskDao;
    private final ScheduleDao scheduleDao;

    public SimpleTaskService(TaskDao taskDao, ScheduleDao scheduleDao) {
        this.taskDao = taskDao;
        this.scheduleDao = scheduleDao;
    }
}
