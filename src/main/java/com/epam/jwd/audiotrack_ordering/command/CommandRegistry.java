package com.epam.jwd.audiotrack_ordering.command;

import com.epam.jwd.audiotrack_ordering.command.impl.*;
import com.epam.jwd.audiotrack_ordering.entity.Role;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.audiotrack_ordering.entity.Role.ADMIN;
import static com.epam.jwd.audiotrack_ordering.entity.Role.UNAUTHORIZED;
import static com.epam.jwd.audiotrack_ordering.entity.Role.USER;

public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.getInstance(), "main_page"),
    ACCOUNTS_PAGE(ShowAccountsPageCommand.getInstance(), "show_accounts", ADMIN),
    ADD_ALBUM(AddAlbumCommand.getInstance(), "add_album", ADMIN),
    ADD_ARTIST(AddArtistCommand.getInstance(), "add_artist", ADMIN),
    ADD_ARTIST_PAGE(ShowAddArtistPageCommand.getInstance(), "show_add_artist", ADMIN),
    ADD_USER(AddUserCommand.getInstance(), "add_user", USER, ADMIN),
    ADD_USER_PAGE(ShowAddUserPageCommand.getInstance(), "show_add_user", USER, ADMIN),
    ADMIN_PAGE(ShowAdminPageCommand.getInstance(), "show_admin", ADMIN),
    ALBUMS_PAGE(ShowAlbumsPageCommand.getInstance(), "show_albums"),
    ARTISTS_PAGE(ShowArtistsPageCommand.getInstance(), "show_artists"),
    CHANGE_LANGUAGE(ChangeLanguageCommand.getInstance(), "change_language"),
    EDIT_DATA(EditUserDataCommand.getInstance(), "edit_data", USER, ADMIN),
    EDIT_PASSWORD(EditPasswordCommand.getInstance(), "edit_password", USER, ADMIN),
    EDIT_DATA_PAGE(ShowEditDataPageCommand.getInstance(), "show_editor", USER, ADMIN),
    EDIT_PASSWORD_PAGE(ShowEditPasswordPageCommand.getInstance(), "show_password_editor", USER, ADMIN),
    ERROR_PAGE(ShowErrorPageCommand.getInstance(), "show_error", UNAUTHORIZED),
    LOGIN(LoginCommand.getInstance(), "login", UNAUTHORIZED),
    LOGIN_PAGE(ShowLoginPageCommand.getInstance(), "show_login", UNAUTHORIZED),
    LOGOUT(LogoutCommand.getInstance(), "logout", USER, ADMIN),
    PERSONAL_DATA_PAGE(ShowPersonalDataCommand.getInstance(), "show_personal_data", USER, ADMIN),
    SHOW_ADD_ALBUM_PAGE(ShowAddAlbumPageCommand.getInstance(), "show_add_album", ADMIN),
    SHOW_ALBUM_TRACKS(ShowAlbumTracksPageCommand.getInstance(), "show_album_tracks"),
    SHOW_ARTIST_ALBUMS(ShowArtistAlbumsPageCommand.getInstance(), "show_artist_albums"),
    SHOW_ARTIST_TRACKS(ShowArtistTracksPageCommand.getInstance(), "show_artist_tracks"),
    SIGN_UP(SignUpCommand.getInstance(), "sign_up", UNAUTHORIZED),
    SIGN_UP_PAGE(ShowSignUpPageCommand.getInstance(), "show_sign_up", UNAUTHORIZED),
    TRACK_PAGE(ShowTracksPageCommand.getInstance(), "show_tracks"),
    USERS_PAGE(ShowUsersPageCommand.getInstance(), "show_users", ADMIN),
    DEFAULT(ShowMainPageCommand.getInstance(), "");

    private final Command command;
    private final String path;
    private final List<Role> allowedRoles;

    CommandRegistry(Command command, String path, Role... roles) {
        this.command = command;
        this.path = path;
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : Role.valuesAsList();
    }

    public Command getCommand() {
        return command;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    static Command of(String name) {
        for (CommandRegistry constant : values()) {
            if (constant.path.equalsIgnoreCase(name)) {
                return constant.command;
            }
        }
        return DEFAULT.command;
    }
}
