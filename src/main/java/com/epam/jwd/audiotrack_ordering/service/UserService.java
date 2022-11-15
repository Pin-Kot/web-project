package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.UserDao;
import com.epam.jwd.audiotrack_ordering.entity.User;

import java.util.List;

public class UserService implements EntityService<User> {

    private final UserDao userDao;

    UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }
}
