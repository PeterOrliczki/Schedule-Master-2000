package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.User;
import com.codecool.web.service.PasswordService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;
import com.codecool.web.service.simple.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends AbstractServlet {

    private static Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private static Logger exceptionLogger = LoggerFactory.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);
            PasswordService passwordService = new PasswordService();

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            Role userRole = Role.REGULAR;

            if (!userService.doesUserExists(email)) {
                User user = userService.addUser(name, email, passwordService.getHashedPassword(password), userRole);
                req.setAttribute("user", user);
                logger.info("Successfully completed registration as " + user.getEmail() + ".");
                sendMessage(resp, HttpServletResponse.SC_OK, user);
            }

        } catch (ServiceException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("Service exception occurred at: ", ex);
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (SQLException ex) {
            logger.warn("Exception occurred while processing request - For more information see the exception log file.");
            logger.error("SQL exception occurred at: ", ex);
            handleSqlError(resp, ex);
        }
    }

}
