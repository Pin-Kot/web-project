package com.epam.jwd.audiotrack_ordering.service.impl;

import com.epam.jwd.audiotrack_ordering.dao.UserDao;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.UserService;

import java.util.List;
import java.util.Optional;

public class SimpleUserService implements UserService {

    private final UserDao userDao;

    public SimpleUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public Optional<User> find(Long id) {
        return userDao.find(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
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
    public boolean delete(Long id) {
        return false;
    }
}
