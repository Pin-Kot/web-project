package com.epam.jwd.audiotrack_ordering.web.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
