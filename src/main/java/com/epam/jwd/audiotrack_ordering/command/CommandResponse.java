package com.epam.jwd.audiotrack_ordering.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
