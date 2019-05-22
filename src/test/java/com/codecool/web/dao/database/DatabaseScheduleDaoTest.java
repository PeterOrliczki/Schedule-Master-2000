//package com.codecool.web.dao.database;
//
//import com.codecool.web.dao.ScheduleDao;
//import com.codecool.web.dao.TaskDao;
//import com.codecool.web.dao.UserDao;
//import com.codecool.web.dto.ScheduleDto;
//import com.codecool.web.model.Role;
//import com.codecool.web.model.Schedule;
//import com.codecool.web.model.Task;
//import com.codecool.web.model.User;
//import com.codecool.web.service.ScheduleService;
//import com.codecool.web.service.TaskService;
//import com.codecool.web.service.UserService;
//import com.codecool.web.service.exception.ServiceException;
//import com.codecool.web.service.simple.SimpleScheduleService;
//import com.codecool.web.service.simple.SimpleTaskService;
//import com.codecool.web.service.simple.SimpleUserService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.ScriptUtils;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DatabaseScheduleDaoTest {
//
//    private static User user;
//    private static Schedule schedule;
//    private static Schedule schedule2;
//    private static Task task;
//    private static Task task2;
//    private static ScheduleService scheduleService;
//
//    @BeforeAll
//    static void setUp() throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/scheduletest", "postgres", "admin");
//        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/inittest.sql"));
//
//        ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
//        scheduleService = new SimpleScheduleService(scheduleDao);
//        UserDao userDao = new DatabaseUserDao(connection);
//        UserService userService = new SimpleUserService(userDao);
//        TaskDao taskDao = new DatabaseTaskDao(connection);
//        TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);
//
//        user = new User(1, "name test", "email test", "password test", Role.ADMIN);
//        schedule = new Schedule(1, user.getId(), "test title", 1);
//        schedule2 = new Schedule(2, user.getId(), "test title2", 2);
//        task = new Task(1, user.getId(), "test title", "test content", 1, 2);
//        task2 = new Task(2, user.getId(), "test title 2", "test content 2", 2, 3);
//
//        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
//        taskService.addTask(user.getId(), task.getTitle(), task.getContent(), task.getStart(), task.getEnd());
//        taskService.addTask(user.getId(), task2.getTitle(), task2.getContent(), task2.getStart(), task2.getEnd());
//
//        scheduleService.addSchedule(user.getId(), schedule.getTitle(), schedule.getDuration());
//        scheduleService.addTaskToSchedule(task.getId(), task.getColumnNumber(), schedule.getId());
//    }
//
//    @Test
//    void findAll() throws SQLException, ServiceException {
//        List<Schedule> schedules = scheduleService.findAll(user);
//        assertEquals(2, schedules.size());
//    }
//
//    @Test
//    void findAllById() throws SQLException {
//        List<Schedule> schedules = scheduleService.findAllById(user.getId());
//        assertEquals(2, schedules.size());
//    }
//
//    @Test
//    void findByScheduleId() throws SQLException {
//        ;
//        schedule = scheduleService.findByScheduleId(schedule.getId());
//        assertEquals(1, schedule.getId());
//    }
//
//    @Test
//    void findByUserId() throws SQLException {
//        schedule = scheduleService.findByUserId(user.getId());
//        assertEquals(1, schedule.getId());
//    }
//
//    @Test
//    void findUserSchedulesWithTaskRelation() throws SQLException {
//        ScheduleDto scheduleDto = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), schedule.getId());
//        assertEquals(1, scheduleDto.getSchedule().getId());
//    }
//
//    @Test
//    void doesRelationExistToScheduleId() throws SQLException {
//        boolean result = scheduleService.doesRelationExistToScheduleId(schedule.getId());
//        assertTrue(result);
//    }
//
//    @Test
//    void deleteRelationRecordByScheduleId() throws SQLException {
//        scheduleService.deleteRelationRecordByScheduleId(schedule.getId());
//        boolean result = scheduleService.doesRelationExistToScheduleId(schedule.getId());
//        assertFalse(result);
//    }
//
//    @Test
//    void addSchedule() throws SQLException, ServiceException {
//        scheduleService.addSchedule(user.getId(), schedule.getTitle(), schedule.getDuration());
//        List<Schedule> schedules = scheduleService.findAll(user);
//        assertEquals(2, schedules.size());
//    }
//
//    @Test
//    void addTaskToSchedule() throws SQLException {
////        scheduleService.addSchedule(user.getId(), schedule2.getTitle(), schedule2.getDuration());
////        scheduleService.addTaskToSchedule(task2.getId(), schedule2.getId(), task2.getColumnNumber());
////        ScheduleDto scheduleDto = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), schedule2.getId());
////        assertEquals(2, scheduleDto.getSchedule().getId());
//    }
//
//    @Test
//    void deleteByScheduleId() {
//    }
//
//    @Test
//    void updateTitleById() {
//    }
//
//    @Test
//    void updateDurationById() {
//    }
//
//    @Test
//    void updateVisibilityById() {
//    }
//}
