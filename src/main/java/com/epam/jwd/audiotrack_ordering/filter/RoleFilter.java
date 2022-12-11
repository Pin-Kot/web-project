package com.epam.jwd.audiotrack_ordering.filter;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Role;
import com.epam.jwd.audiotrack_ordering.command.Command;
import com.epam.jwd.audiotrack_ordering.command.CommandRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebFilter(urlPatterns = "/*")
public class RoleFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(RoleFilter.class);
    private static final String COMMAND_PARAM_NAME = "command";
    private static final String ACCOUNT_SESSION_ATTRIBUTE_NAME = "account";
    private static final String ERROR_PAGE_URL = "/controller?command=show_error";

    private final Map<Role, Set<Command>> commandsByRoles;

    public RoleFilter() {
        commandsByRoles = new EnumMap<>(Role.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        for (CommandRegistry command : CommandRegistry.values()) {
            for (Role allowedRole : command.getAllowedRoles()) {
                final Set<Command> commands = commandsByRoles.computeIfAbsent(allowedRole, k -> new HashSet<>());
                commands.add(command.getCommand());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final String commandName = req.getParameter(COMMAND_PARAM_NAME);
        LOG.trace("Checking permission for command. commandName: {}", commandName);
        if (currentAccountHasPermissionForCommand(commandName, req)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(ERROR_PAGE_URL);
        }
    }

    private boolean currentAccountHasPermissionForCommand(String commandName, HttpServletRequest request) {
        Role currentAccountRole = retrieveCurrentAccountRole(request);
        final Command command = Command.of(commandName);
        final Set<Command> allowedCommands = commandsByRoles.get(currentAccountRole);
        return allowedCommands.contains(command);
    }

    private Role retrieveCurrentAccountRole(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
                .map(s -> (Account) s.getAttribute(ACCOUNT_SESSION_ATTRIBUTE_NAME))
                .map(Account::getRole)
                .orElse(Role.UNAUTHORIZED);
    }
}
