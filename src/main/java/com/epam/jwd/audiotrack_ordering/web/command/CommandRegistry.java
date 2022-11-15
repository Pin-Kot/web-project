package com.epam.jwd.audiotrack_ordering.web.command;

public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.getInstance(), "main_page"),
    ACCOUNTS_PAGE(ShowAccountsPageCommand.getInstance(), "show_accounts"),
    ALBUMS_PAGE(ShowAlbumsPageCommand.getInstance(), "show_albums"),
    ARTISTS_PAGE(ShowArtistsPageCommand.getInstance(), "show_artists"),
    LOGIN(LoginCommand.getInstance(), "login"),
    LOGIN_PAGE(ShowLoginPageCommand.getInstance(), "show_login"),
    LOGOUT(LogoutCommand.getInstance(), "logout"),
    USERS_PAGE(ShowUsersPageCommand.getInstance(), "show_users"),
    DEFAULT(ShowMainPageCommand.getInstance(), "");

    private final Command command;
    private final String path;

    CommandRegistry(Command command, String path) {
        this.command = command;
        this.path = path;
    }

    public Command getCommand() {
        return command;
    }

    static Command of(String name){
        for (CommandRegistry constant : values()) {
            if (constant.path.equalsIgnoreCase(name)) {
                return constant.command;
            }
        }
        return DEFAULT.command;
    };
}
