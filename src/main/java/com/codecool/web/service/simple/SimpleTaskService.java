package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;

import java.sql.SQLException;
import java.util.List;

public final class SimpleTaskService implements TaskService {

    private final TaskDao taskDao;
    private final ScheduleDao scheduleDao;

    public SimpleTaskService(TaskDao taskDao, ScheduleDao scheduleDao) {
        this.taskDao = taskDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public List<Task> getTasks() throws SQLException {
        return taskDao.findAll();
    }

    @Override
    public List<Task> findAllByTaskId(int id) throws SQLException {
        return taskDao.findAllByTaskId(id);
    }

    @Override
    public Task findTaskById(int id) throws SQLException {
        return taskDao.findByTaskId(id);
    }

    @Override
    public Task findTaskByUserId(int id) throws SQLException {
        return taskDao.findByUserId(id);
    }

    @Override
    public Task addTask(int userId, String taskTitle, String taskContent, int taskStart, int taskEnd) throws SQLException {
        return taskDao.addTask(userId, taskTitle, taskContent, taskStart, taskEnd);
    }

    @Override
    public void deleteTaskById(int id) throws SQLException {
        taskDao.deleteByTaskId(id);
    }

    @Override
    public void updateTitleById(int id, String title) throws SQLException {
        taskDao.updateTitleById(id, title);
    }

    @Override
    public void updateContentById(int id, String content) throws SQLException {
        taskDao.updateContentById(id, content);
    }

    @Override
    public void updateStartById(int id, int start) throws SQLException {
        taskDao.updateStartByID(id, start);
    }

    @Override
    public void updateEndById(int id, int end) throws SQLException {
        taskDao.updateEndByID(id, end);
    }

}
