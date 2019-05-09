package com.codecool.web.dao;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    List<Task> findAll() throws SQLException;

    List<Task> findAllByTaskId(int id) throws SQLException;

    Task findByTaskId(int id) throws SQLException;

    Task findByUserId(int id) throws SQLException;

    Task addTask(int userId, String taskTitle, String taskContent, int taskStart, int taskEnd) throws SQLException;

    void deleteByTaskId(int id) throws SQLException;

    void deleteRelationRecordByTaskId(int id) throws SQLException;

    boolean doesRelationExistsTaskId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateContentById(int id, String content) throws SQLException;

    void updateStartByID(int id, int start) throws SQLException;

    void updateEndByID(int id, int end) throws SQLException;

}
