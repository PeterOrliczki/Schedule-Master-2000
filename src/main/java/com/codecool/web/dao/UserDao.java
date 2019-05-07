package com.codecool.web.dao;

import com.codecool.web.model.Role;
import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    List<User> findAllUsers() throws SQLException;

    User addUser(String name, String email, String password, Role role) throws SQLException;

    User findUserById(int id) throws SQLException;

    User findUserByName(String name) throws SQLException;

    User findUserByEmail(String email) throws SQLException;

    void deleteUserById(int id) throws SQLException;

    void updateUserNameById(String id, String name) throws SQLException;

    void updateUserEmailById(String id, String email) throws SQLException;

    void updateUserPasswordById(String id, String password) throws SQLException;

    boolean doesUserExists(String name) throws SQLException;
}
