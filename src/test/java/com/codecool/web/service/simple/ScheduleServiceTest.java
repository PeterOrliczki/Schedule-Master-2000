package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleServiceTest {

    private static User user;
    private static Schedule schedule;
    private static ScheduleService scheduleService;

    @BeforeAll
    static void setUp() throws SQLException, ServiceException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/scheduletest", "postgres", "admin");
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/init.sql"));

        ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
        scheduleService = new SimpleScheduleService(scheduleDao);
        UserDao userDao = new DatabaseUserDao(connection);
        UserService userService = new SimpleUserService(userDao);

        user = userService.findUserByEmail("a");
        schedule = scheduleService.findAll(user).get(0);
    }

    @Test
    void findAll() throws SQLException, ServiceException {
        int result = scheduleService.findAll(user).size();
        assertEquals(8, result);
    }

    @Test
    void findAllById() throws SQLException {
        int result = scheduleService.findAllById(user.getId()).size();
        assertEquals(4, result);
    }

    @Test
    void findByScheduleId() throws SQLException {
        Schedule result = scheduleService.findByScheduleId(schedule.getId());
        assertEquals(schedule, result);
    }

    @Test
    void findByUserId() throws SQLException {
        Schedule result = scheduleService.findByScheduleId(user.getId());
        assertEquals(schedule, result);
    }

    @Test
    void findUserSchedulesWithTaskRelation() throws SQLException {
        int result = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), schedule.getId()).getSchedule().getId();
        assertEquals(1, result);
    }

    @Test
    void doesRelationExistToScheduleId() throws SQLException {
        boolean result = scheduleService.doesRelationExistToScheduleId(schedule.getId());
        assertTrue(result);
    }

    @Test
    void addSchedule() throws SQLException {
        Schedule scheduleToBeAdded = new Schedule(7, user.getId(), "test title", 1);
        scheduleService.addSchedule(user.getId(), scheduleToBeAdded.getTitle(), scheduleToBeAdded.getDuration());
        Schedule result = scheduleService.findByScheduleId(scheduleToBeAdded.getId());
        assertEquals(scheduleToBeAdded, result);
    }

    @Test
    void addTaskToSchedule() throws SQLException {
        Task taskToBeAdded = new Task(10, user.getId(), "test title", "test content", 1, 2);
        scheduleService.addTaskToSchedule(taskToBeAdded.getId(), 1, 1);
    }

    @Test
    void deleteByScheduleId() throws SQLException {
        Schedule schedule = new Schedule(7, user.getId(), "test title", 1);
        scheduleService.addSchedule(user.getId(), schedule.getTitle(), schedule.getDuration());
        scheduleService.deleteByScheduleId(schedule.getId());
        Schedule result = scheduleService.findByScheduleId(schedule.getId());
        assertNull(result);
    }

    @Test
    void updateTitleById() throws SQLException {
        Schedule schedule = new Schedule(7, user.getId(), "test title", 1);
        scheduleService.addSchedule(user.getId(), schedule.getTitle(), schedule.getDuration());
        scheduleService.updateTitleById(7, "test title updated");
        Schedule result = scheduleService.findByScheduleId(7);
        assertEquals("test title updated", result.getTitle());
    }

    @Test
    void updateDurationById() throws SQLException {
        Schedule schedule = new Schedule(7, user.getId(), "test title", 1);
        scheduleService.addSchedule(user.getId(), schedule.getTitle(), schedule.getDuration());
        scheduleService.updateDurationById(7, 2);
        Schedule result = scheduleService.findByScheduleId(7);
        assertEquals(2, result.getDuration());
    }
}
