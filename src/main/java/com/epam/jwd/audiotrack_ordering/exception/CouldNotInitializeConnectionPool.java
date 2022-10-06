package com.epam.jwd.audiotrack_ordering.exception;

public class CouldNotInitializeConnectionPool extends Error {

    private static final long serialVersionUID = 1906897689556732595L;

    public CouldNotInitializeConnectionPool(String message, Throwable cause) {
        super(message, cause);
    }
}
