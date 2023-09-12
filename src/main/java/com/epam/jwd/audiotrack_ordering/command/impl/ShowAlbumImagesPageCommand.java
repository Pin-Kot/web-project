package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Image;
import com.epam.jwd.audiotrack_ordering.service.ImageService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowAlbumImagesPageCommand implements Command {

    private static final String ALBUM_TITLE_REQUEST_PARAM_NAME = "albumTitle";
    private static final String JSP_ALBUM_IMAGES_ATTRIBUTE_NAME = "images";
    private static final String IMAGES_PAGE = "page.images";

    private static ShowAlbumImagesPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final ImageService service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowAlbumImagesPageCommand(ImageService service, RequestFactory requestFactory, PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowAlbumImagesPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowAlbumImagesPageCommand(ServiceFactory.getInstance().imageService(),
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
        final String albumTitle = request.getParameter(ALBUM_TITLE_REQUEST_PARAM_NAME);
        final List<Image> albumImages = service.findImagesByAlbum(albumTitle);
        request.addAttributeToJSP(JSP_ALBUM_IMAGES_ATTRIBUTE_NAME, albumImages);
        return requestFactory.createForwardResponse(propertyContext.get(IMAGES_PAGE));
    }
}
