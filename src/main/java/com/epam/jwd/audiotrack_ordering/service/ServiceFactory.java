package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.*;

public interface ServiceFactory {

    <T extends Entity> EntityService<T> serviceFor(Class<T> entityClass);

    default AccountService accountService() {
        return (AccountService) serviceFor(Account.class);
    }

    default AlbumService albumService() {
        return (AlbumService) serviceFor(Album.class);
    }

    default ArtistService artistService() {
        return (ArtistService) serviceFor(Artist.class);
    }

    default UserService userService() {
        return (UserService) serviceFor(User.class);
    }

    default TrackService trackService() {
        return (TrackService) serviceFor(Track.class);
    }

    static ServiceFactory getInstance() {
        return SimpleServiceFactory.getInstance();
    }
}
