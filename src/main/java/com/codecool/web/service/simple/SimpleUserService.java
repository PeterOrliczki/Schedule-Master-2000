package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.User;
import com.codecool.web.service.PasswordService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

public final class SimpleUserService implements UserService {

    private final UserDao userDao;

    public SimpleUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() throws SQLException {
        return userDao.findAllUsers();
    }

    public List<User> findAllUser() throws SQLException {
        return userDao.findAllUsers();
    }

    public User findUserById(int id) throws SQLException {
        return userDao.findUserById(id);
    }

    public User findUserByName(String name) throws SQLException {
        return userDao.findUserByName(name);
    }

    public User findUserByEmail(String email) throws SQLException {
        return userDao.findUserByEmail(email);
    }

    public User addUser(String name, String email, String password, Role role) throws SQLException {
        return userDao.addUser(name, email, password, role);
    }

    public void deleteUserById(int id) throws SQLException {
        userDao.deleteUserById(id);
    }

    public void updateUserNameById(String id, String name) throws SQLException {
        userDao.updateUserNameById(id, name);
    }

    public void updateUserEmailById(String id, String email) throws SQLException {
        userDao.updateUserEmailById(id, email);
    }

    public void updateUserPasswordById(String id, String password) throws SQLException {
        userDao.updateUserPasswordById(id, password);
    }

    public boolean doesUserExists(String email) throws SQLException, ServiceException {
        if (!userDao.doesUserExists(email)) {
            return false;
        } else {
            throw new ServiceException("Email already exists");
        }
    }

    @Override
    public User loginUser(String email, String password) throws SQLException, ServiceException {
        PasswordService passwordService = new PasswordService();
        try {
            User user = userDao.findUserByEmail(email);
            if (user == null || !passwordService.validatePassword(password, user.getPassword())) {
                throw new ServiceException("Bad login");
            }
            return user;
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
