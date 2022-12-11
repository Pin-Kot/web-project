package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Entity;

public interface ServiceFactory {

    <T extends Entity> EntityService<T> serviceFor(Class<T> entityClass);

    default AccountService accountService() {
        return (AccountService) serviceFor(Account.class);
    }

    static ServiceFactory getInstance() {
        return SimpleServiceFactory.getInstance();
    }
}
