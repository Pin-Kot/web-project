package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.service.AlbumService;
import com.epam.jwd.audiotrack_ordering.service.ImageService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.validator.MusicEntityValidator;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddImageCommand implements Command {

    private static final String ALBUM_TITLE_REQUEST_PARAM_NAME = "albumTitle";
    private static final String ALBUM_YEAR_REQUEST_PARAM_NAME = "albumYear";
    private static final String ALBUM_TYPE_REQUEST_PARAM_NAME = "albumType";
    private static final String IMAGE_REQUEST_PARAM_NAME = "image";

    private static final String ERROR_INCORRECT_YEAR_FORMAT_ATTRIBUTE = "errorIncorrectYearFormatMessage";
    private static final String ERROR_INCORRECT_YEAR_FORMAT_MESSAGE = "Incorrect format of year";

    private static final String ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE = "errorIncorrectAlbumDataMessage";
    private static final String ERROR_INCORRECT_ALBUM_DATA_MESSAGE = "Incorrect entered album's data";

    private static final String ERROR_ALBUM_DOES_NOT_EXIST_ATTRIBUTE = "errorAlbumDoesNotExistMessage";
    private static final String ALBUM_DOES_NOT_EXIST_MESSAGE = "Album doesn't exist";

    private static final String ADD_IMAGE_PAGE = "page.add_image";
    private static final String ADMIN_PAGE = "page.admin";

    private static AddImageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final AlbumService albumService;
    private final ImageService imageService;

    public AddImageCommand(RequestFactory requestFactory, PropertyContext propertyContext, AlbumService albumService, ImageService imageService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.albumService = albumService;
        this.imageService = imageService;
    }

    public static AddImageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddImageCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().albumService(), ServiceFactory.getInstance().imageService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String albumTitle = request.getParameter(ALBUM_TITLE_REQUEST_PARAM_NAME);
        final String albumYearFromRequest = request.getParameter(ALBUM_YEAR_REQUEST_PARAM_NAME);
        final String albumType = request.getParameter(ALBUM_TYPE_REQUEST_PARAM_NAME);

        MusicEntityValidator validator = MusicEntityValidator.getInstance();

        if (!validator.isNumeric(albumYearFromRequest)) {
            request.addAttributeToJSP(ERROR_INCORRECT_YEAR_FORMAT_ATTRIBUTE, ERROR_INCORRECT_YEAR_FORMAT_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_IMAGE_PAGE));
        }
        int albumYear = Integer.parseInt(albumYearFromRequest);

        if (!validator.isAlbumDataValid(albumTitle, albumYear, albumType)) {
            request.addAttributeToJSP(ERROR_INCORRECT_ALBUM_DATA_ATTRIBUTE, ERROR_INCORRECT_ALBUM_DATA_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_IMAGE_PAGE));
        }
        final Optional<Album> albumFromRequest = albumService.findByTitleByYear(albumTitle, albumYear);

        if (!albumFromRequest.isPresent()) {
            request.addAttributeToJSP(ERROR_ALBUM_DOES_NOT_EXIST_ATTRIBUTE, ALBUM_DOES_NOT_EXIST_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_IMAGE_PAGE));
        }

        try {
            final InputStream image = request.getPart(IMAGE_REQUEST_PARAM_NAME).getInputStream();
            imageService.createImage(albumFromRequest.get(), image);
            return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return requestFactory.createForwardResponse(propertyContext.get(ADD_IMAGE_PAGE));
    }
}
