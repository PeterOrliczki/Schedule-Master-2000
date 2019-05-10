package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Role;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SimpleScheduleService implements ScheduleService {

    private final ScheduleDao scheduleDao;

    public SimpleScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public List<Schedule> findAll(User user) throws SQLException, ServiceException {
        if (user.getRole().equals(Role.ADMIN)) {
            return scheduleDao.findAll();
        } else {
            return scheduleDao.findAllByVisibility();
        }
    }

    @Override
    public List<Schedule> findAllById(int id) throws SQLException {
        return scheduleDao.findAllById(id);
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
    public ScheduleDto findUserSchedulesWithTaskRelation(int userId, int scheduleId) throws SQLException {
        ScheduleDto scheduleDto = scheduleDao.findUserSchedulesWithTaskRelation(userId, scheduleId);
        if (scheduleDto.getSchedule() == null) {
            Schedule schedule = findByScheduleId(scheduleId);
            List<Task> tasks = new ArrayList<>();
            scheduleDto = new ScheduleDto(schedule, tasks);
        }
        return scheduleDto;
    }

    @Override
    public void addTaskToSchedule(int taskId, int scheduleId, int columnNumber) throws SQLException {
        scheduleDao.addTaskToSchedule(taskId, scheduleId, columnNumber);
    }

    @Override
    public boolean doesRelationExistToScheduleId(int id) throws SQLException {
        return scheduleDao.doesRelationExistToScheduleId(id);
    }

    @Override
    public void deleteRelationRecordByScheduleId(int id) throws SQLException {
        scheduleDao.deleteRelationRecordByScheduleId(id);
    }

    @Override
    public Schedule addSchedule(int userId, String scheduleTitle, int scheduleDuration) throws SQLException {
        return scheduleDao.addSchedule(userId, scheduleTitle, scheduleDuration);
    }

    @Override
    public void deleteByScheduleId(int id) throws SQLException {
        while (doesRelationExistToScheduleId(id)) {
            deleteRelationRecordByScheduleId(id);
        }
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
