package com.codecool.web.servlet;

import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.User;
import com.codecool.web.service.PasswordService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public final class LoginServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = authUser(req);
        try {
            if (currentUser != null) {
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Invalid username of password!");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (ServletException exc1) {
            exc1.printStackTrace();
        }
//        idk if well need this one or not
//        sendMessage(resp, HttpServletResponse.SC_OK, req.getSession().getAttribute("user"));
    }

    private User authUser(HttpServletRequest request) {
        User currentUser = null;
        try (Connection connection = getConnection(request.getServletContext())) {
            DatabaseUserDao userDao = new DatabaseUserDao(connection);
            SimpleUserService userService = new SimpleUserService(userDao);
            PasswordService passwordService = new PasswordService();

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            for (User user : userService.getUsers()) {
                if (email.equals(user.getEmail()) && passwordService.validatePassword(password, user.getPassword())) {
                    currentUser = user;
                    HttpSession session = request.getSession();
                    session.setAttribute("user", currentUser);
                }
            }

        } catch (SQLException exc1) {
            exc1.printStackTrace();
        } catch (NoSuchAlgorithmException exc2) {
            exc2.getMessage();
        } catch (InvalidKeySpecException exc3) {
            exc3.getMessage();
        }
        return currentUser;
    }
}
