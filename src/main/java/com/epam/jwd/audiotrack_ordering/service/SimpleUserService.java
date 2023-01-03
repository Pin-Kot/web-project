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

    static SimpleUserService getInstance() {
        return SimpleUserService.Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimpleUserService INSTANCE = new SimpleUserService(UserDao.getInstance());
    }

    @Override
    public Optional<User> findUserByAccountId(Long accountId) {
        return userDao.findUserByAccountId(accountId);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return Optional.ofNullable(userDao.update(user));
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }

    @Override
    public Optional<User> create(User user) {
        return Optional.ofNullable(userDao.create(user));
    }
}
