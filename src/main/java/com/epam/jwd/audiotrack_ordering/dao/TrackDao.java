package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodTrackDao;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TrackDao extends EntityDao<Track> {

    void createTrack(Track track, Album album, Artist artist);

    Optional<Track> findByTitle(String title);

    Optional<Track> findByTitleByYearByPrice(String title, int year, BigDecimal price);

    List<Track> findTracksByArtistName(String artistName);

    List<Track> findTracksByAlbumTitle(String title);

    static TrackDao getInstance() {
        return MethodTrackDao.getInstance();
    }
}
