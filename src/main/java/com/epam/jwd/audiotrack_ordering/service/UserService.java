package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.UserDao;
import com.epam.jwd.audiotrack_ordering.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService implements EntityService<User> {

    private final UserDao userDao;

    UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }

    @Override
    public Optional<User> create(User entity) {
        return Optional.empty();
    }
}
