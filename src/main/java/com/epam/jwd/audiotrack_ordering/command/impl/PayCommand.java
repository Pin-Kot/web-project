package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Card;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.CardService;
import com.epam.jwd.audiotrack_ordering.service.OrderService;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class PayCommand implements Command {

    private static final String MAIN_PAGE = "page.main";
    private static final String TRACKS_PAGE = "page.tracks";
    private static final String CHECKOUT_PAGE = "page.checkout";
    private static final String INDEX_PAGE = "page.index";

    private static final String SHOPPING_CART_REQUEST_PARAM_NAME = "shoppingCart";
    private static final String ACCOUNT_REQUEST_PARAM_NAME = "account";
    private static final String CARD_NUMBER_REQUEST_PARAM_NAME = "cardNumber";
    private static final String ORDER_VALUE_REQUEST_PARAM_NAME = "payment";
    private static final String TOTAL_PRICE_ATTRIBUTE_NAME = "totalPrice";

    private static final String ADMIN_CARD_NUMBER = "4463000000013";

    private static final String ERROR_SHOPPING_CART_DOES_NOT_EXIST_ATTRIBUTE = "errorShoppingCartDoesNotExistMessage";
    private static final String SHOPPING_CART_DOES_NOT_EXIST_MESSAGE = "You have an empty shopping cart, please add" +
            " a track to the cart";

    private static final String ERROR_ACCOUNT_ARE_NOT_AUTHORIZED_ATTRIBUTE = "errorAccountDoesNotExistMessage";
    private static final String ACCOUNT_ARE_NOT_AUTHORIZED_MESSAGE = "You aren't authorized, please log in";

    private static final String ERROR_INVALID_USER_DATA_ATTRIBUTE = "errorInvalidUserData";
    private static final String ERROR_INVALID_USER_DATA_MESSAGE = "User data is not defined, please enter data";

    private static final String ERROR_CARD_NOT_FOUND_ATTRIBUTE = "errorCardNotFoundMessage";
    private static final String CARD_NOT_FOUND_MESSAGE = "Card not found";

    private static final String ERROR_CANNOT_DO_PAYMENT_ATTRIBUTE = "cannotDoPaymentMessage";
    private static final String CANNOT_DO_PAYMENT_MESSAGE = "You can't do payment, please contact the administration";

    private static final String ERROR_AMOUNT_MONEY_ATTRIBUTE = "errorAmountMoneyMessage";
    private static final String ERROR_AMOUNT_MONEY_MESSAGE = "Not enough money";

    private static final String STATUS_ORDER = "COMPLETED";

    private static PayCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final UserService userService;
    private final OrderService orderService;
    private final CardService cardService;

    public PayCommand(RequestFactory requestFactory, PropertyContext propertyContext, UserService userService,
                      OrderService orderService, CardService cardService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = userService;
        this.orderService = orderService;
        this.cardService = cardService;
    }

    public static PayCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new PayCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            ServiceFactory.getInstance().userService(), ServiceFactory.getInstance()
                            .orderService(), ServiceFactory.getInstance().cardService());
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

        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_REQUEST_PARAM_NAME);
        if (!accountFromSession.isPresent()) {
            request.addAttributeToJSP(ERROR_ACCOUNT_ARE_NOT_AUTHORIZED_ATTRIBUTE, ACCOUNT_ARE_NOT_AUTHORIZED_MESSAGE);
            return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
        }
        final Account account = (Account) accountFromSession.get();

        if (!userService.findUserByAccountId(account.getId()).isPresent()) {
            request.addAttributeToJSP(ERROR_INVALID_USER_DATA_ATTRIBUTE, ERROR_INVALID_USER_DATA_MESSAGE);
            return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
        }
        final User user = userService.findUserByAccountId(account.getId()).get();

        final LocalDate date = LocalDate.now();
        final String valueFromRequest = request.getParameter(ORDER_VALUE_REQUEST_PARAM_NAME);
        final BigDecimal totalValue = new BigDecimal(valueFromRequest);

        final String cardNumber = request.getParameter(CARD_NUMBER_REQUEST_PARAM_NAME);
        final Optional<Card> cardBuyerFromSession = cardService.findCardByNumber(cardNumber);
        if (!cardBuyerFromSession.isPresent()) {
            request.addAttributeToJSP(ERROR_CARD_NOT_FOUND_ATTRIBUTE, CARD_NOT_FOUND_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(CHECKOUT_PAGE));
        }

        final Optional<Card> cardAdmin = cardService.findCardByNumber(ADMIN_CARD_NUMBER);
        if (!cardAdmin.isPresent()) {
            request.addAttributeToJSP(ERROR_CANNOT_DO_PAYMENT_ATTRIBUTE, CANNOT_DO_PAYMENT_MESSAGE);
            return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
        }

        if (cardService.transferMoney(cardBuyerFromSession.get(), cardAdmin.get(), totalValue)) {
            orderService.createOrder(new Order(date, user.getId(), Order.typeOf(STATUS_ORDER),
                    totalValue), shoppingCart);
            request.removeFromSession(SHOPPING_CART_REQUEST_PARAM_NAME);
            request.removeFromSession(TOTAL_PRICE_ATTRIBUTE_NAME);
            return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
        }
        request.addAttributeToJSP(ERROR_AMOUNT_MONEY_ATTRIBUTE, ERROR_AMOUNT_MONEY_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(CHECKOUT_PAGE));
    }
}
