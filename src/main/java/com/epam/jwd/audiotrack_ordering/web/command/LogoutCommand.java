package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.web.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class LogoutCommand implements Command {

    private static final String ACCOUNT_SESSION_ATTRIBUTE_NAME = "account";
    private static final String INDEX_PATH = "/";

    private static LogoutCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;

    public static LogoutCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new LogoutCommand(RequestFactory.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public LogoutCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (noLoggedInAccountPresent(request)) {
            //
            return null;
        }
        request.clearSession();
        return requestFactory.createRedirectResponse(INDEX_PATH);
    }

    private boolean noLoggedInAccountPresent(CommandRequest request) {
        return !request.sessionExists()
                || (request.sessionExists()
                        && !request.retrieveFromSession(ACCOUNT_SESSION_ATTRIBUTE_NAME).isPresent());
    }
}
