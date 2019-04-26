package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.service.UserService;
//import javafx.concurrent.Service;

public final class SimpleUserService implements UserService {

    private final UserDao userDao;

    public SimpleUserService(UserDao userDao) {
        this.userDao = userDao;
    }

}
