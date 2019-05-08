package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/my-schedules")
public class ScheduleServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            User user = (User)req.getSession().getAttribute("user");
            List<Schedule> schedules = scheduleService.findAllById(user.getId());
            sendMessage(resp, HttpServletResponse.SC_OK, schedules);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);

            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = (User)request.getSession().getAttribute("user");
            String title = request.getParameter("schedule-title");
            int duration = Integer.valueOf(request.getParameter("schedule-duration"));

            scheduleService.addSchedule(user.getId(), title, duration);

        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }
}
