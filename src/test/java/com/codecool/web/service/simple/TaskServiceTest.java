package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
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

class TaskServiceTest {

    private static User user;
    private static Task task;
    private static ScheduleService scheduleService;
    private static TaskService taskService;

    @BeforeAll
    static void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/scheduletest", "postgres", "admin");
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/init.sql"));

        ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
        scheduleService = new SimpleScheduleService(scheduleDao);
        UserDao userDao = new DatabaseUserDao(connection);
        UserService userService = new SimpleUserService(userDao);
        TaskDao taskDao = new DatabaseTaskDao(connection);
        taskService = new SimpleTaskService(taskDao, scheduleDao);

        user = userService.findUserByEmail("a");
        task = taskService.findTaskByUserId(user.getId());
    }

    @Test
    void getTasks() throws SQLException, ServiceException {
        int result = taskService.getTasks().size();
        assertEquals(20, result);
    }

    @Test
    void findAllByTaskId() throws SQLException {
        int result = taskService.findAllByTaskId(task.getId()).size();
        assertEquals(10, result);
    }

    @Test
    void findTaskById() throws SQLException {
        String result = taskService.findTaskById(1).getTitle();
        assertEquals("cleaning", result);
    }

    @Test
    void findTaskByUserId() throws SQLException {
        String result = taskService.findTaskByUserId(user.getId()).getTitle();
        assertEquals("cleaning", result);
    }

    @Test
    void addTask() throws SQLException {
        Task taskToBeAdded = new Task(17, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), taskToBeAdded.getTitle(), taskToBeAdded.getContent(), taskToBeAdded.getStart(), taskToBeAdded.getEnd());
        Task result = taskService.findTaskById(taskToBeAdded.getId());
        assertEquals(taskToBeAdded, result);
    }

    @Test
    void deleteTaskById() throws SQLException {
        Task task = new Task(16, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
        taskService.deleteTaskById(task.getId());
        Task result = taskService.findTaskById(task.getId());
        assertNull(result);
    }

    @Test
    void deleteRelationRecordByTaskId() throws SQLException {
        taskService.deleteRelationRecordByTaskId(3);
        scheduleService.findUserSchedulesWithTaskRelation(user.getId(), 1);
        boolean result = taskService.doesRelationExistByTaskAndScheduleId(3, 1);
        assertFalse(result);
    }

    @Test
    void deleteRelationRecordByTaskAndScheduleId() throws SQLException {
        taskService.deleteRelationRecordByTaskAndScheduleId(2, 1);
        scheduleService.findUserSchedulesWithTaskRelation(user.getId(), 1);
        boolean result = taskService.doesRelationExistByTaskAndScheduleId(2, 1);
        assertFalse(result);
    }

    @Test
    void doesRelationExistByTaskId() throws SQLException {
        boolean result = taskService.doesRelationExistByTaskId(1);
        assertTrue(result);
    }

    @Test
    void doesRelationExistByTaskAndScheduleId() throws SQLException {
        boolean result = taskService.doesRelationExistByTaskAndScheduleId(1, 1);
        assertTrue(result);
    }

    @Test
    void updateTitleById() throws SQLException {
        Task task = new Task(16, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
        taskService.updateTitleById(16, "test title updated");
        Task result = taskService.findTaskById(16);
        assertEquals("test title updated", result.getTitle());
    }

    @Test
    void updateContentById() throws SQLException {
        Task task = new Task(16, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
        taskService.updateContentById(16, "test content updated");
        Task result = taskService.findTaskById(16);
        assertEquals("test content updated", result.getContent());
    }

    @Test
    void updateStartById() throws SQLException {
        Task task = new Task(16, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
        taskService.updateStartById(16, 2);
        Task result = taskService.findTaskById(16);
        assertEquals(2, result.getStart());
    }

    @Test
    void updateEndById() throws SQLException {
        Task task = new Task(16, user.getId(), "test title", "test content", 1, 2);
        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
        taskService.updateEndById(16, 3);
        Task result = taskService.findTaskById(16);
        assertEquals(3, result.getEnd());
    }
}
