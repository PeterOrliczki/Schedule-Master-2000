package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Activity;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/protected/activities")
public class ActivityLogServlet extends AbstractServlet {

    private static Logger logger = LoggerFactory.getLogger(ActivityLogServlet.class);
    private static Logger exceptionLogger = LoggerFactory.getLogger(ActivityLogServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            List<Activity> activities = userService.findAllActivity();

            logger.info("Loaded activity log.");
            sendMessage(response, HttpServletResponse.SC_OK, activities);
        } catch (SQLException exc) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", exc);
            handleSqlError(response, exc);
        }
    }
}
