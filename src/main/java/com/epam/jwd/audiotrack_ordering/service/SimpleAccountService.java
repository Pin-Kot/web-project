package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.AccountDao;
import com.epam.jwd.audiotrack_ordering.entity.Account;

import java.util.List;
import java.util.Optional;

public class SimpleAccountService implements AccountService {

    private final AccountDao accountDao;

    public SimpleAccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Optional<Account> authenticate(String login, String password) {
        final Optional<Account> readAccount = accountDao.readAccountByLogin(login);
        return readAccount.filter(account -> account.getPassword().equals(password));
    }

    @Override
    public List<Account> findAll() {
        return accountDao.read();
    }

    static AccountService getInstance() {
        return SimpleAccountService.Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountService INSTANCE = new SimpleAccountService(AccountDao.getInstance());
    }

}
