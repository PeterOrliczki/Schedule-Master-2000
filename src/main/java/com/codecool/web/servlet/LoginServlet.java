package com.codecool.web.servlet;

import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public final class LoginServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            DatabaseUserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            String email = req.getParameter("email");
            String password = req.getParameter("password");

            for (User user : SimpleUserService)

                sendMessage(resp, HttpServletResponse.SC_OK, req.getSession().getAttribute("user"));
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }
}
