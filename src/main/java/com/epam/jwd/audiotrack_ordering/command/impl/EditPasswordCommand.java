package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.AccountService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.validator.AccountValidator;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class EditPasswordCommand implements Command {

    private static final String EDIT_PASSWORD_PAGE = "page.edit_password";
    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String DOUBLE_PASSWORD_REQUEST_PARAM_NAME = "doublePassword";

    private static final String ERROR_EDIT_PASSWORD_ATTRIBUTE = "errorEditPasswordMessage";
    private static final String ERROR_EDIT_PASSWORD_MESSAGE = "Entered incorrect password or passwords don't match";
    private static final String ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE = "errorAccountDoesNotExistMessage";
    private static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Account doesn't exist";

    private static EditPasswordCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final AccountService accountService;


    public EditPasswordCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                               AccountService accountService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.accountService = accountService;
    }

    public static EditPasswordCommand getInstance() {
       if (instance == null) {
           try {
               LOCK.lock();
               if (instance == null) {
                   instance = new EditPasswordCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                           ServiceFactory.getInstance().accountService());
               }
           } finally {
               LOCK.unlock();
           }
       }
       return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME);

        if (request.sessionExists() && accountFromSession.isPresent()){

            final Account account = (Account) accountFromSession.get();
            final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
            final String doublePassword = request.getParameter(DOUBLE_PASSWORD_REQUEST_PARAM_NAME);

            if (AccountValidator.getInstance().isPasswordValid(password) && doublePassword.equals(password)) {
                accountService.update(new Account(account.getId(), account.getLogin(), password, account.getRole()));
                request.clearSession();
                return requestFactory.createForwardResponse(propertyContext.get(INDEX_PAGE));
            } else {
                request.addAttributeToJSP(ERROR_EDIT_PASSWORD_ATTRIBUTE,ERROR_EDIT_PASSWORD_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(EDIT_PASSWORD_PAGE));
            }
        }
        request.addAttributeToJSP(ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE, ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
