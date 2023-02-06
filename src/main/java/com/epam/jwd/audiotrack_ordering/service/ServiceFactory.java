package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Entity;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.entity.User;

public interface ServiceFactory {

    <T extends Entity> EntityService<T> serviceFor(Class<T> entityClass);

    default AccountService accountService() {
        return (AccountService) serviceFor(Account.class);
    }

    default UserService userService() {
        return (UserService) serviceFor(User.class);
    }

    default TrackService trackService() {
        return (TrackService) serviceFor(Track.class);
    }

    static ServiceFactory getInstance() {
        return SimpleServiceFactory.getInstance();
    }
}
