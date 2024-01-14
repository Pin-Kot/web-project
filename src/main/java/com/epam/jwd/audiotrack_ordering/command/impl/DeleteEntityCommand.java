package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.service.AlbumService;
import com.epam.jwd.audiotrack_ordering.service.ArtistService;
import com.epam.jwd.audiotrack_ordering.service.ImageService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;
import com.epam.jwd.audiotrack_ordering.validator.EnteredDataValidator;

import java.util.concurrent.locks.ReentrantLock;

public class DeleteEntityCommand implements Command {

    private static final String DELETE_PAGE = "page.delete";
    private static final String ADMIN_PAGE = "page.admin";

    private static final String ENTITY_REQUEST_PARAM_NAME = "element";
    private static final String ID_REQUEST_PARAM_NAME = "id";

    private static final String ERROR_ENTITY_NOT_FOUND_ATTRIBUTE = "errorEntityNotFoundMessage";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";

    private static final String ERROR_WRONG_ENTITY_NAME_ATTRIBUTE = "errorWrongEntityNameFoundMessage";
    private static final String WRONG_ENTITY_NAME_MESSAGE = "Entity not found";

    private static final String ERROR_ENTERED_ID_ATTRIBUTE = "errorEnteredIdMessage";
    private static final String WRONG_ID_MESSAGE = "Incorrect user id";

    private static DeleteEntityCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final ImageService imageService;
    private final TrackService trackService;

    public DeleteEntityCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                               AlbumService albumService, ArtistService artistService, ImageService imageService,
                               TrackService trackService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.albumService = albumService;
        this.artistService = artistService;
        this.imageService = imageService;
        this.trackService = trackService;
    }

    public static DeleteEntityCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new DeleteEntityCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().albumService(), ServiceFactory.getInstance().artistService(),
                            ServiceFactory.getInstance().imageService(), ServiceFactory.getInstance().trackService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private boolean isEntityDeleted(CommandRequest request) {
        final String entityFromRequest = request.getParameter(ENTITY_REQUEST_PARAM_NAME);
        final String idFromRequest = request.getParameter(ID_REQUEST_PARAM_NAME);
        if (!EnteredDataValidator.getInstance().isLongNumberValid(idFromRequest)){
            request.addAttributeToJSP(ERROR_ENTERED_ID_ATTRIBUTE, WRONG_ID_MESSAGE);
            return false;
        }
        final Long id = Long.parseLong(idFromRequest);
        switch (entityFromRequest) {
            case "album":
               return albumService.delete(id);
            case "artist":
                return artistService.delete(id);
            case "image":
                return imageService.delete(id);
            case "track":
                return trackService.delete(id);
            default:
                request.addAttributeToJSP(ERROR_WRONG_ENTITY_NAME_ATTRIBUTE, WRONG_ENTITY_NAME_MESSAGE);
                return false;
        }
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
       if (isEntityDeleted(request)) {
           return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
       }
        request.addAttributeToJSP(ERROR_ENTITY_NOT_FOUND_ATTRIBUTE, ENTITY_NOT_FOUND_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(DELETE_PAGE));
    }

}
