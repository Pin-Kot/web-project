package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.service.ArtistService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.validator.ArtistValidator;

import java.util.concurrent.locks.ReentrantLock;

public class AddArtistCommand implements Command {

    private static final String ARTIST_NAME_REQUEST_PARAM_NAME = "artistName";
    private static final String ADMIN_PAGE = "page.admin";
    private static final String ADD_ARTIST_PAGE = "page.add_artist";

    private static final String ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE = "errorIncorrectArtistNameMessage";
    private static final String ERROR_INCORRECT_ARTIST_NAME_MESSAGE = "Incorrect artist name";

    private static final String ERROR_ARTIST_EXISTS_ATTRIBUTE = "errorArtistExistsMessage";
    private static final String ARTIST_ALREADY_EXISTS_MESSAGE = "The artist already exists";

    private static AddArtistCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final ArtistService artistService;

    public AddArtistCommand(RequestFactory requestFactory, PropertyContext propertyContext, ArtistService artistService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.artistService = artistService;
    }

    public static AddArtistCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddArtistCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().artistService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String name = request.getParameter(ARTIST_NAME_REQUEST_PARAM_NAME);
        if (ArtistValidator.getInstance().isNameInvalid(name)) {
            request.addAttributeToJSP(ERROR_INCORRECT_ARTIST_NAME_ATTRIBUTE, ERROR_INCORRECT_ARTIST_NAME_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_ARTIST_PAGE));
        }
        if (artistService.findByName(name).isPresent()) {
            request.addAttributeToJSP(ERROR_ARTIST_EXISTS_ATTRIBUTE, ARTIST_ALREADY_EXISTS_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_ARTIST_PAGE));
        }
        artistService.create(new Artist(name));
        return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
    }
}
