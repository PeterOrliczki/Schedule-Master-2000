package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dto.ScheduleListDto;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.exception.ServiceException;
import com.codecool.web.service.simple.SimpleScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/schedules")
public class SchedulesServlet extends AbstractServlet {

    private static Logger logger = LoggerFactory.getLogger(SchedulesServlet.class);
    private static Logger exceptionLogger = LoggerFactory.getLogger(SchedulesServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            User user = (User) req.getSession().getAttribute("user");
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);

            ScheduleListDto schedules = new ScheduleListDto(scheduleService.findAll(user), scheduleService.findAllById(user.getId()));
            int numOfSchedules = schedules.getMySchedules().size() + schedules.getPublicSchedules().size();

            logger.info("Loaded " + numOfSchedules + " schedules.");
            sendMessage(resp, HttpServletResponse.SC_OK, schedules);
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        } catch (ServiceException e) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("Service exception occurred at: ", e);
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
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

            logger.info("New schedule created by " + user.getName() + ".");
            sendMessage(response, HttpServletResponse.SC_OK, "Schedule added.");
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
            handleSqlError(response, exc);
        }
    }
}
