package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {

    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<Schedule> findAll() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                schedules.add(fetchSchedule(resultSet));
            }
        }
        return schedules;
    }

    @Override
    public Schedule findByScheduleId(int id) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchSchedule(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public Schedule findByUserId(int id) throws SQLException {
        String sql = "SELECT * FROM schedules WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchSchedule(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public Schedule addTask(int userId, String scheduleTitle, int scheduleDuration, boolean scheduleVisiblity) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks(user_id, schedule_title, schedule_duration, schedule_visibility) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, scheduleTitle);
            statement.setInt(3, scheduleDuration);
            statement.setBoolean(4, scheduleVisiblity);
            executeInsert(statement);
            int scheduleId = fetchGeneratedId(statement);
            connection.commit();
            return new Schedule(scheduleId, userId, scheduleTitle, scheduleDuration, scheduleVisiblity);
        } catch (SQLException exc) {
            connection.rollback();
            throw exc;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteByScheduleId(int id) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException exc) {
            connection.rollback();
            throw exc;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateTitleById(String id, String title) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_title=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setString(2, id);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException exc) {
            connection.rollback();
            throw exc;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateDurationById(String id, String duration) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_duration=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, duration);
            statement.setString(2, id);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException exc) {
            connection.rollback();
            throw exc;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateVisibilityById(String id, String visibility) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_visibility=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, visibility);
            statement.setString(2, id);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException exc) {
            connection.rollback();
            throw exc;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    private Schedule fetchSchedule(ResultSet resultSet) throws SQLException {
        int scheduleId = resultSet.getInt("schedule_id");
        int userId = resultSet.getInt("user_id");
        String scheduleTitle = resultSet.getString("schedule_title");
        int scheduleDuration = resultSet.getInt("schedule_duration");
        boolean scheduleVisiblity = resultSet.getBoolean("schedule_visibilty");

        return new Schedule(scheduleId, userId, scheduleTitle, scheduleDuration, scheduleVisiblity);
    }
}
