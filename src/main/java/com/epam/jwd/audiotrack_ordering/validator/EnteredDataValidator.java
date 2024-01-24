package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class EnteredDataValidator {

    private static final String TYPE_REGEX = "[A-Z]{4,20}";
    private static final String PRICE_REGEX = "^\\d{0,8}(\\.\\d{1,2})?$";
    private static final String DISCOUNT_REGEX = "^(0|1?\\d|20)$";
    private static final String LONG_REGEX = "^\\d+$";
    private static final String ENTERED_YEAR_REGEX = "(?:(?:18|19)[0-9]{2})|(?:(?:200|201)[0-9])|(?:(?:202)[0-3])";

    private static EnteredDataValidator instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static EnteredDataValidator getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new EnteredDataValidator();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private EnteredDataValidator() {
    }

    public boolean isTitleValid(String title) {
        if (title == null || title.isEmpty() || title.length() > 250) {
            return false;
        }
        return !Character.isWhitespace(title.charAt(0));
    }

    public boolean isDecimal(String strPrice) {
        if (strPrice == null || strPrice.isEmpty()) {
            return false;
        }
        return Pattern.compile(PRICE_REGEX).matcher(strPrice).matches();
    }

    public boolean isTrackYearValid(String strYear) {
        if (strYear == null || strYear.isEmpty()) {
            return false;
        }
        return Pattern.compile(ENTERED_YEAR_REGEX).matcher(strYear).matches();
    }

    public boolean isTypeValid(String type) {
        if (type == null || type.isEmpty() || type.length() > 20) {
            return false;
        }
        return Pattern.compile(TYPE_REGEX).matcher(type).matches();
    }

    public boolean isDiscountValid(String discount) {
        if (discount == null || discount.isEmpty()) {
            return false;
        }
        return Pattern.compile(DISCOUNT_REGEX).matcher(discount).matches();
    }

    public boolean isLongNumberValid(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        return Pattern.compile(LONG_REGEX).matcher(number).matches();
    }

    public boolean isAlbumDataValid(String title, String strYear, String type) {
        return isTitleValid(title) && isTrackYearValid(strYear) && isTypeValid(type);
    }

    public boolean isTrackDataValid(String title, String strYear, String strPrice) {
        return isTitleValid(title) && isTrackYearValid(strYear) && isDecimal(strPrice);
    }

    public boolean isTextReviewValid(String text) {
        if (text == null || text.isEmpty() || text.length() > 3000) {
            return false;
        }
        return !Character.isWhitespace(text.charAt(0));
    }
}
