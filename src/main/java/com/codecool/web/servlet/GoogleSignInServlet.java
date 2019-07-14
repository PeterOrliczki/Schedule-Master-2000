package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.User;
import com.codecool.web.service.PasswordService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;


@WebServlet("/googlesignin")
public final class GoogleSignInServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            PasswordService passwordService = new PasswordService();
            JsonFactory jsonFactory = new JacksonFactory();
            HttpTransport transport = new NetHttpTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("442693073873-plpoc0nhj0nqn5c69bubk73pa3sohg0a.apps.googleusercontent.com"))
                .build();

            try {
                String id = req.getParameter("idtoken");
                GoogleIdToken idToken = verifier.verify(id);
                if (idToken != null) {
                    Payload payload = idToken.getPayload();




                    String email = payload.getEmail();
                    boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                    String name = (String) payload.get("name");



                    UserDao userDao = new DatabaseUserDao(connection);
                    UserService userService = new SimpleUserService(userDao);
                    User user;
                    if (userService.findUserByEmail(email) != null) {
                        user = userService.findUserByEmail(email);
                    } else {
                        user = userService.addUser(name, email, passwordService.getHashedPassword(id), Role.REGULAR);
                    }

                    req.getSession().setAttribute("user", user);
                    sendMessage(resp, HttpServletResponse.SC_OK, user);
                } else {
                    System.out.println("Invalid ID token.");
                }
            } catch (GeneralSecurityException ex) {
                sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }
}
