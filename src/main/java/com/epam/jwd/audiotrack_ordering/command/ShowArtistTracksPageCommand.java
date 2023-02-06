package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowArtistTracksPageCommand implements Command {

    private static final String JSP_ARTIST_TRACKS_ATTRIBUTE_NAME = "tracks";
    private static final String TRACKS_PAGE = "page.tracks";
    private static final String ARTIST_NAME_REQUEST_PARAM_NAME = "artist_name";

    private static ShowArtistTracksPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final TrackService service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowArtistTracksPageCommand(TrackService service, RequestFactory requestFactory,
                                       PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowArtistTracksPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowArtistTracksPageCommand(ServiceFactory.getInstance().trackService(),
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
        final List<Track> artistTracks = service.findTracksByArtistName(artistName);
        request.addAttributeToJSP(JSP_ARTIST_TRACKS_ATTRIBUTE_NAME, artistTracks);
        return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
    }
}
