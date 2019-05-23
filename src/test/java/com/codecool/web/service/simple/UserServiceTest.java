package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static UserService userService;

    @BeforeAll
    static void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/scheduletest", "postgres", "admin");
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/init.sql"));

        UserDao userDao = new DatabaseUserDao(connection);
        userService = new SimpleUserService(userDao);
    }

    @Test
    void findAllUser() throws SQLException {
        int result = userService.findAllUser().size();
        assertEquals(7, result);
    }

    @Test
    void findUserById() throws SQLException {
        String result = userService.findUserById(1).getName();
        assertEquals("a", result);
    }

    @Test
    void findUserByName() throws SQLException {
        String result = userService.findUserByName("a").getName();
        assertEquals("a", result);
    }

    @Test
    void findUserByEmail() throws SQLException {
        String result = userService.findUserByName("a").getName();
        assertEquals("a", result);
    }

    @Test
    void findAllActivity() throws SQLException {
        int result = userService.findAllActivity().size();
        assertEquals(29, result);
    }

    @Test
    void addUser() throws SQLException {
        User user = new User(6, "test name", "test email", "test password", Role.ADMIN);
        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        User result = userService.findUserById(user.getId());
        assertEquals(user, result);
    }

    @Test
    void deleteUserById() throws SQLException {
        User user = new User(6, "test name", "test email", "test password", Role.ADMIN);
        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        userService.deleteUserById(user.getId());
        User result = userService.findUserById(user.getId());
        assertNull(result);
    }

    @Test
    void updateUserNameById() throws SQLException {
        User user = new User(6, "test name", "test email", "test password", Role.ADMIN);
        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        userService.updateUserNameById(6, "name updated");
        User result = userService.findUserById(6);
        assertEquals("name updated", result.getName());
    }

    @Test
    void updateUserEmailById() throws SQLException {
        User user = new User(6, "test name", "test email", "test password", Role.ADMIN);
        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        userService.updateUserEmailById(6, "email updated");
        User result = userService.findUserById(6);
        assertEquals("email updated", result.getEmail());
    }

    @Test
    void updateUserPasswordById() throws SQLException {
        User user = new User(6, "test name", "test email", "test password", Role.ADMIN);
        userService.addUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        userService.updateUserPasswordById(6, "password updated");
        User result = userService.findUserById(6);
        assertEquals("password updated", result.getPassword());
    }

    @Test
    void doesUserExists() throws SQLException, ServiceException {
        boolean result = userService.doesUserExists("test email new");
        assertFalse(result);
    }
}
