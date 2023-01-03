package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.UserService;
import com.epam.jwd.audiotrack_ordering.validator.UserValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantLock;

public class EditUserDataCommand implements Command {

    private static final String MAIN_PAGE = "page.main";
    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String FIRST_NAME_REQUEST_PARAM_NAME = "firstName";
    private static final String LAST_NAME_REQUEST_PARAM_NAME = "lastName";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String DAY_REQUEST_PARAM_NAME = "day";
    private static final String MONTH_REQUEST_PARAM_NAME = "month";
    private static final String YEAR_REQUEST_PARAM_NAME = "year";

    private static final String ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE = "errorEditPersonalDataMessage";
    private static final String ERROR_EDIT_PERSONAL_DATA_MESSAGE = "Incorrect personal data";

    private static final String ERROR_USER_DOES_NOT_EXIST_ATTRIBUTE = "errorUserDoesNotExistMessage";
    private static final String USER_DOES_NOT_EXIST_MESSAGE = "User doesn't exist";

    private static final String ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE = "errorAccountDoesNotExistMessage";
    private static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Account doesn't exist";


    private static EditUserDataCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final UserService userService;

    public EditUserDataCommand(RequestFactory requestFactory, PropertyContext propertyContext, UserService userService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = userService;
    }

    public static EditUserDataCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new EditUserDataCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
                            UserService.getInstance());
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME).isPresent()) {
            final Account account = (Account) request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME).get();
            if (userService.findUserByAccountId(account.getId()).isPresent()) {
                final User user = userService.findUserByAccountId(account.getId()).get();
                final Long userId = user.getId();
                final String firstName = request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME);
                final String lastName = request.getParameter(LAST_NAME_REQUEST_PARAM_NAME);
                final String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
                final int day = Integer.parseInt(request.getParameter(DAY_REQUEST_PARAM_NAME));
                final int month = Integer.parseInt(request.getParameter(MONTH_REQUEST_PARAM_NAME));
                final int year = Integer.parseInt(request.getParameter(YEAR_REQUEST_PARAM_NAME));
                final BigDecimal discount = user.getDiscount();
                final Long accId = user.getAccId();
                if (UserValidator.getInstance().isAllValid(firstName, lastName, email, year, month, day)) {
                    LocalDate birthday = LocalDate.of(year, month, day);
                    User updatedUser = new User(userId, firstName, lastName, email, birthday, discount, accId);
                    userService.updateUser(updatedUser);
                    return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
                } else {
                    request.addAttributeToJSP(ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE, ERROR_EDIT_PERSONAL_DATA_MESSAGE);
                }
            }
            request.addAttributeToJSP(ERROR_USER_DOES_NOT_EXIST_ATTRIBUTE, USER_DOES_NOT_EXIST_MESSAGE);
        }
        request.addAttributeToJSP(ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE, ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
