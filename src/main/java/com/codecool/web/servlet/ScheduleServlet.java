package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dto.ScheduleDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int id = Integer.parseInt(req.getParameter("schedule-id"));
            ScheduleDto schedule = scheduleService.findUserSchedulesWithTaskRelation(user.getId(), id);

            sendMessage(resp, HttpServletResponse.SC_OK, schedule);
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);

            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = (User)request.getSession().getAttribute("user");
            String title = request.getParameter("title");
            int duration = Integer.valueOf(request.getParameter("duration"));

            scheduleService.addSchedule(user.getId(), title, duration);
            sendMessage(response, HttpServletResponse.SC_OK, "Schedule added.");
        } catch (SQLException exc) {
            handleSqlError(response, exc);
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

            sendMessage(resp, HttpServletResponse.SC_OK, "Schedule updated.");
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int id = Integer.parseInt(req.getParameter("schedule-id"));

            scheduleService.deleteByScheduleId(id);

            sendMessage(resp, HttpServletResponse.SC_OK, "Schedule deleted.");
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        }
    }
}
