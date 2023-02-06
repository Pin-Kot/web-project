package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;
import java.util.Optional;

public interface TrackDao extends EntityDao<Track> {

    Optional<Track> findByTitle(String title);

    List<Track> findTracksByArtistName(String artistName);

    List<Track> findTRacksByAlbumTitle(String title);

    static TrackDao getInstance() {
        return MethodTrackDao.getInstance();
    }
}
