package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.User;
import java.util.Optional;

public interface UserService extends EntityService<User> {

        Optional<User> findUserByAccountId(Long accountId);

}
