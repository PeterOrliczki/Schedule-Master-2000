package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dto.ScheduleListDto;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.exception.ServiceException;
import com.codecool.web.service.simple.SimpleScheduleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/schedules")
public class SchedulesServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            ScheduleListDto schedules = new ScheduleListDto(scheduleService.findAll(user), scheduleService.findAllById(user.getId()));

            sendMessage(resp, HttpServletResponse.SC_OK, schedules);
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            int taskId = Integer.parseInt(req.getParameter("task-id"));
            int columnNumber = Integer.parseInt(req.getParameter("columnNumber"));
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));



            sendMessage(resp, HttpServletResponse.SC_OK, "Task added.");
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        /*} catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());*/
        }
    }
}
