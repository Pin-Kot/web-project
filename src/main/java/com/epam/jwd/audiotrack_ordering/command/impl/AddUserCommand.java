package com.epam.jwd.audiotrack_ordering.command.impl;

import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.controller.PropertyContext;
import com.epam.jwd.audiotrack_ordering.controller.RequestFactory;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.ServiceFactory;
import com.epam.jwd.audiotrack_ordering.service.UserService;
import com.epam.jwd.audiotrack_ordering.validator.UserValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class AddUserCommand implements Command {

    private static final String MAIN_PAGE = "page.main";
    private static final String ADD_USER_PAGE = "page.add_user";
    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    private static final String FIRST_NAME_REQUEST_PARAM_NAME = "firstName";
    private static final String LAST_NAME_REQUEST_PARAM_NAME = "lastName";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String DAY_REQUEST_PARAM_NAME = "day";
    private static final String MONTH_REQUEST_PARAM_NAME = "month";
    private static final String YEAR_REQUEST_PARAM_NAME = "year";

    private static final String ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE = "errorAccountDoesNotExistMessage";
    private static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Account doesn't exist";

    private static final String ERROR_USER_ALREADY_EXISTS_ATTRIBUTE = "errorUserAlreadyExistsMessage";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";

    private static final String ERROR_ENTERED_INCORRECT_DATE_OF_BIRTHDAY_ATTRIBUTE = "errorEnteredDateOfBirthdayMessage";
    private static final String ERROR_ENTERED_INCORRECT_DATE_OF_BIRTH_MESSAGE = "Entered incorrect date of birth";

    private static final String ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE = "errorEditPersonalDataMessage";
    private static final String ERROR_EDIT_PERSONAL_DATA_MESSAGE = "Incorrect personal data";

    private static AddUserCommand instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final UserService userService;

    public AddUserCommand(RequestFactory requestFactory, PropertyContext propertyContext, UserService userService) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = userService;
    }

    public static AddUserCommand getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new AddUserCommand(RequestFactory.getInstance(), PropertyContext.getInstance(),
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
        final Optional<Object> accountFromSession = request.retrieveFromSession(ACCOUNT_ATTRIBUTE_NAME);
        if (request.sessionExists() && accountFromSession.isPresent()) {
            final Account account = (Account) accountFromSession.get();
            if (!userService.findUserByAccountId(account.getId()).isPresent()) {
                final String firstName = request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME);
                final String lastName = request.getParameter(LAST_NAME_REQUEST_PARAM_NAME);
                final String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
                final String dayFromRequest = request.getParameter(DAY_REQUEST_PARAM_NAME);
                final String monthFromRequest = request.getParameter(MONTH_REQUEST_PARAM_NAME);
                final String yearFromRequest = request.getParameter(YEAR_REQUEST_PARAM_NAME);
                final UserValidator validator = UserValidator.getInstance();
                return createUserByEnteredData(request, account, firstName, lastName, email, dayFromRequest,
                        monthFromRequest, yearFromRequest, validator);
            }
            request.addAttributeToJSP(ERROR_USER_ALREADY_EXISTS_ATTRIBUTE, USER_ALREADY_EXISTS_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_USER_PAGE));
        }
        request.addAttributeToJSP(ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE, ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(INDEX_PAGE));
    }

    private CommandResponse createUserByEnteredData(CommandRequest request, Account account, String firstName,
                                                    String lastName, String email, String dayFromRequest,
                                                    String monthFromRequest, String yearFromRequest,
                                                    UserValidator validator) {
        if (isEnteredParametersNumeric(dayFromRequest, monthFromRequest, yearFromRequest, validator)) {
            final int day = Integer.parseInt(dayFromRequest);
            final int month = Integer.parseInt(monthFromRequest);
            final int year = Integer.parseInt(yearFromRequest);
            if (validator.isAllValid(firstName, lastName, email, year, month, day)) {
                userService.create(new User( firstName, lastName, email,
                        LocalDate.of(year, month, day), new BigDecimal(0), account.getId()));
                return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
            } else {
                request.addAttributeToJSP(ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE, ERROR_EDIT_PERSONAL_DATA_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(ADD_USER_PAGE));
            }
        } else {
            request.addAttributeToJSP(ERROR_ENTERED_INCORRECT_DATE_OF_BIRTHDAY_ATTRIBUTE,
                    ERROR_ENTERED_INCORRECT_DATE_OF_BIRTH_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(ADD_USER_PAGE));
        }
    }

    private boolean isEnteredParametersNumeric(String dayFromRequest, String monthFromRequest, String yearFromRequest,
                                               UserValidator validator) {
        return validator.isNumeric(dayFromRequest) && validator.isNumeric(monthFromRequest)
                && validator.isNumeric(yearFromRequest);
    }
}
