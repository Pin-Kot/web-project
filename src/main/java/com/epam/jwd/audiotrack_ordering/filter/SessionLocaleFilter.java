package com.epam.jwd.audiotrack_ordering.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {

    private static final String LANGUAGE_ATTRIBUTE_NAME = "lang";
    private static final String DEFAULT_LANGUAGE = "en_US";
    private static final String NULL_VALUE = "null";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String currentLanguage = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE_NAME);
        if (currentLanguage == null || currentLanguage.equals(NULL_VALUE)) {
            request.getSession().setAttribute(LANGUAGE_ATTRIBUTE_NAME, DEFAULT_LANGUAGE);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
