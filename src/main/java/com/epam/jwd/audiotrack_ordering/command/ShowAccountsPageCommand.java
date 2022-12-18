package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowAccountsPageCommand implements Command {

    private static final String JSP_ACCOUNTS_ATTRIBUTE_NAME = "accounts";
    private static final String ACCOUNTS_PAGE = "page.accounts";

    private static ShowAccountsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Account> service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static ShowAccountsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowAccountsPageCommand(ServiceFactory.getInstance().serviceFor(Account.class),
                            RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowAccountsPageCommand(EntityService<Account> service, RequestFactory requestFactory,
                                   PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Account> accounts = service.findAll();
        request.addAttributeToJSP(JSP_ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return requestFactory.createForwardResponse(propertyContext.get(ACCOUNTS_PAGE));
    }
}
