package com.codecool.web.service;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskService {

    List<Task> getTasks() throws SQLException;

    List<Task> findAllByTaskId(int id) throws SQLException;

    Task findTaskById(int id) throws SQLException;

    Task findTaskByUserId(int id) throws SQLException;

    Task addTask(int userId, String taskTitle, String taskContent, int taskStart, int taskEnd) throws SQLException;

    void deleteTaskById(int id) throws SQLException;

    void deleteRelationRecordByTaskId(int id) throws SQLException;

    boolean doesRelationExistsTaskId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateContentById(int id, String content) throws SQLException;

    void updateStartById(int id, int start) throws SQLException;

    void updateEndById(int id, int end) throws SQLException;


}
