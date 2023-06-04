package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TrackService extends EntityService<Track> {

    Optional<Track> findByTitleByYearByPrice(String title, int year, BigDecimal price);

    List<Track> findTracksByArtistName(String artistName);

    List<Track> findTracksByAlbumTitle(String title);

    boolean createByArtistByAlbumWithValidator(Artist artist, Album album, String title, String strYear,
                                               String strPrice);
}
