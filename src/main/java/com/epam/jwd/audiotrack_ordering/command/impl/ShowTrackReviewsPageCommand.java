package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import com.epam.jwd.audiotrack_ordering.service.ReviewService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ShowTrackReviewsPageCommand implements Command {

    private static final String JSP_TRACK_REVIEWS_ATTRIBUTE_NAME = "reviews";
    private static final String REVIEWS_PAGE = "page.reviews";
    private static final String TRACK_ID_REQUEST_PARAM_NAME = "trackId";

    private static ShowTrackReviewsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final ReviewService service;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowTrackReviewsPageCommand(ReviewService service, RequestFactory requestFactory,
                                       PropertyContext propertyContext) {
        this.service = service;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowTrackReviewsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowTrackReviewsPageCommand(ServiceFactory.getInstance().reviewService(),
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
        final String trackIdFromRequest = request.getParameter(TRACK_ID_REQUEST_PARAM_NAME);
        final Long trackId = Long.parseLong(trackIdFromRequest);
        final List<Review> trackReviews = service.findReviewsByTrackId(trackId);
        request.addAttributeToJSP(JSP_TRACK_REVIEWS_ATTRIBUTE_NAME, trackReviews);
        return requestFactory.createForwardResponse(propertyContext.get(REVIEWS_PAGE));
    }
}
