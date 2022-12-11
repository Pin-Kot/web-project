package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowAlbumsPageCommand implements Command {

    private static final String ALBUMS_PAGE = "page.albums";
    private static final String JSP_ALBUMS_ATTRIBUTE_NAME = "albums";

    private static ShowAlbumsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Album> service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static ShowAlbumsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowAlbumsPageCommand(ServiceFactory.getInstance().serviceFor(Album.class),
                            RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowAlbumsPageCommand(EntityService<Album> service, RequestFactory requestFactory, PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Album> albums = service.findAll();
        request.addAttributeToJSP(JSP_ALBUMS_ATTRIBUTE_NAME, albums);
        return requestFactory.createForwardResponse(propertyContext.get(ALBUMS_PAGE));
    }

}
