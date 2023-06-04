package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class MusicEntityValidator {

    private static final String YEAR_REGEX = "\\d{4}";
    private static final String TYPE_REGEX = "[A-Z]{4,20}";
    private static final String PRICE_REGEX = "^\\d{0,8}(\\.\\d{1,2})?$";
    private static final String TRACK_YEAR_REGEX = "(?:(?:18|19)[0-9]{2})|(?:(?:200|201)[0-9]{1})|(?:(?:202)[0-3]{1})";

    private static MusicEntityValidator instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static MusicEntityValidator getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new MusicEntityValidator();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private MusicEntityValidator() {
    }

    public boolean isTitleValid(String title) {
        if (title == null || title.isEmpty() || title.length() > 250) {
            return false;
        }
        return !Character.isWhitespace(title.charAt(0));
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        return Pattern.compile(YEAR_REGEX).matcher(strNum).matches();
    }

    public boolean isDecimal(String strPrice) {
        if (strPrice == null || strPrice.isEmpty()) {
            return false;
        }
        return Pattern.compile(PRICE_REGEX).matcher(strPrice).matches();
    }

    public boolean isYearValid(int year) {
        return year >= 1900 && year <= 2023;
    }

    public boolean isTrackYearValid(String strYear) {
        if (strYear == null || strYear.isEmpty()) {
            return false;
        }
        return Pattern.compile(TRACK_YEAR_REGEX).matcher(strYear).matches();
    }

    public boolean isTypeValid(String type) {
        if (type == null || type.isEmpty() || type.length() > 20) {
            return false;
        }
        return Pattern.compile(TYPE_REGEX).matcher(type).matches();
    }

    public boolean isTitleAndYearValid(String title, int year) {
        return isTitleValid(title) && isYearValid(year);
    }

    public boolean isTrackTitleAndYearValid(String title, String strYear) {
        return isTitleValid(title) && isTrackYearValid(strYear);
    }

    public boolean isAlbumDataValid(String title, int year, String type) {
        return isTitleAndYearValid(title, year) && isTypeValid(type);
    }

    public boolean isAlbumDataValid(String title, String strYear, String type) {
        if (isNumeric(strYear)){
            final int year = Integer.parseInt(strYear);
            return isTitleAndYearValid(title, year) && isTypeValid(type);
        }
        return false;
    }

    public boolean isTrackDataValid(String title, String strYear, String strPrice) {
        return isTitleValid(title) && isTrackYearValid(strYear) && isDecimal(strPrice);
    }

    public boolean isTrackDataValid(String title, int year, String strPrice) {
        return isTitleAndYearValid(title, year) && isDecimal(strPrice);
    }
}
