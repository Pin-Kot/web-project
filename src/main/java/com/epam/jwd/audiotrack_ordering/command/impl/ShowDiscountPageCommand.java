package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowDiscountPageCommand implements Command {

    private static final String ASSIGN_DISCOUNT_PAGE = "page.assign_discount";

    private static ShowDiscountPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowDiscountPageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowDiscountPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowDiscountPageCommand(RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(ASSIGN_DISCOUNT_PAGE));
    }
}
