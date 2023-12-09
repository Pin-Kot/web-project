package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.service.AlbumService;
import com.epam.jwd.audiotrack_ordering.service.ArtistService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;
import com.epam.jwd.audiotrack_ordering.validator.EnteredDataValidator;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddTrackCommand implements Command {

    private static final String ADD_TRACK_PAGE = "page.add_track";
    private static final String ADMIN_PAGE = "page.admin";

    private static final String ARTIST_NAME_REQUEST_PARAM_NAME = "artistName";
    private static final String ALBUM_TITLE_REQUEST_PARAM_NAME = "albumTitle";
    private static final String ALBUM_YEAR_REQUEST_PARAM_NAME = "albumYear";
    private static final String ALBUM_TYPE_REQUEST_PARAM_NAME = "albumType";
    private static final String TRACK_TITLE_REQUEST_PARAM_NAME = "trackTitle";
    private static final String TRACK_YEAR_REQUEST_PARAM_NAME = "trackYear";
    private static final String TRACK_PRICE_REQUEST_PARAM_NAME = "trackPrice";

    private static final String ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE = "errorIncorrectArtistNameMessage";
    private static final String ERROR_INCORRECT_ARTIST_NAME_MESSAGE = "Incorrect artist name";

    private static final String ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE = "errorIncorrectAlbumDataMessage";
    private static final String ERROR_INCORRECT_ALBUM_DATA_MESSAGE = "Incorrect album's data";

    private static final String ERROR_INCORRECT_TITLE_DATA_ATTRIBUTE = "errorIncorrectTitleDataMessage";
    private static final String ERROR_INCORRECT_TITLE_DATA_MESSAGE = "Incorrect title's data";

    private static final String ERROR_ARTIST_OR_ALBUM_DO_NOT_EXIST_ATTRIBUTE = "errorArtistOrAlbumDoNotExistMessage";
    private static final String ARTIST_OR_ALBUM_DO_NOT_EXIST_MESSAGE = "The artist or the album don't exist";

    private static AddTrackCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final TrackService trackService;

    public AddTrackCommand(RequestFactory requestFactory, PropertyContext propertyContext, AlbumService albumService,
                           ArtistService artistService, TrackService trackService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.albumService = albumService;
        this.artistService = artistService;
        this.trackService = trackService;
    }

    public static AddTrackCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddTrackCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().albumService(), ServiceFactory.getInstance().artistService(),
                            ServiceFactory.getInstance().trackService());
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

        final String albumTitle = request.getParameter(ALBUM_TITLE_REQUEST_PARAM_NAME);
        final String albumYearFromRequest = request.getParameter(ALBUM_YEAR_REQUEST_PARAM_NAME);
        final String albumType = request.getParameter(ALBUM_TYPE_REQUEST_PARAM_NAME);

        final String trackTitle = request.getParameter(TRACK_TITLE_REQUEST_PARAM_NAME);
        final String trackYearFromRequest = request.getParameter(TRACK_YEAR_REQUEST_PARAM_NAME);
        final String trackPriceFromRequest = request.getParameter(TRACK_PRICE_REQUEST_PARAM_NAME);

        if (!EnteredDataValidator.getInstance().isTitleValid(artistName)) {
            request.addAttributeToJSP(ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE, ERROR_INCORRECT_ARTIST_NAME_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_TRACK_PAGE));
        }

        EnteredDataValidator validator = EnteredDataValidator.getInstance();

        if (!validator.isAlbumDataValid(albumTitle, albumYearFromRequest, albumType)) {
            request.addAttributeToJSP(ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE, ERROR_INCORRECT_ALBUM_DATA_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_TRACK_PAGE));
        }

        int albumYear = Integer.parseInt(albumYearFromRequest);

        final Optional<Artist> artistFromRequest = artistService.findByName(artistName);
        final Optional<Album> albumFromRequest = albumService.findByTitleByYear(albumTitle, albumYear);

        if (artistFromRequest.isPresent() && albumFromRequest.isPresent()) {
            final boolean isCreated = trackService.createByArtistByAlbumWithValidator(artistFromRequest.get(),
                    albumFromRequest.get(), trackTitle, trackYearFromRequest, trackPriceFromRequest);
            if (!isCreated) {
                request.addAttributeToJSP(ERROR_INCORRECT_TITLE_DATA_ATTRIBUTE, ERROR_INCORRECT_TITLE_DATA_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(ADD_TRACK_PAGE));
            }
            return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
        }
        request.addAttributeToJSP(ERROR_ARTIST_OR_ALBUM_DO_NOT_EXIST_ATTRIBUTE, ARTIST_OR_ALBUM_DO_NOT_EXIST_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(ADD_TRACK_PAGE));
    }
}
