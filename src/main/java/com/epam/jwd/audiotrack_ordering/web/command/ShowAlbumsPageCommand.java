package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.web.controller.RequestFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowAlbumsPageCommand implements Command {

    private static final String JSP_ALBUMS_PATH = "/WEB-INF/jsp/albums.jsp";
    private static final String JSP_ALBUMS_ATTRIBUTE_NAME = "albums";

    private static ShowAlbumsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Album> service;
    private final RequestFactory requestFactory;

    public static ShowAlbumsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowAlbumsPageCommand(ServiceFactory.getInstance().serviceFor(Album.class),
                            RequestFactory.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowAlbumsPageCommand(EntityService<Album> service, RequestFactory requestFactory) {
        this.service = service;
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Album> albums = service.findAll();
        request.addAttributeToJSP(JSP_ALBUMS_ATTRIBUTE_NAME, albums);
        return requestFactory.createForwardResponse(JSP_ALBUMS_PATH);
    }

}
