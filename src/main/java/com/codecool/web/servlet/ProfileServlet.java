package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;
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

@WebServlet("/protected/profile")

public class ProfileServlet extends AbstractServlet {

    private final ObjectMapper om = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(ProfileServlet.class);
    private static Logger exceptionLogger = LoggerFactory.getLogger(ProfileServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        logger.info("Loaded profile for " + user.getName() + ".");
        sendMessage(response, HttpServletResponse.SC_OK, user);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            User user = om.readValue(req.getInputStream(), User.class);
            userService.updateUserNameById(user.getId(), user.getName());
            userService.updateUserEmailById(user.getId(), user.getEmail());
            userService.updateUserPasswordById(user.getId(), user.getPassword());
            req.getSession().setAttribute("user", user);
            logger.info("Updated user data for " + user.getName() + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, "Your data has been updated.");
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }
}
