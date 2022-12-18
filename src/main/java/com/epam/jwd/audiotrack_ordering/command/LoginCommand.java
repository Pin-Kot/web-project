package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.AccountService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class LoginCommand implements Command {

    private static final String INDEX_PAGE = "page.index";
    private static final String LOGIN_PAGE = "page.login";

    private static final String ACCOUNT_SESSION_ATTRIBUTE_NAME = "account";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";

    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Incorrect login or password";

    private static LoginCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final AccountService accountService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static LoginCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new LoginCommand(ServiceFactory.getInstance().accountService(),
                            RequestFactory.getInstance(),
                            PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public LoginCommand(AccountService accountService, RequestFactory requestFactory,
                        PropertyContext propertyContext) {
        this.accountService = accountService;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final Optional<Account> account = accountService.authenticate(login, password);
        if (!account.isPresent()) {
            request.addAttributeToJSP(ERROR_LOGIN_PASS_ATTRIBUTE, ERROR_LOGIN_PASS_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(LOGIN_PAGE));
        }
        request.addToSession(ACCOUNT_SESSION_ATTRIBUTE_NAME, account.get());
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
