package com.epam.jwd.audiotrack_ordering.controller;

public enum PagePaths {
    ACCOUNTS("/WEB-INF/jsp/accounts.jsp"),
    ALBUMS("/WEB-INF/jsp/albums.jsp"),
    ARTISTS("/WEB-INF/jsp/artists.jsp"),
    ERROR("/WEB-INF/jsp/error.jsp"),
    INDEX("/"),
    LOGIN("/WEB-INF/jsp/login.jsp"),
    MAIN("/WEB-INF/jsp/main.jsp"),
    PERSONAL_DATA("/WEB-INF/jsp/personal_data.jsp"),
    SIGN_UP("/WEB-INF/jsp/sign_up.jsp"),
    TRACKS("/WEB-INF/jsp/tracks.jsp"),
    USERS("/WEB-INF/jsp/users.jsp");

    private final String path;

    PagePaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static PagePaths of(String name) {
        for (PagePaths page : values()) {
            if (page.name().equalsIgnoreCase(name)) {
                return page;
            }
        }
        return MAIN;
    }
}
