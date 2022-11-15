package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Account;

import java.util.Optional;

public interface AccountService extends EntityService<Account> {

    Optional<Account> authenticate(String account, String password);

    static AccountService getInstance() {
        return SimpleAccountService.getInstance();
    }
}
