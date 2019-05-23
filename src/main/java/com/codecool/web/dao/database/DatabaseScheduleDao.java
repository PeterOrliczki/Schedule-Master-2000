package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {

    private final DatabaseTaskDao taskDao;

    public DatabaseScheduleDao(Connection connection) {
        super(connection);
        this.taskDao = new DatabaseTaskDao(connection);
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
    public ScheduleDto findUserSchedulesWithTaskRelation(int userId, int scheduleId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        List<Task> allTasks = taskDao.findAllByUserId(userId);
        Schedule schedule = null;
        String sql = "SELECT * FROM tasks JOIN (SELECT schedules.schedule_id, schedule_title, schedule_duration, schedule_visibility, column_number, task_id\n" +
            "FROM schedules JOIN schedule_tasks ON schedules.schedule_id = schedule_tasks.schedule_id) AS temp_table ON tasks.task_id = temp_table.task_id\n" +
            "WHERE temp_table.schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = taskDao.fetchTask(resultSet);
                    task.setColumnNumber(resultSet.getInt("column_number"));
                    tasks.add(task);
                    schedule = fetchSchedule(resultSet);
                }
            }
        }
        return new ScheduleDto(schedule, tasks, allTasks);
    }

    @Override
    public boolean doesRelationExistToScheduleId(int id) throws SQLException {
        connection.setAutoCommit(false);
        String sql = "SELECT * FROM schedule_tasks WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteRelationRecordByScheduleId(int id) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "DELETE FROM schedule_tasks WHERE schedule_id = ?";
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
    public void addTaskToSchedule(int taskId, int columnNumber, int scheduleId) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedule_tasks(schedule_id, task_id, column_number) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, scheduleId);
            statement.setInt(2, taskId);
            statement.setInt(3, columnNumber);
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
}
