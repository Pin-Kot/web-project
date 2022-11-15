package com.epam.jwd.audiotrack_ordering.web.command;

import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowArtistsPageCommand implements Command {

    private static final String JSP_ARTISTS_PATH = "/WEB-INF/jsp/artists.jsp";
    private static final String JSP_ARTISTS_ATTRIBUTE_NAME = "artists";


    private static ShowArtistsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Artist> service;

    public static ShowArtistsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowArtistsPageCommand(ServiceFactory.getInstance().serviceFor(Artist.class));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowArtistsPageCommand(EntityService<Artist> service) {
        this.service = service;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Artist> artists = service.findAll();
        request.addAttributeToJSP(JSP_ARTISTS_ATTRIBUTE_NAME, artists);
        return FORWARD_TO_ARTISTS_PAGE;
    }

    private static final CommandResponse FORWARD_TO_ARTISTS_PAGE = new CommandResponse() {

        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return JSP_ARTISTS_PATH;
        }
    };
}
