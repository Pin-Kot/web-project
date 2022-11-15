package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Account;

import java.util.Optional;

public interface AccountDao extends EntityDao<Account> {

    Optional<Account> readAccountByLogin(String login);

    static AccountDao getInstance() {
        return MethodAccountDao.getInstance();
    }
}
