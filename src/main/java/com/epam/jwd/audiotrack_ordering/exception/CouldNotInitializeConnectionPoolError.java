package com.epam.jwd.audiotrack_ordering.exception;

public class CouldNotInitializeConnectionPoolError extends Error {

    private static final long serialVersionUID = 1906897689556732595L;

    public CouldNotInitializeConnectionPoolError(String message, Throwable cause) {
        super(message, cause);
    }
}
