package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowTracksPageCommand implements Command {

    private static final String TRACKS_PAGE = "page.tracks";
    private static final String JSP_TRACKS_ATTRIBUTE_NAME = "tracks";

    private static ShowTracksPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final EntityService<Track> service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public static ShowTracksPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowTracksPageCommand(ServiceFactory.getInstance().serviceFor(Track.class),
                            RequestFactory.getInstance(), PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public ShowTracksPageCommand(EntityService<Track> track, RequestFactory requestFactory, PropertyContext propertyContext) {
        this.service = track;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Track> tracks = service.findAll();
        request.addAttributeToJSP(JSP_TRACKS_ATTRIBUTE_NAME, tracks);
        return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
    }
}
