package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowArtistsPageCommand implements Command {

    private static final String JSP_ARTISTS_ATTRIBUTE_NAME = "artists";
    private static final String ARTISTS_PAGE = "page.artists";


    private static ShowArtistsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Artist> service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static ShowArtistsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowArtistsPageCommand(ServiceFactory.getInstance().serviceFor(Artist.class),
                            RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowArtistsPageCommand(EntityService<Artist> service, RequestFactory requestFactory,
                                  PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Artist> artists = service.findAll();
        request.addAttributeToJSP(JSP_ARTISTS_ATTRIBUTE_NAME, artists);
        return requestFactory.createForwardResponse(propertyContext.get(ARTISTS_PAGE));
    }
}
