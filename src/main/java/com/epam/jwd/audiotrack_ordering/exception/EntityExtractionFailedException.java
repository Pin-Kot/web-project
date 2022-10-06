package com.epam.jwd.audiotrack_ordering.exception;

public class EntityExtractionFailedException extends Exception {

    private static final long serialVersionUID = -5190622316275632233L;

    public EntityExtractionFailedException(String message) {
        super(message);
    }

    public EntityExtractionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
