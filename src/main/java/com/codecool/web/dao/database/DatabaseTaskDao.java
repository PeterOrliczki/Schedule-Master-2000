package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.model.Task;

import java.sql.*;
import java.util.List;

public final class DatabaseTaskDao extends AbstractDao implements TaskDao {

    public DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    public List<Task> findAll() throws SQLException {
        return null;
    }

    public Task findById(int id) throws SQLException {
        return null;
    }

    public void add() throws SQLException {

    }

    public void add(int scheduleId, int... taskIds) throws SQLException {

    }

    private Task fetchTask(ResultSet resultSet) throws SQLException {
        return null;
    }
}
