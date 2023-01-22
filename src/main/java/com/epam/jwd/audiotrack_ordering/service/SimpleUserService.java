package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.UserDao;
import com.epam.jwd.audiotrack_ordering.entity.User;

import java.util.List;
import java.util.Optional;

public class SimpleUserService implements UserService {

    private final UserDao userDao;

    public SimpleUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findUserByAccountId(Long accountId) {
        return userDao.findUserByAccountId(accountId);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }

    @Override
    public void create(User user) {
        userDao.create(user);
    }
}
