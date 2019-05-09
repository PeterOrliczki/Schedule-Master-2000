package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Activity;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);
            ;

            List<Activity> activities = userService.findAllActivity();

            sendMessage(response, HttpServletResponse.SC_OK, activities);
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }
}
