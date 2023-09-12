package com.epam.jwd.audiotrack_ordering.command;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface CommandRequest {

    void addAttributeToJSP(String name, Object attribute);

    String getParameter(String name);

    Part getPart(String name) throws IOException, ServletException;

    Collection<Part> getParts() throws IOException, ServletException;

    boolean sessionExists();

    boolean addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);

    void clearSession();

    void createSession();

    void removeFromSession(String name);

    String[] getParameters(String name);

    Cookie[] findCookies();
}
