package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.web.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowLoginPageCommand implements Command {

    private static final String JSP_LOGIN_PATH = "/WEB-INF/jsp/login.jsp";

    private static ShowLoginPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;

    public static ShowLoginPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowLoginPageCommand(RequestFactory.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowLoginPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(JSP_LOGIN_PATH);
    }
}
