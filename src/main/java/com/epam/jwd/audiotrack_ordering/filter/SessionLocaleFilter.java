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

@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = "/*")
public class SessionLocaleFilter implements Filter {

    private static final String LANGUAGE_ATTRIBUTE_NAME = "lang";
    public static final String REQUEST_PARAMETER_NAME = "sessionLocale";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getParameter(REQUEST_PARAMETER_NAME) != null) {
            request.getSession().setAttribute(LANGUAGE_ATTRIBUTE_NAME, request.getParameter(REQUEST_PARAMETER_NAME));
        }
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
