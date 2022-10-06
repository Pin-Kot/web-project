package com.epam.jwd.audiotrack_ordering.exception;

public class ResourceLoadingFailedException extends  RuntimeException {

    private static final long serialVersionUID = -4017293021668675754L;

    public ResourceLoadingFailedException(String message) {
        super(message);
    }

    public ResourceLoadingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
