package com.epam.jwd.audiotrack_ordering.web.controller;

import com.epam.jwd.audiotrack_ordering.web.command.CommandRequest;
import com.epam.jwd.audiotrack_ordering.web.command.CommandResponse;
import com.epam.jwd.audiotrack_ordering.web.command.PlainCommandResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRequestFactory implements RequestFactory {

    private final Map<String, CommandResponse> forwardResponseCache = new ConcurrentHashMap<>();
    private final Map<String, CommandResponse> redirectResponseCache = new ConcurrentHashMap<>();

    @Override
    public CommandRequest createRequest(HttpServletRequest request) {
        return new WrappingCommandRequest(request);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return forwardResponseCache.computeIfAbsent(path, PlainCommandResponse::new);
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return redirectResponseCache.computeIfAbsent(path, p -> new PlainCommandResponse(true, path));
    }

    static SimpleRequestFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimpleRequestFactory INSTANCE = new SimpleRequestFactory();
    }
}
