package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {

    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }


    public List<Schedule> findAll() throws SQLException {
        return null;
    }


    public List<Schedule> findByScheduleId(int couponId) throws SQLException {
        return null;
    }



    public Schedule add(String name) throws SQLException {
        return null;
    }

    private Schedule fetchSchedule(ResultSet resultSet) throws SQLException {
        return null;
    }
}
