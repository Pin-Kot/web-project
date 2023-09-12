package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowAddImageCommand implements Command {

    private static final String ADD_IMAGE_PAGE = "page.add_image";

    private static ShowAddImageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowAddImageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowAddImageCommand getInstance() {
            if (instance == null) {
                try {
                    LOCK.lock();
                    if (instance == null) {
                        instance = new ShowAddImageCommand(RequestFactory.getInstance(), PropertyContext.getInstance());
                    }
                } finally {
                    LOCK.unlock();
                }
            }
            return instance;
        }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(ADD_IMAGE_PAGE));
    }
}
