package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class DeleteTrackFromShoppingCartCommand implements Command {

    private static final String TRACKS_PAGE = "page.tracks";
    private static final String SHOPPING_CART_PAGE = "page.shopping_cart";

    private static final String TRACK_ID_REQUEST_PARAM_NAME = "trackId";
    private static final String SHOPPING_CART_REQUEST_PARAM_NAME = "shoppingCart";
    private static final String TOTAL_PRICE_ATTRIBUTE_NAME = "totalPrice";

    private static final String ERROR_SHOPPING_CART_DOES_NOT_EXIST_ATTRIBUTE = "errorShoppingCartDoesNotExistMessage";
    private static final String SHOPPING_CART_DOES_NOT_EXIST_MESSAGE = "You have an empty shopping cart, please add" +
            " a track to the cart";


    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private static DeleteTrackFromShoppingCartCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public DeleteTrackFromShoppingCartCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static DeleteTrackFromShoppingCartCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new DeleteTrackFromShoppingCartCommand(RequestFactory.getInstance(),
                            PropertyContext.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandResponse execute(CommandRequest request) {

        final Optional<Object> shoppingCartFromSession = request.retrieveFromSession(SHOPPING_CART_REQUEST_PARAM_NAME);

        if (!request.sessionExists() || !shoppingCartFromSession.isPresent()) {
            request.addAttributeToJSP(ERROR_SHOPPING_CART_DOES_NOT_EXIST_ATTRIBUTE,
                    SHOPPING_CART_DOES_NOT_EXIST_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(TRACKS_PAGE));
        }

        final List<Track> shoppingCart = (ArrayList<Track>) shoppingCartFromSession.get();

        final String trackIdFromRequest = request.getParameter(TRACK_ID_REQUEST_PARAM_NAME);
        final Long trackId = Long.parseLong(trackIdFromRequest);

        shoppingCart.removeIf(st -> st.getId().equals(trackId));

        BigDecimal sum = shoppingCart
                .stream()
                .map(Track::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.addToSession(TOTAL_PRICE_ATTRIBUTE_NAME, sum);
        request.addToSession(SHOPPING_CART_REQUEST_PARAM_NAME, shoppingCart);
        return requestFactory.createForwardResponse(propertyContext.get(SHOPPING_CART_PAGE));
    }
}
