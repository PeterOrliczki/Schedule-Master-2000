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

    void updateTitleById(String id, String title) throws SQLException;

    void updateContentById(String id, String content) throws SQLException;

    void updateStartByID(String id, String start) throws SQLException;

    void updateEndByID(String id, String end) throws SQLException;

}
