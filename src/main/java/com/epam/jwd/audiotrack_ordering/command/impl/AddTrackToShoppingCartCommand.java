package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddTrackToShoppingCartCommand implements Command {

    private static final String TRACKS_PAGE = "page.tracks";
    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String TRACK_ID_REQUEST_PARAM_NAME = "trackId";
    private static final String SHOPPING_CART_ATTRIBUTE_NAME = "shoppingCart";
    private static final String TOTAL_PRICE_ATTRIBUTE_NAME = "totalPrice";

    private static final String ERROR_ACCOUNT_DOES_NOT_LOGGED_ATTRIBUTE = "errorAccountDoesNotLoggedMessage";
    private static final String ACCOUNT_DOES_NOT_LOGGED_MESSAGE = "You have no access, please log in";

    private static final String ERROR_ID_TRACK_ATTRIBUTE = "errorIdTrackMessage";
    private static final String ERROR_ID_TRACK_MESSAGE = "Incorrect track id";

    private static AddTrackToShoppingCartCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final TrackService trackService;

    public static AddTrackToShoppingCartCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddTrackToShoppingCartCommand(RequestFactory.getInstance(),
                            PropertyContext.getInstance(), ServiceFactory.getInstance().trackService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public AddTrackToShoppingCartCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                                         TrackService trackService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.trackService = trackService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME);

        if (!request.sessionExists() || !accountFromSession.isPresent()) {
            request.addAttributeToJSP(ERROR_ACCOUNT_DOES_NOT_LOGGED_ATTRIBUTE, ACCOUNT_DOES_NOT_LOGGED_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
        }
        final String trackIdFromRequest = request.getParameter(TRACK_ID_REQUEST_PARAM_NAME);
        final Long trackId = Long.parseLong(trackIdFromRequest);
        final Optional<Track> trackFromRequest = trackService.find(trackId);

        if (!trackFromRequest.isPresent()) {
            request.addAttributeToJSP(ERROR_ID_TRACK_ATTRIBUTE, ERROR_ID_TRACK_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
        }
        final Track track = trackFromRequest.get();
        final List<Track> shoppingCart;

        if (request.retrieveFromSession(SHOPPING_CART_ATTRIBUTE_NAME).isPresent()) {
            shoppingCart = (ArrayList<Track>) request.retrieveFromSession(SHOPPING_CART_ATTRIBUTE_NAME).get();
        } else {
            shoppingCart = new ArrayList<>();
        }

        if (shoppingCart.stream()
                .noneMatch(st->st.getId().equals(track.getId()))) {
            shoppingCart.add(track);
        }

        BigDecimal sum = shoppingCart
                .stream()
                .map(st->st.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.addToSession(TOTAL_PRICE_ATTRIBUTE_NAME, sum);
        request.addToSession(SHOPPING_CART_ATTRIBUTE_NAME, shoppingCart);
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
