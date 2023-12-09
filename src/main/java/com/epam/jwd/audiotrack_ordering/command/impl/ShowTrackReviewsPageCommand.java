package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.ReviewService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ShowTrackReviewsPageCommand implements Command {

    private static final String REVIEWS_PAGE = "page.reviews";
    private static final String TRACKS_PAGE = "page.tracks";

    private static final String TRACK_SESSION_ATTRIBUTE_NAME = "track";
    private static final String JSP_TRACK_REVIEWS_ATTRIBUTE_NAME = "reviews";
    private static final String TRACK_ID_REQUEST_PARAM_NAME = "trackId";

    private static final String ERROR_ID_TRACK_ATTRIBUTE = "errorIdTrackMessage";
    private static final String ERROR_ID_TRACK_MESSAGE = "Incorrect track id";

    private static ShowTrackReviewsPageCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final ReviewService reviewService;
    private final TrackService trackService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowTrackReviewsPageCommand(ReviewService reviewService, TrackService trackService, RequestFactory requestFactory,
                                       PropertyContext propertyContext) {
        this.reviewService = reviewService;
        this.trackService = trackService;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowTrackReviewsPageCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowTrackReviewsPageCommand(ServiceFactory.getInstance().reviewService(),
                            ServiceFactory.getInstance().trackService(), RequestFactory.getInstance(),
                            PropertyContext.getInstance());
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
        final Optional<Track> track = trackService.find(trackId);
        if (!track.isPresent()) {
            request.addAttributeToJSP(ERROR_ID_TRACK_ATTRIBUTE, ERROR_ID_TRACK_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
        }
        request.addToSession(TRACK_SESSION_ATTRIBUTE_NAME, track.get());
        final List<Review> trackReviews = reviewService.findReviewsByTrackId(trackId);
        request.addAttributeToJSP(JSP_TRACK_REVIEWS_ATTRIBUTE_NAME, trackReviews);
        return requestFactory.createForwardResponse(propertyContext.get(REVIEWS_PAGE));
    }
}
