package com.codecool.web.service;

import com.codecool.web.model.Schedule;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleService {

    List<Schedule> findAll() throws SQLException, ServiceException;

    Schedule findByScheduleId(int id) throws SQLException;

    Schedule findByUserId(int id) throws SQLException;

    Schedule addTask(int userId, String scheduleTitle, int scheduleDuration) throws SQLException;

    void deleteByScheduleId(int id) throws SQLException;

    void updateTitleById(int id, String title) throws SQLException;

    void updateDurationById(int id, int duration) throws SQLException;

    void updateVisibilityById(int id, boolean visibility) throws SQLException;
}
