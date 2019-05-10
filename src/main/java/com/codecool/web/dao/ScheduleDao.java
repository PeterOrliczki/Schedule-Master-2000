package com.codecool.web.dao;

import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleDao {

    List<Schedule> findAll() throws SQLException;

    List<Schedule> findAllById(int id) throws SQLException;

    List<Schedule> findAllByVisibility() throws SQLException;

    Schedule findByScheduleId(int id) throws SQLException;

    Schedule findByUserId(int id) throws SQLException;

    ScheduleDto findUserSchedulesWithTaskRelation(int userId, int scheduleId) throws SQLException;

    void addTaskToSchedule(int taskId, int scheduleId, int columnNumber) throws SQLException;

    boolean doesRelationExistToScheduleId(int id) throws SQLException;

    void deleteRelationRecordByScheduleId(int id) throws SQLException;

    Schedule addSchedule(int userId, String scheduleTitle, int scheduleDuration) throws SQLException;

    void deleteByScheduleId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateDurationById(int id, int duration) throws SQLException;

    void updateVisibilityById(int id, boolean visibility) throws SQLException;

}
