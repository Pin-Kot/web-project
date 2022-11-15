package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowUsersPageCommand implements Command {

    private static final String JSP_USERS_PATH = "/WEB-INF/jsp/users.jsp";
    private static final String JSP_USERS_ATTRIBUTE_NAME = "users";

    private static ShowUsersPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<User> service;

    public static ShowUsersPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowUsersPageCommand(ServiceFactory.getInstance().serviceFor(User.class));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowUsersPageCommand(EntityService<User> service) {
        this.service = service;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<User> users = service.findAll();
        request.addAttributeToJSP(JSP_USERS_ATTRIBUTE_NAME, users);
        return FORWARD_TO_USERS_PAGE;
    }

    private static final CommandResponse FORWARD_TO_USERS_PAGE = new CommandResponse() {

        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return JSP_USERS_PATH;
        }
    };
}
