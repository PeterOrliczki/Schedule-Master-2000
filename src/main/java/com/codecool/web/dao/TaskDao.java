package com.codecool.web.dao;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    List<Task> findAll() throws SQLException;

    Task findByTaskId(int id) throws SQLException;

    Task findByUserId(int id) throws SQLException;

    Task addTask(int userId, String taskTitle, String taskContent, int taskStart, int taskEnd) throws SQLException;

    void deleteByTaskId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateContentById(int id, String content) throws SQLException;

    void updateStartByID(int id, String start) throws SQLException;

    void updateEndByID(int id, String end) throws SQLException;

}
