package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;

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
    public List<Schedule> findAllById(int id) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedules.add(fetchSchedule(resultSet));
                }
            }
        }
        return schedules;
    }

    @Override
    public List<Schedule> findAllByVisibility() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE schedule_visibility = true";
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

    public ScheduleDto findUserSchedulesWithTaskRelation(int userId, int scheduleId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks JOIN (SELECT schedules.schedule_id, schedule_title, schedule_duration, schedule_visibility, task_id\n" +
            "FROM schedules JOIN schedule_tasks ON schedules.schedule_id = schedule_tasks.schedule_id) AS temp_table ON tasks.task_id = temp_table.task_id\n" +
            "WHERE tasks.user_id = ? AND temp_table.schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, scheduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    //tasks.add()
                }
            }
        }
    }

    @Override
    public Schedule addSchedule(int userId, String scheduleTitle, int scheduleDuration) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, scheduleTitle);
            statement.setInt(3, scheduleDuration);
            executeInsert(statement);
            int scheduleId = fetchGeneratedId(statement);
            connection.commit();
            return new Schedule(scheduleId, userId, scheduleTitle, scheduleDuration);
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
    public void updateTitleById(int id, String title) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_title=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setInt(2, id);
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
    public void updateDurationById(int id, int duration) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_duration=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, duration);
            statement.setInt(2, id);
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
    public void updateVisibilityById(int id, boolean visibility) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET schedule_visibility=? WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1, visibility);
            statement.setInt(2, id);
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
        boolean scheduleVisibility = resultSet.getBoolean("schedule_visibility");

        return new Schedule(scheduleId, userId, scheduleTitle, scheduleDuration, scheduleVisibility);
    }

    private ScheduleDto fetchScheduleDto(ResultSet resultSet) throws SQLException {
        int scheduleId = resultSet.getInt("schedule_id");
        int userId = resultSet.getInt("user_id");
        String scheduleTitle = resultSet.getString("schedule_title");
        int scheduleDuration = resultSet.getInt("schedule_duration");
        boolean scheduleVisibility = resultSet.getBoolean("schedule_visibility");

        return new ScheduleDto();
    }
}
