package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/schedule")
public class ScheduleServlet extends AbstractServlet {

    private final ObjectMapper om = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(ScheduleServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int id = Integer.parseInt(req.getParameter("schedule-id"));
            ScheduleDto schedule = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), id);

            logger.info("Loaded schedule ID " + id + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, schedule);
        } catch (SQLException exc) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int taskId = Integer.parseInt(req.getParameter("task-id"));
            int columnNumber = Integer.parseInt(req.getParameter("columnNumber"));
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));

            scheduleService.addTaskToSchedule(taskId, columnNumber, scheduleId);
            ScheduleDto schedule = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), scheduleId);

            logger.info("Added task ID " + taskId + " to schedule ID " + scheduleId + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, schedule);
        } catch (SQLException exc) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            Schedule schedule = om.readValue(req.getInputStream(), Schedule.class);
            scheduleService.updateDurationById(schedule.getId(), schedule.getDuration());
            scheduleService.updateTitleById(schedule.getId(), schedule.getTitle());
            scheduleService.updateVisibilityById(schedule.getId(), schedule.isVisibility());

            logger.info("Updated schedule ID " + schedule.getId() + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, "Schedule updated.");
        } catch (SQLException exc) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int id = Integer.parseInt(req.getParameter("schedule-id"));

            scheduleService.deleteByScheduleId(id);

            logger.info("Deleted schedule ID " + id + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, "Schedule deleted.");
        } catch (SQLException exc) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }
}
