package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.UserService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ShowPersonalDataCommand implements Command {

    private static final String PERSONAL_DATA_PAGE = "page.personal_data";
    private static final String ACCOUNT_REQUEST_PARAM_NAME = "account";
    private static final String JSP_USER_ATTRIBUTE_NAME = "user";

    private static final String ERROR_INVALID_USER_DATA_ATTRIBUTE = "errorInvalidUserData";
    private static final String ERROR_INVALID_USER_DATA_MESSAGE = "User data is not defined, please enter data";

    private static ShowPersonalDataCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final UserService userService;

    public ShowPersonalDataCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                                   UserService userService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = userService;
    }

    public static ShowPersonalDataCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowPersonalDataCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().userService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_REQUEST_PARAM_NAME);
        if (accountFromSession.isPresent()) {
            final Account account = (Account) accountFromSession.get();
            if (userService.findUserByAccountId(account.getId()).isPresent()) {
                final User user = userService.findUserByAccountId(account.getId()).get();
                request.addAttributeToJSP(JSP_USER_ATTRIBUTE_NAME, user);
            } else {
                request.addAttributeToJSP(ERROR_INVALID_USER_DATA_ATTRIBUTE, ERROR_INVALID_USER_DATA_MESSAGE);
            }
        }
        return requestFactory.createForwardResponse(propertyContext.get(PERSONAL_DATA_PAGE));
    }
}
