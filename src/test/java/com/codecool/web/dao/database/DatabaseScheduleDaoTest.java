//package com.codecool.web.dao.database;
//
//import com.codecool.web.dao.ScheduleDao;
//import com.codecool.web.dto.ScheduleDto;
//import com.codecool.web.model.Role;
//import com.codecool.web.model.Schedule;
//import com.codecool.web.model.User;
//import com.codecool.web.service.ScheduleService;
//import com.codecool.web.service.exception.ServiceException;
//import com.codecool.web.service.simple.SimpleScheduleService;
//import org.junit.jupiter.api.AfterEach;
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
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class DatabaseScheduleDaoTest {
//
//    private static User user;
//    private static Connection connection;
//    private static ScheduleService scheduleService;
//
//    @BeforeAll
//    static void setUp() throws SQLException {
//        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/scheduletest", "postgres", "admin");
//        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/init.sql"));
//        ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
//        scheduleService = new SimpleScheduleService(scheduleDao);
//        user = new User(1, "name test", "email test", "password test", Role.REGULAR);
//    }
//
//    @Test
//    void findAll() throws SQLException, ServiceException {
//        List<Schedule> schedules = scheduleService.findAll(user);
//        assertEquals(0, schedules.size());  // this user haven't published anything yet
//    }
//
//    @Test
//    void findAllById() throws SQLException {
//        List<Schedule> schedules = scheduleService.findAllById(user.getId());
//        assertEquals(2, schedules.size());  // there's two schedules under this user's id in init script
//    }
//
//    @Test
//    void findByScheduleId() throws SQLException {
//        Schedule schedule = scheduleService.findByUserId(user.getId());  // the point of this is that i can pass a schedule from the
//        schedule = scheduleService.findByScheduleId(schedule.getId());  // database as an argument, so the id there is not hardcoded
//        assertEquals(1, schedule.getId());
//    }
//
//    @Test
//    void findByUserId() throws SQLException {
//        Schedule schedule = scheduleService.findByUserId(user.getId());
//        assertEquals(1, schedule.getId());
//    }
//
//    @Test
//    void findUserSchedulesWithTaskRelation() throws SQLException {
//        Schedule schedule = scheduleService.findByUserId(user.getId());
//        ScheduleDto scheduleDto = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), schedule.getId());
//        assertEquals(1, scheduleDto.getSchedule().getId());
//    }
//
//    @Test
//    void doesRelationExistToScheduleId() {
//        scheduleService.doesRelationExistToScheduleId()
//    }
//
//    @Test
//    void deleteRelationRecordByScheduleId() {
//    }
//
//    @Test
//    void addSchedule() {
//    }
//
//    @Test
//    void addTaskToSchedule() {
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
