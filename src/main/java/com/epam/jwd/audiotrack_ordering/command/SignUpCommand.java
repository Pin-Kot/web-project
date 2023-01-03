package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.AccountService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.validator.AccountValidator;

import java.util.concurrent.locks.ReentrantLock;

import static com.epam.jwd.audiotrack_ordering.entity.Role.USER;

public class SignUpCommand implements Command {

    private static final String INDEX_PAGE = "page.index";
    private static final String SIGN_UP_PAGE = "page.sign_up";


    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String DOUBLE_PASSWORD_REQUEST_PARAM_NAME = "doublePassword";

    private static final String ACCOUNT_HAS_ALREADY_LOGGED_IN_MESSAGE = "Account has already logged in";

    private static final String ERROR_SIGN_UP_PASS_ATTRIBUTE = "errorSignUpPassMessage";
    private static final String ERROR_SIGN_UP_PASS_MESSAGE = "Incorrect login or password";

    private static final String ERROR_ACCOUNT_EXIST_ATTRIBUTE = "errorAccountExistMessage";
    private static final String ACCOUNT_ALREADY_EXISTS_MESSAGE = "Account already exists";

    private static final String ERROR_PASSWORD_MISMATCH_ATTRIBUTE = "errorPasswordMismatchMessage";
    private static final String PASSWORDS_DO_NOT_MATCH_MESSAGE = "Passwords don't match";

    private static SignUpCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final AccountService accountService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static SignUpCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new SignUpCommand(ServiceFactory.getInstance().accountService(),
                            RequestFactory.getInstance(),
                            PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public SignUpCommand(AccountService accountService, RequestFactory requestFactory, PropertyContext propertyContext) {
        this.accountService = accountService;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME).isPresent()) {
            throw new IllegalArgumentException(ACCOUNT_HAS_ALREADY_LOGGED_IN_MESSAGE);
        }
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final String doublePassword = request.getParameter(DOUBLE_PASSWORD_REQUEST_PARAM_NAME);
        if (AccountValidator.getInstance().isAllValid(login, password) && doublePassword.equals(password)
                && !accountService.findAccountByLogin(login).isPresent()) {
            Account newAccount = new Account(login, password, USER);
            accountService.create(newAccount);
            request.addToSession(ACCOUNT_ATTRIBUTE_NAME, newAccount);
            return requestFactory.createForwardResponse(propertyContext.get(INDEX_PAGE));
        } else {
            if (accountService.findAccountByLogin(login).isPresent()) {
                request.addAttributeToJSP(ERROR_ACCOUNT_EXIST_ATTRIBUTE, ACCOUNT_ALREADY_EXISTS_MESSAGE);
            } else {
                if (!AccountValidator.getInstance().isAllValid(login, password)) {
                    request.addAttributeToJSP(ERROR_SIGN_UP_PASS_ATTRIBUTE, ERROR_SIGN_UP_PASS_MESSAGE);
                } else {
                    if (!doublePassword.equals(password)) {
                        request.addAttributeToJSP(ERROR_PASSWORD_MISMATCH_ATTRIBUTE, PASSWORDS_DO_NOT_MATCH_MESSAGE);
                    }
                }
            }
            return requestFactory.createForwardResponse(propertyContext.get(SIGN_UP_PAGE));
        }
    }
}
