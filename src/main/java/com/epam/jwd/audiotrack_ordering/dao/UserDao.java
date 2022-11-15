package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.User;

import java.util.Optional;

public interface UserDao extends EntityDao<User> {

    Optional<User> findByEmail(String email);

    static UserDao getInstance() {
        return MethodUserDao.getInstance();
    }
}
