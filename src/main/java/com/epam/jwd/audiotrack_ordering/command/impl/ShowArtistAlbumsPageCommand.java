package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.service.AlbumService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowArtistAlbumsPageCommand implements Command {

    private static final String ARTIST_NAME_REQUEST_PARAM_NAME = "artistName";
    private static final String JSP_ARTIST_ALBUMS_ATTRIBUTE_NAME = "albums";
    private static final String ALBUMS_PAGE = "page.albums";

    private static ShowArtistAlbumsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final AlbumService service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowArtistAlbumsPageCommand(AlbumService service, RequestFactory requestFactory,
                                       PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowArtistAlbumsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowArtistAlbumsPageCommand(ServiceFactory.getInstance().albumService(),
                            RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String artistName = request.getParameter(ARTIST_NAME_REQUEST_PARAM_NAME);
        final List<Album> artistAlbums = service.findAlbumsByArtistName(artistName);
        request.addAttributeToJSP(JSP_ARTIST_ALBUMS_ATTRIBUTE_NAME, artistAlbums);
        return requestFactory.createForwardResponse(propertyContext.get(ALBUMS_PAGE));
    }
}
