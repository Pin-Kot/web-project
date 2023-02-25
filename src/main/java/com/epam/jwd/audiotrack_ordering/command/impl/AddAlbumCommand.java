package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.entity.dto.AlbumArtistLink;
import com.epam.jwd.audiotrack_ordering.service.AlbumService;
import com.epam.jwd.audiotrack_ordering.service.ArtistService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.relation.AlbumArtistLinkService;
import com.epam.jwd.audiotrack_ordering.service.relation.LinkService;
import com.epam.jwd.audiotrack_ordering.validator.ArtistValidator;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddAlbumCommand implements Command {

    private static final String ADD_ALBUM_PAGE = "page.add_album";
    private static final String ADMIN_PAGE = "page.admin";

    private static final String ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE = "errorIncorrectArtistNameMessage";
    private static final String ERROR_INCORRECT_ARTIST_NAME_MESSAGE = "Incorrect artist name";

    private static final String ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE = "errorIncorrectAlbumDataMessage";
    private static final String ERROR_INCORRECT_ALBUM_DATA_MESSAGE = "Incorrect entered album's data";

    private static final String ERROR_ARTIST_DOES_NOT_EXIST_ATTRIBUTE = "errorArtistDoesNotExistMessage";
    private static final String ARTIST_DOES_NOT_EXIST_MESSAGE = "The artist doesn't exist";

    private static final String ERROR_ALBUM_DOES_NOT_EXIST_ATTRIBUTE = "errorAlbumDoesNotExistMessage";
    private static final String ALBUM_DOES_NOT_EXIST_MESSAGE = "The album doesn't exist";
    public static final String ARTIST_NAME_REQUEST_PARAM_NAME = "artistName";
    public static final String ALBUM_TITLE_REQUEST_PARAM_NAME = "albumTitle";
    public static final String ALBUM_YEAR_REQUEST_PARAM_NAME = "albumYear";
    public static final String ALBUM_TYPE_REQUEST_PARAM_NAME = "albumType";

    private static AddAlbumCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final LinkService<AlbumArtistLink> linkService;

    public AddAlbumCommand(RequestFactory requestFactory, PropertyContext propertyContext, AlbumService albumService,
                           ArtistService artistService, LinkService<AlbumArtistLink> linkService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.albumService = albumService;
        this.artistService = artistService;
        this.linkService = linkService;
    }

    public static AddAlbumCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddAlbumCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().albumService(), ServiceFactory.getInstance().artistService(),
                            AlbumArtistLinkService.getInstance());
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
        final String title = request.getParameter(ALBUM_TITLE_REQUEST_PARAM_NAME);
        final String yearFromRequest = request.getParameter(ALBUM_YEAR_REQUEST_PARAM_NAME);
        final String type = request.getParameter(ALBUM_TYPE_REQUEST_PARAM_NAME);
        if (ArtistValidator.getInstance().isNameInvalid(artistName)) {
            request.addAttributeToJSP(ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE, ERROR_INCORRECT_ARTIST_NAME_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_ALBUM_PAGE));
        }
        final Optional<Artist> artistFromRequest = artistService.findByName(artistName);
        if (!artistFromRequest.isPresent()) {
            request.addAttributeToJSP(ERROR_ARTIST_DOES_NOT_EXIST_ATTRIBUTE, ARTIST_DOES_NOT_EXIST_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_ALBUM_PAGE));
        }
        final boolean isCreated = albumService.createByArtistNameWithValidator(title, yearFromRequest, type,
                artistFromRequest.get().getName());
        if (isCreated) {
            final int year = Integer.parseInt(yearFromRequest);
            Optional<Album> newAlbum = albumService.findByTitleByYear(title, year);
            if (!newAlbum.isPresent()) {
                request.addAttributeToJSP(ERROR_ALBUM_DOES_NOT_EXIST_ATTRIBUTE, ALBUM_DOES_NOT_EXIST_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(ADD_ALBUM_PAGE));
            }
            linkService.create(new AlbumArtistLink(newAlbum.get().getId(), artistFromRequest.get().getId()));
            return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
        }
        request.addAttributeToJSP(ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE, ERROR_INCORRECT_ALBUM_DATA_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(ADD_ALBUM_PAGE));
    }
}
