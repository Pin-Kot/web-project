package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.ReviewService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.validator.EnteredDataValidator;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddReviewCommand implements Command {

    private static final String MAIN_PAGE = "page.main";
    private static final String REVIEWS_PAGE = "page.reviews";

    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String TRACK_ATTRIBUTE_NAME = "track";
    private static final String TEXT_REQUEST_PARAM_NAME = "text";

    private static final String ACCOUNT_ARE_NOT_AUTHORIZED_MESSAGE = "You aren't authorized, please log in";

    private static final String TRACK_DOES_NOT_EXIST_MESSAGE = "Track  doesn't exist";

    private static final String ERROR_TEXT_IS_INVALID_ATTRIBUTE = "errorTextIsInvalidMessage";
    private static final String TEXT_IS_INVALID_MESSAGE = "You didn't enter a valid text, please try again";


    private static AddReviewCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final ReviewService reviewService;

    public AddReviewCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                            ReviewService reviewService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.reviewService = reviewService;
    }

    public static AddReviewCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddReviewCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().reviewService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME);
        if (request.sessionExists() && accountFromSession.isPresent()) {
            final Account account = (Account) accountFromSession.get();
            final Optional<Object> trackFromSession = request.retrieveFromSession(TRACK_ATTRIBUTE_NAME);
            if (trackFromSession.isPresent()) {
                final Track track = (Track) trackFromSession.get();
                final LocalDate date = LocalDate.now();
                final String text = request.getParameter(TEXT_REQUEST_PARAM_NAME);
                if (EnteredDataValidator.getInstance().isTextReviewValid(text)) {
                    reviewService.create(new Review(date, text, account.getLogin(), track.getId()));
                    return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
                }
                request.addAttributeToJSP(ERROR_TEXT_IS_INVALID_ATTRIBUTE, TEXT_IS_INVALID_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(REVIEWS_PAGE));
            }
            throw new IllegalArgumentException(TRACK_DOES_NOT_EXIST_MESSAGE);
        }
        throw new IllegalArgumentException(ACCOUNT_ARE_NOT_AUTHORIZED_MESSAGE);
    }
}
