package com.epam.jwd.audiotrack_ordering.web.controller;

import com.epam.jwd.audiotrack_ordering.web.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.web.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {

    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    static RequestFactory getInstance() {
        return SimpleRequestFactory.getInstance();
    }
}
