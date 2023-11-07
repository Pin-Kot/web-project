package com.epam.jwd.audiotrack_ordering.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.audiotrack_ordering.dao.AccountDao;
import com.epam.jwd.audiotrack_ordering.entity.Account;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;

public class SimpleAccountService implements AccountService {

    private static final byte[] DUMMY_PASSWORD = "password".getBytes(StandardCharsets.UTF_8);
    private final AccountDao accountDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;

    public SimpleAccountService(AccountDao accountDao, BCrypt.Hasher hasher, BCrypt.Verifyer verifyer) {
        this.accountDao = accountDao;
        this.hasher = hasher;
        this.verifyer = verifyer;
    }

    @Override
    @Transactional
    public Optional<Account> authenticate(String login, String password) {
        if (login == null || password == null) {
            return Optional.empty();
        }
        final byte[] enteredPassword = password.getBytes(StandardCharsets.UTF_8);
        final Optional<Account> readAccount = accountDao.findAccountByLogin(login);
        if (readAccount.isPresent()) {
            final byte[] hashedPassword = readAccount.get()
                    .getPassword()
                    .getBytes(StandardCharsets.UTF_8);
            return verifyer.verify(enteredPassword, hashedPassword).verified
                    ? readAccount
                    : Optional.empty();
        } else {
            protectFromAttack(enteredPassword);
            return Optional.empty();
        }
    }

    @Override
    public void create(Account account) {
        accountDao.create(account.withPassword(retrieveHashedPassword(account.getPassword())));
    }

    private void protectFromAttack(byte[] enteredPassword) {
        verifyer.verify(enteredPassword, DUMMY_PASSWORD);
    }

    @Override
    public Optional<Account> find(Long id) {
        return accountDao.find(id);
    }

    @Override
    public Optional<Account> findAccountByLogin(String login) {
        return accountDao.findAccountByLogin(login);
    }

    @Override
    public void update(Account account) {
        accountDao.update(account.withPassword(retrieveHashedPassword(account.getPassword())));
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }


    private String retrieveHashedPassword(String password) {
        final char[] rawPassword = password.toCharArray();
        return hasher.hashToString(MIN_COST, rawPassword);
    }
}
