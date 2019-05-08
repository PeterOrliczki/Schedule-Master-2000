package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public final class SimpleScheduleService implements ScheduleService {

    private final ScheduleDao scheduleDao;

    public SimpleScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public List<Schedule> findAll() throws SQLException, ServiceException {
        List<Schedule> schedules = scheduleDao.findAll();
        if (schedules != null) {
            return schedules;
        } else {
            throw new ServiceException("No existing schedule yet, but you can create a new one now.");
        }
    }

    @Override
    public Schedule findByScheduleId(int id) throws SQLException {
        return scheduleDao.findByScheduleId(id);
    }

    @Override
    public Schedule findByUserId(int id) throws SQLException {
        return scheduleDao.findByUserId(id);
    }

    @Override
    public Schedule addTask(int userId, String scheduleTitle, int scheduleDuration) throws SQLException {
        return scheduleDao.addTask(userId, scheduleTitle, scheduleDuration);
    }

    @Override
    public void deleteByScheduleId(int id) throws SQLException {
        scheduleDao.deleteByScheduleId(id);
    }

    @Override
    public void updateTitleById(int id, String title) throws SQLException {
        scheduleDao.updateTitleById(id, title);
    }

    @Override
    public void updateDurationById(int id, int duration) throws SQLException {
        scheduleDao.updateDurationById(id, duration);
    }

    @Override
    public void updateVisibilityById(int id, boolean visibility) throws SQLException {
        scheduleDao.updateVisibilityById(id, visibility);
    }

}
