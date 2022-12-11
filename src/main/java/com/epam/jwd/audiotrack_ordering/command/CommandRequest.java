package com.epam.jwd.audiotrack_ordering.command;

import javax.servlet.http.Cookie;
import java.util.Optional;

public interface CommandRequest {

    void addAttributeToJSP(String name, Object attribute);

    String getParameter(String name);

    boolean sessionExists();

    boolean addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);

    void clearSession();

    void createSession();

    void removeFromSession(String name);

    String[] getParameters(String name);

    Cookie[] findCookies();
}
