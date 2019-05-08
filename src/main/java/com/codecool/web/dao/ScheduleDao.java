package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleDao {

    List<Schedule> findAll() throws SQLException;

    Schedule findByScheduleId(int id) throws SQLException;

    Schedule findByUserId(int id) throws SQLException;

    Schedule addTask(int userId, String scheduleTitle, int scheduleDuration, boolean scheduleVisiblity) throws SQLException;

    void deleteByScheduleId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateDurationById(int id, String duration) throws SQLException;

    void updateVisibilityById(int id, String visibility) throws SQLException;

}
