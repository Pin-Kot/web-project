package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.exception.EntityExtractionFailedException;

import java.util.Optional;

public interface TrackDao extends EntityDao<Track> {

    Optional<Track> findByTitle(String title) throws EntityExtractionFailedException;

    static TrackDao getInstance() {
        return MethodTrackDao.getInstance();
    }
}
