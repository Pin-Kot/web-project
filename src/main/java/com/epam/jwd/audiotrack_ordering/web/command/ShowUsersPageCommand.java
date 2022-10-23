package com.epam.jwd.audiotrack_ordering.web.command;

import java.util.concurrent.locks.ReentrantLock;

public class ShowUsersPageCommand implements Command {

    private static ShowUsersPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static ShowUsersPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowUsersPageCommand();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private static final CommandResponse FORWARD_TO_USERS_PAGE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/users.jsp";
        }
    };

    @Override
    public CommandResponse execute(CommandRequest request) {
        return FORWARD_TO_USERS_PAGE;
    }
}
