package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.service.OrderService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.TrackService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ShowOrderTracksCommand implements Command {

    private static final String JSP_ORDER_TRACKS_ATTRIBUTE_NAME = "tracks";
    private static final String ORDER_PAGE = "page.order";
    private static final String ORDER_ID_REQUEST_PARAM_NAME = "orderId";
    private static final String JSP_ORDER_ATTRIBUTE_NAME = "order";

    private static ShowOrderTracksCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final TrackService trackService;
    private final OrderService orderService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    public ShowOrderTracksCommand(TrackService trackService, OrderService orderService, RequestFactory requestFactory,
                                  PropertyContext propertyContext) {
        this.trackService = trackService;
        this.orderService = orderService;
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowOrderTracksCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ShowOrderTracksCommand(ServiceFactory.getInstance().trackService(),
                            ServiceFactory.getInstance().orderService(), RequestFactory.getInstance(),
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
        final String orderIdFromRequest = request.getParameter(ORDER_ID_REQUEST_PARAM_NAME);
        final Long orderId = Long.parseLong(orderIdFromRequest);
        final Optional<Order> orderFromRequest = orderService.find(orderId);
        final List<Track> orderTracks = trackService.findTracksByOrderId(orderId);
        request.addAttributeToJSP(JSP_ORDER_TRACKS_ATTRIBUTE_NAME, orderTracks);
        request.addAttributeToJSP(JSP_ORDER_ATTRIBUTE_NAME, orderFromRequest.get());
        return requestFactory.createForwardResponse(propertyContext.get(ORDER_PAGE));
    }
}
