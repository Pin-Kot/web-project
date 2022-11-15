package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.AccountService;
import com.epam.jwd.audiotrack_ordering.web.controller.RequestFactory;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class LoginCommand implements Command {

    private static final String INDEX_PATH = "/";
    private static final String JSP_LOGIN_PATH = "/WEB-INF/jsp/login.jsp";

    private static final String ACCOUNT_SESSION_ATTRIBUTE_NAME = "account";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";

    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Incorrect login or password";

    private static LoginCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final AccountService accountService;
    private final RequestFactory requestFactory;

    public static LoginCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new LoginCommand(AccountService.getInstance(), RequestFactory.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public LoginCommand(AccountService accountService, RequestFactory requestFactory) {
        this.accountService = accountService;
        this.requestFactory = requestFactory;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(ACCOUNT_SESSION_ATTRIBUTE_NAME).isPresent()) {
            //
            return null;
        }
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final Optional<Account> account = accountService.authenticate(login, password);
        if (!account.isPresent()) {
            request.addAttributeToJSP(ERROR_LOGIN_PASS_ATTRIBUTE, ERROR_LOGIN_PASS_MESSAGE);
            return requestFactory.createForwardResponse(JSP_LOGIN_PATH);
        }
        request.clearSession();
        request.createSession();
        request.addToSession(ACCOUNT_SESSION_ATTRIBUTE_NAME, account.get());
        return requestFactory.createRedirectResponse(INDEX_PATH);
    }
}
