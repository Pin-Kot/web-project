package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowAccountsPageCommand implements Command {

    private static final String JSP_ACCOUNTS_PATH = "/WEB-INF/jsp/accounts.jsp";
    private static final String JSP_ACCOUNTS_ATTRIBUTE_NAME = "accounts";

    private static ShowAccountsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Account> service;

    public static ShowAccountsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowAccountsPageCommand(ServiceFactory.getInstance().serviceFor(Account.class));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowAccountsPageCommand(EntityService<Account> service) {
        this.service = service;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Account> accounts = service.findAll();
        request.addAttributeToJSP(JSP_ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return FORWARD_TO_ACCOUNTS_PAGE;
    }

    private static final CommandResponse FORWARD_TO_ACCOUNTS_PAGE = new CommandResponse() {

        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return JSP_ACCOUNTS_PATH;
        }
    };
}
