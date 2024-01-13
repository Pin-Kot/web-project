package com.epam.jwd.audiotrack_ordering.controller;

public enum PagePaths {
    ACCOUNTS("/WEB-INF/jsp/accounts.jsp"),
    ADD_ALBUM("/WEB-INF/jsp/add_album.jsp"),
    ADD_ARTIST("/WEB-INF/jsp/add_artist.jsp"),
    ADD_IMAGE("/WEB-INF/jsp/add_image.jsp"),
    ADD_TRACK("/WEB-INF/jsp/add_track.jsp"),
    ADD_USER("/WEB-INF/jsp/add_user.jsp"),
    ADMIN("/WEB-INF/jsp/admin.jsp"),
    ALBUMS("/WEB-INF/jsp/albums.jsp"),
    ARTISTS("/WEB-INF/jsp/artists.jsp"),
    CHECKOUT("/WEB-INF/jsp/checkout.jsp"),
    DELETE("/WEB-INF/jsp/delete.jsp"),
    EDIT_DATA("/WEB-INF/jsp/edit_data.jsp"),
    EDIT_PASSWORD("/WEB-INF/jsp/edit_password.jsp"),
    ERROR("/WEB-INF/jsp/error.jsp"),
    IMAGES("/WEB-INF/jsp/images.jsp"),
    INDEX("/"),
    LOGIN("/WEB-INF/jsp/login.jsp"),
    MAIN("/WEB-INF/jsp/main.jsp"),
    ORDER("/WEB-INF/jsp/order.jsp"),
    PERSONAL_DATA("/WEB-INF/jsp/personal_data.jsp"),
    REVIEWS("/WEB-INF/jsp/reviews.jsp"),
    SHOPPING_CART("/WEB-INF/jsp/shopping_cart.jsp"),
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
