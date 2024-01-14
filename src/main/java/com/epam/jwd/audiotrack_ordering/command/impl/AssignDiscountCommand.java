package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.UserService;
import com.epam.jwd.audiotrack_ordering.validator.EnteredDataValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AssignDiscountCommand implements Command {

    private static final String ADMIN_PAGE = "page.admin";
    private static final String ASSIGN_DISCOUNT_PAGE = "page.assign_discount";

    private static final String USER_ID_REQUEST_PARAM_NAME = "userId";
    private static final String DISCOUNT_REQUEST_PARAM_NAME = "discount";

    private static final String ERROR_WRONG_DISCOUNT_ATTRIBUTE = "errorEnteredDiscountMessage";
    private static final String WRONG_DISCOUNT_MESSAGE = "Incorrect discount amount";

    private static final String ERROR_ENTERED_ID_ATTRIBUTE = "errorEnteredIdMessage";
    private static final String WRONG_ID_MESSAGE = "Incorrect user id";

    private static AssignDiscountCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final UserService userService;

    public AssignDiscountCommand(RequestFactory requestFactory, PropertyContext propertyContext,
                                 UserService userService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = userService;
    }

    public static AssignDiscountCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AssignDiscountCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().userService());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String idFromRequest = request.getParameter(USER_ID_REQUEST_PARAM_NAME);
        final String discountFromRequest = request.getParameter(DISCOUNT_REQUEST_PARAM_NAME);
        final  EnteredDataValidator validator = EnteredDataValidator.getInstance();
        if (validator.isLongNumberValid(idFromRequest)) {
            final Long id = Long.parseLong(idFromRequest);
            if(validator.isDiscountValid(discountFromRequest)) {
                final Optional<User> userFromRequest = userService.find(id);
                if (userFromRequest.isPresent()) {
                    final User user = userFromRequest.get();
                    userService.update(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                            user.getBirthday(), new BigDecimal(discountFromRequest), user.getAccId()));
                    return requestFactory.createForwardResponse(propertyContext.get(ADMIN_PAGE));
                }
            }
            request.addAttributeToJSP(ERROR_WRONG_DISCOUNT_ATTRIBUTE, WRONG_DISCOUNT_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ASSIGN_DISCOUNT_PAGE));
        }
        request.addAttributeToJSP(ERROR_ENTERED_ID_ATTRIBUTE, WRONG_ID_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(ASSIGN_DISCOUNT_PAGE));
    }
}
