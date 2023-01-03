package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String FIRST_NAME_REGEX = "\\w{2,150}";
    private static final String LAST_NAME_REGEX = "\\w{2,150}";
    private static final String EMAIL_REGEX = "(\\w{6,154})@(\\w{1,90}\\.)([a-z]{2,4})";

    private static UserValidator instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static UserValidator getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new UserValidator();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private UserValidator() {
    }

    public boolean isFirstNameValid(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return false;
        }
        return Pattern.compile(FIRST_NAME_REGEX).matcher(firstName).matches();
    }

    public boolean isLastNameValid(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return false;
        }
        return Pattern.compile(LAST_NAME_REGEX).matcher(lastName).matches();
    }

    public boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        }
        if (year % 100 == 0) {
            return false;
        }
        return year % 4 == 0;
    }

    public boolean isYearValid(int year) {
        return year >= 1900 && year <= 2022;
    }

    public boolean isMonthValid(int month) {
        return month >= 1 && month <= 12;
    }

    public boolean isDayValid(int year, int month, int day) {
        if (month == 2) {
            return (isLeapYear(year) && (day >= 1 && day <= 29))
                    || (!isLeapYear(year) && (day >= 1 && day <= 28));
        }
        if ((month < 8 && month % 2 != 0) || (month >= 8 && month % 2 == 0)) {
            return day >= 1 && day <= 31;
        } else {
            return day >= 1 && day <= 30;
        }
    }

    public boolean isDateValid(int year, int month, int day) {
        return isYearValid(year) && isMonthValid(month) && isDayValid(year, month, day);
    }

    public boolean isAllValid(String firstName, String lastName, String email, int year, int month, int day) {
        return isFirstNameValid(firstName) && isLastNameValid(lastName) && isEmailValid(email)
                && isDateValid(year, month, day);
    }
}
