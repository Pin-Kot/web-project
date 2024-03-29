package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.entity.Card;
import com.epam.jwd.audiotrack_ordering.entity.Entity;
import com.epam.jwd.audiotrack_ordering.entity.Image;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.impl.SimpleServiceFactory;

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

    default CardService cardService() {
        return (CardService) serviceFor(Card.class);
    }

    default ImageService imageService() {
        return (ImageService) serviceFor(Image.class);
    }

    default OrderService orderService() {
        return (OrderService) serviceFor(Order.class);
    }

    default ReviewService reviewService() {
        return (ReviewService) serviceFor(Review.class);
    }

    default TrackService trackService() {
        return (TrackService) serviceFor(Track.class);
    }

    default UserService userService() {
        return (UserService) serviceFor(User.class);
    }

    static ServiceFactory getInstance() {
        return SimpleServiceFactory.getInstance();
    }
}
