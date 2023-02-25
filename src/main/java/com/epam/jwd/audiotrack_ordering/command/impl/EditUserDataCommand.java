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

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class EditUserDataCommand implements Command {

    private static final String MAIN_PAGE = "page.main";
    private static final String EDIT_DATA_PAGE = "page.edit_data";
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

    private static final String ERROR_USER_DOES_NOT_EXIST_ATTRIBUTE = "errorUserDoesNotExistMessage";
    private static final String USER_DOES_NOT_EXIST_MESSAGE = "User doesn't exist";

    private static final String ERROR_ENTERED_INCORRECT_DATE_OF_BIRTHDAY_ATTRIBUTE = "errorEnteredDateOfBirthdayMessage";
    private static final String ERROR_ENTERED_INCORRECT_DATE_OF_BIRTH_MESSAGE = "Entered incorrect date of birth";

    private static final String ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE = "errorEditPersonalDataMessage";
    private static final String ERROR_EDIT_PERSONAL_DATA_MESSAGE = "Incorrect personal data";

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
            final Optional<User> userFromSession = userService.findUserByAccountId(account.getId());

            if (userFromSession.isPresent()) {

                final User user = userFromSession.get();
                final String firstName = request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME);
                final String lastName = request.getParameter(LAST_NAME_REQUEST_PARAM_NAME);
                final String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
                final String dayFromRequest = request.getParameter(DAY_REQUEST_PARAM_NAME);
                final String monthFromRequest = request.getParameter(MONTH_REQUEST_PARAM_NAME);
                final String yearFromRequest = request.getParameter(YEAR_REQUEST_PARAM_NAME);
                final UserValidator validator = UserValidator.getInstance();

                return updateUserByEnteredData(request, user, firstName, lastName, email, dayFromRequest,
                        monthFromRequest, yearFromRequest, validator);
            }
            request.addAttributeToJSP(ERROR_USER_DOES_NOT_EXIST_ATTRIBUTE, USER_DOES_NOT_EXIST_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(EDIT_DATA_PAGE));
        }
        request.addAttributeToJSP(ERROR_ACCOUNT_DOES_NOT_EXIST_ATTRIBUTE, ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        return requestFactory.createForwardResponse(propertyContext.get(INDEX_PAGE));
    }

    private CommandResponse updateUserByEnteredData(CommandRequest request, User user, String firstName,
                                                    String lastName, String email, String dayFromRequest,
                                                    String monthFromRequest, String yearFromRequest,
                                                    UserValidator validator) {
        if (isEnteredParametersNumeric(dayFromRequest, monthFromRequest, yearFromRequest, validator)) {
            final int day = Integer.parseInt(dayFromRequest);
            final int month = Integer.parseInt(monthFromRequest);
            final int year = Integer.parseInt(yearFromRequest);
            if (validator.isAllValid(firstName, lastName, email, year, month, day)) {
                userService.update(new User(user.getId(), firstName, lastName, email,
                        LocalDate.of(year, month, day), user.getDiscount(), user.getAccId()));
                return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
            } else {
                request.addAttributeToJSP(ERROR_EDIT_PERSONAL_DATA_ATTRIBUTE, ERROR_EDIT_PERSONAL_DATA_MESSAGE);
                return requestFactory.createForwardResponse(propertyContext.get(EDIT_DATA_PAGE));
            }
        } else {
            request.addAttributeToJSP(ERROR_ENTERED_INCORRECT_DATE_OF_BIRTHDAY_ATTRIBUTE,
                    ERROR_ENTERED_INCORRECT_DATE_OF_BIRTH_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(EDIT_DATA_PAGE));
        }
    }

    private boolean isEnteredParametersNumeric(String dayFromRequest, String monthFromRequest, String yearFromRequest,
                                               UserValidator validator) {
        return validator.isNumeric(dayFromRequest) && validator.isNumeric(monthFromRequest)
                && validator.isNumeric(yearFromRequest);
    }
}
