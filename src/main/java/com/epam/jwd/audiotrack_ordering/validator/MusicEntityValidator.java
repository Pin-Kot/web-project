package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class MusicEntityValidator {

    private static final String YEAR_REGEX = "\\d{4}";
    private static final String TYPE_REGEX = "[A-Z]{4,20}";

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

    public boolean isYearValid(int year) {
        return year >= 1900 && year <= 2023;
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
}
