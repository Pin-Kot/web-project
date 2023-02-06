package com.epam.jwd.audiotrack_ordering.validator;

import java.util.concurrent.locks.ReentrantLock;

public class ArtistValidator {

    private static ArtistValidator instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static ArtistValidator getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ArtistValidator();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private ArtistValidator() {
    }

    public boolean isNameValid(String name) {
        if (name == null || name.isEmpty() || name.length() > 250) {
            return false;
        }
        return !Character.isWhitespace(name.charAt(0));
    }
}
