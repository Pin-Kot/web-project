package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class AccountValidator {

    private static final String LOGIN_REGEX = "\\w{3,25}";
    private static final String PASSWORD_REGEX = "\\w{4,60}";

    private static AccountValidator instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static AccountValidator getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AccountValidator();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private AccountValidator() {
    }

    public boolean isLoginValid(String login) {
        if (login == null || login.isEmpty()) {
            return false;
        }
        return Pattern.compile(LOGIN_REGEX).matcher(login).matches();
    }

    public boolean isPasswordValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }

    public boolean isAllValid(String login, String password) {
        return isLoginValid(login) && isPasswordValid(password);
    }
}
